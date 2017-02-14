package rcdemo.graphics.java3d;

import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;

/**
 * Simple color-per-vertex cube with a different color for each face
 */
public class CheckeredPlane extends Shape3D {
    private float[] verts;

    private Color3f[] colors;

    public interface ColoringFunction {
        Color3f getColor(int i, int j);
    }
    /**
     * Constructs a color cube with unit scale.  The corners of the
     * color cube are [-1,-1,-1] and [1,1,1].
     */
    public CheckeredPlane() {
        ColoringFunction func;
        func = (i,j) -> new Color3f((i+j)%2, (i+j)%2, (i+j)%2);
        
        float dx = 10;
        float dy = 10;
        int n = 200, n1=n+1;
        verts = new float[4 * n*n * 3];
        colors = new Color3f[4 * n*n];
        float z0 = 1.0f;
        
        int ind = 0;
        for (int i = -n/2; i < n/2; i++) {
            for (int j = -n/2; j < n/2; j++) {
                verts[ind++] = i * dx;
                verts[ind++] = j * dy;
                verts[ind++] = z0;
                verts[ind++] = (i+1) * dx;
                verts[ind++] = j * dy;
                verts[ind++] = z0;
                verts[ind++] = (i+1) * dx;
                verts[ind++] = (j+1) * dy;
                verts[ind++] = z0;
                verts[ind++] = i * dx;
                verts[ind++] = (j+1) * dy;
                verts[ind++] = z0;
            }
        }
                
        ind = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                //int c = (ind/12) % 8;
                int c = (ind/4+i) % 8;
                float c1 = c & 1;
                float c2 = (c & 2)/2;
                float c3 = (c & 4)/4;
                float f = 0.2f;
                c1 *= f;
                c2 *= 0.4+f;
                c3 *= f;
                //System.out.format("%s %s %s %s\n", c, c1, c2, c3);
                colors[ind++] = new Color3f(c1, c2, c3);
                colors[ind++] = new Color3f(c1, c2, c3);
                colors[ind++] = new Color3f(c1, c2, c3);
                colors[ind++] = new Color3f(c1, c2, c3);
            }
        }
        
	QuadArray plane = new QuadArray(n*n*4, 
                QuadArray.COORDINATES |
		QuadArray.COLOR_3);
        

	plane.setCoordinates(0, verts);
	plane.setColors(0, colors);
	this.setGeometry(plane);
    }
}
