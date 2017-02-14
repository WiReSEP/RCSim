package de.tubs.wire.graphics.java3d;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import java.util.Random;
import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.Material;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import de.tubs.wire.graphics.terrain.DiamonSquareGenerator;
import de.tubs.wire.graphics.terrain.TerrainGenerator;


        
/**
 * Simple color-per-vertex cube with a different color for each face
 * 
 * http://devmag.org.za/2009/04/25/perlin-noise/
 * https://en.wikipedia.org/wiki/Scenery_generator
 * https://en.wikipedia.org/wiki/Fractal_landscape
 * https://en.wikipedia.org/wiki/Simplex_noise
 * https://en.wikipedia.org/wiki/Perlin_noise
 * http://codeflow.org/entries/2010/dec/09/minecraft-like-rendering-experiments-in-opengl-4/
 * http://gamedev.stackexchange.com/questions/55895/terrain-generation-advanced
 * http://www.javaworld.com/article/2076745/learn-java/3d-graphic-java--render-fractal-landscapes.html
 */
public class FractalMountains extends Shape3D {

    public interface ColoringFunction {

        Color3f getColor(int i, int j);
    }

//    final double dx = 8;
//    final double dy = 8;
//    final int div = 512;
    final int nnn=2;
    final double dx = 8*nnn;
    final double dy = 8*nnn;
    final int div = 512/nnn;
    int N = div + 1, M = div + 1;



    public FractalMountains() {
        
        N = ((N + div - 1) / div) * div + 1;
        M = ((M + div - 1) / div) * div + 1;

        //final int N = 3, M = 4;
        double xpos[] = new double[M];
        double ypos[] = new double[N];
        double zpos[][] = new double[M][N];

        for (int i = 0; i < M; i++) {
            xpos[i] = (i - 0.5 * M) * dx;
        }
        for (int j = 0; j < N; j++) {
            ypos[j] = (j - 0.5 * N) * dy;
        }


    
        TerrainGenerator gen = new DiamonSquareGenerator();
        zpos = gen.generate(N);
        final double maxHeight = DiamonSquareGenerator.max(zpos);
        final double zshift = -maxHeight - 220;
        for (int i = 0; i < M; i += 1)
            for (int j = 0; j < N; j += 1)
                zpos[i][j] += zshift;

        Point3d coords[][] = new Point3d[M][N];
        Color3f colors[][] = new Color3f[M][N];

        double min = zpos[0][0], max = zpos[0][0];
        for (int i = 0; i < M; i += 1) {
            for (int j = 0; j < N; j += 1) {
                //zpos[i][j] = 2 * (zpos[i][j] - zshift) + zshift;
                min = Math.min(min, zpos[i][j]);
                max = Math.max(max, zpos[i][j]);
            }
        }
        System.out.format("min %s  max %s\n", min, max);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                coords[i][j] = new Point3d(xpos[i], ypos[j], zpos[i][j]);
                float c;
                c = (float) ((zpos[i][j] - min) / (max - min));
                c = Math.max(c, 0.2f);
                colors[i][j] = new Color3f(c+0.3f, c, c);
                
                //colors[i][j] = new Color3f(0,0,0);
            }
        }

        makeGeometry2(M, N, coords, colors);
    }

    static <Type> void convertToQuads(int M, int N, Type in[][], Type out[]) {
        assert in.length == M;
        assert in[0].length == N;
        assert out.length == 4 * (M - 1) * (N - 1);

        for (int i = 0; i < M - 1; i++) {
            for (int j = 0; j < N - 1; j++) {
                int k = 4 * (j + i * (N - 1));
                out[k++] = in[i + 0][j + 0];
                out[k++] = in[i + 1][j + 0];
                out[k++] = in[i + 1][j + 1];
                out[k++] = in[i + 0][j + 1];
            }
        }
    }

    static <Type> void convertToQuads(int M, int N, Type in[][], Type out[], int index[]) {
        assert in.length == M;
        assert in[0].length == N;
        assert out.length == M*N;
        assert index.length == 4 * (M - 1) * (N - 1);
                
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                out[i + M * j] = in[i][j];
            }
        }
        for (int i = 0; i < M - 1; i++) {
            for (int j = 0; j < N - 1; j++) {
                int k = 4 * (j + i * (N - 1));
                index[k++] = (i + 0) + M * (j + 0);
                index[k++] = (i + 1) + M * (j + 0);
                index[k++] = (i + 1) + M * (j + 1);
                index[k++] = (i + 0) + M * (j + 1);
            }
        }
    }
    
    Vector3f compNormal(Point3d p1, Point3d p2, Point3d p3) {
        Vector3d v1 = new Vector3d(p1);
        Vector3d v2 = new Vector3d(p1);
        Vector3d v = new Vector3d();
        v1.sub(p2);
        v2.sub(p3);
        v.cross(v1, v2);
        v.normalize();
        return new Vector3f(v);
    }

    final void makeGeometry(int M, int N,
            Point3d coords[][], Color3f colors[][]) {
        int n = (M - 1) * (N - 1) * 4;
        QuadArray plane = new QuadArray(n,
                QuadArray.COORDINATES
                | QuadArray.COLOR_3
                | QuadArray.NORMALS);

        Point3d[] coordsQuad = new Point3d[n];
        convertToQuads(M, N, coords, coordsQuad);
        System.out.println("coords: " + coordsQuad);
        plane.setCoordinates(0, coordsQuad);

        Vector3f[] normalsQuad = new Vector3f[n];
        for (int i = 0; i < n; i += 4) {
            normalsQuad[i + 0] = compNormal(coordsQuad[i + 3], coordsQuad[i + 0], coordsQuad[i + 1]);
            normalsQuad[i + 1] = compNormal(coordsQuad[i + 0], coordsQuad[i + 1], coordsQuad[i + 2]);
            normalsQuad[i + 2] = compNormal(coordsQuad[i + 1], coordsQuad[i + 2], coordsQuad[i + 3]);
            normalsQuad[i + 3] = compNormal(coordsQuad[i + 2], coordsQuad[i + 3], coordsQuad[i + 0]);
        }
        plane.setNormals(0, normalsQuad);

        Color3f[] colorsQuad = new Color3f[n];
        convertToQuads(M, N, colors, colorsQuad);
        plane.setColors(0, colorsQuad);

        
        Appearance appearance = new Appearance();
        Material material = new Material();
        material.setLightingEnable(true);
        material.setEmissiveColor(new Color3f(0.0f, 0.0f, 0.0f));
        //Color3f ambColor = new Color3f(0.8f, 1.0f, 0.0f);
        Color3f ambColor = new Color3f(0.8f, 0.8f, 0.6f);
        material.setAmbientColor( ambColor );
        material.setDiffuseColor( ambColor );
        material.setSpecularColor(new Color3f(0.2f, 0.2f, 0.2f));
        material.setShininess(15.0f);
        //material.
        
        appearance.setMaterial(material);
        this.setAppearance(appearance);
        
        this.setGeometry(plane);
    }



    final void makeGeometry2(int M, int N,
            Point3d coords[][], Color3f colors[][]) {
        int n = (M - 1) * (N - 1) * 4;

        int[] indicesQuad = new int[n];
        Point3d[] coordsQuad = new Point3d[M*N];
        convertToQuads(M, N, coords, coordsQuad, indicesQuad);

        Color3f[] colorsQuad = new Color3f[M*N];
        convertToQuads(M, N, colors, colorsQuad, indicesQuad);

        IndexedQuadArray plane = new IndexedQuadArray(M*N,
                QuadArray.COORDINATES
                | QuadArray.COLOR_3
                | QuadArray.NORMALS, n);
        plane.setCoordinates(0, coordsQuad);
        plane.setCoordinateIndices(0, indicesQuad);
        plane.setColors(0, colorsQuad);
        //assert(false);
//        GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
//        gi.setCoordinates(coordsQuad);
//        gi.setCoordinateIndices(indicesQuad);
//        gi.setColors(colorsQuad);
        GeometryInfo gi = new GeometryInfo(plane);
        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(gi);
//        Stripifier st = new Stripifier();
//        st.stripify(gi);        
        GeometryArray plane2 = gi.getIndexedGeometryArray();

        
        Appearance appearance = new Appearance();
        Material material = new Material();
        material.setLightingEnable(true);
        material.setEmissiveColor(new Color3f(0.0f, 0.0f, 0.0f));
        //Color3f ambColor = new Color3f(0.8f, 1.0f, 0.0f);
        Color3f ambColor = new Color3f(0.8f, 0.8f, 0.6f);
        material.setAmbientColor( ambColor );
        material.setDiffuseColor( ambColor );
        material.setSpecularColor(new Color3f(0.2f, 0.2f, 0.2f));
        material.setShininess(15.0f);
        //material.
        
        appearance.setMaterial(material);
        
        this.setAppearance(appearance);
        //this.setGeometry(plane);
        this.setGeometry(plane2);
    }
}
