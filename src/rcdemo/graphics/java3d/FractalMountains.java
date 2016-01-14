package rcdemo.graphics.java3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

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
 */
public class FractalMountains extends Shape3D {

    public interface ColoringFunction {

        Color3f getColor(int i, int j);
    }

    java.util.Random rng;
//    final double dx = 8;
//    final double dy = 8;
//    final int div = 512;
    final int nnn=2;
    final double dx = 8*nnn;
    final double dy = 8*nnn;
    final int div = 512/nnn;
    int N = div + 1, M = div + 1;
    final double roughness = 0.4;
    final double maxHeight = 500;
    final double zshift = -maxHeight - 320;

    /**
     * Constructs a color cube with unit scale. The corners of the color cube
     * are [-1,-1,-1] and [1,1,1].
     * http://www.javaworld.com/article/2076745/learn-java/3d-graphic-java--render-fractal-landscapes.html
     */
    static double avg(double zpos[][], int is[], int js[], int M, int N) {
        int n = 0;
        double sum = 0;
        for (int k = 0; k < is.length; k++) {
            int i = is[k];
            int j = js[k];
            if (i >= 0 && i < M && j >= 0 && j < N) {
                n++;
                sum += zpos[i][j];
                //System.out.println("zpos: " + zpos[i][j]);
            }
        }
        //System.out.println("mean: " + sum/n);
        assert n >= 3;
        return sum / n;
    }

    double random() {
        return rng.nextDouble();
    }
    void fractalGen(double zpos[][], int s,
            double r, double alpha,
            int M, int N) {

        while (s >= 2) {
            int s2 = s / 2;
            alpha *= roughness;

            for (int i = 0; i + s2 < M; i += s) {
                for (int j = 0; j + s2 < N; j += s) {
                    double d = r * alpha * (2 * random() - 1);
                    int is[] = {i, i + s, i + s, i};
                    int js[] = {j, j, j + s, j + s};
                    double mean = avg(zpos, is, js, M, N);
                    //System.out.println("zpos1: " + zpos[i + s2][j + s2]);
                    zpos[i + s2][j + s2] = mean + d;
                    //System.out.println("zpos2: " + zpos[i + s2][j + s2]);
                }
            }
            for (int i = 0; i + s2 < M; i += s) {
                for (int j = 0; j < N; j += s) {
                    int is[] = {i, i + s2, i + s2, i + s};
                    int js[] = {j, j + s2, j - s2, j};
                    double mean = avg(zpos, is, js, M, N);
                    double d = r * alpha * (2 * random() - 1);
                    zpos[i + s2][j] = mean + d;
                }
            }
            for (int i = 0; i < M; i += s) {
                for (int j = 0; j + s2 < N; j += s) {
                    int is[] = {i, i + s2, i - s2, i};
                    int js[] = {j, j + s2, j + s2, j + s};
                    double mean = avg(zpos, is, js, M, N);
                    double d = r * alpha * (2 * random() - 1);
                    zpos[i][j + s2] = mean + d;
                }
            }
            s = s2;
        }
        //fractalGen(zpos, s/2, r, alpha/2, i, i+s, j, j+2);

    }

    public FractalMountains() {
        rng = new java.util.Random(1236);
        
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

        for (int i = 0; i < M; i += 1) {
            for (int j = 0; j < N; j += 1) {
                zpos[i][j] = zshift - 100;
                //zpos[i][j] += (xpos[i]*ypos[j])/200;
                zpos[i][j] += Math.sqrt(xpos[i] * xpos[i] + ypos[j] * ypos[j]) / 200;
                zpos[i][j] = Double.NaN;
            }
        }
        for (int i = 0; i < M; i += div) {
            for (int j = 0; j < N; j += div) {
                zpos[i][j] = maxHeight * random() + zshift;
                //zpos[i][j] += (xpos[i]+ypos[j])/40;
                zpos[i][j] += Math.sqrt(xpos[i] * xpos[i] + ypos[j] * ypos[j]) / 20;
            }
        }
        fractalGen(zpos, div, maxHeight, 1, M, N);

        Point3d coords[][] = new Point3d[M][N];
        Color3f colors[][] = new Color3f[M][N];

        double min = zpos[0][0], max = zpos[0][0];
        for (int i = 0; i < M; i += 1) {
            for (int j = 0; j < N; j += 1) {
                zpos[i][j] = 2 * (zpos[i][j] - zshift) + zshift;
                min = Math.min(min, zpos[i][j]);
                max = Math.max(max, zpos[i][j]);
            }
        }
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                coords[i][j] = new Point3d(xpos[i], ypos[j], zpos[i][j]);
                float c = (float) random();
                c = (float) ((zpos[i][j] - min) / (max - min));
                c = Math.max(c, 0.2f);
                colors[i][j] = new Color3f(c, c, c);
                //colors[i][j] = new Color3f(0,0,0);
            }
        }

        makeGeometry(M, N, coords, colors);
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
}
