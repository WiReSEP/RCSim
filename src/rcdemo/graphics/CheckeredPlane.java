package rcdemo.graphics;

import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;

/**
 * Simple color-per-vertex cube with a different color for each face
 */
public class CheckeredPlane extends Shape3D {
    private static float[] verts;

    private static float[] colors;

    double scale;

    /**
     * Constructs a color cube with unit scale.  The corners of the
     * color cube are [-1,-1,-1] and [1,1,1].
     */
    public CheckeredPlane() {
        
        float dx = 10;
        float dy = 10;
        int n = 20, n1=n+1;
        verts = new float[4 * n1*n1 * 3];
        colors = new float[4 * n1*n1 * 3];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int ind = 3 * (i * n1 + j) * 4;
                verts[ind++] = i * dx;
                verts[ind++] = j * dy;
                verts[ind++] = 0;
                verts[ind++] = i * dx;
                verts[ind++] = (j+1) * dy;
                verts[ind++] = 0;
                verts[ind++] = (i+1) * dx;
                verts[ind++] = (j+1) * dy;
                verts[ind++] = 0;
                verts[ind++] = (i+1) * dx;
                verts[ind++] = j * dy;
                verts[ind++] = 0;

                ind = 3 * (i * n1 + j) * 4;
                int c = ind % 8;
                int c1 = c & 1;
                int c2 = (c & 2)/2;
                int c3 = (c & 4)/4;
                colors[ind++] = c1;
                colors[ind++] = c2;
                colors[ind++] = c3;
                colors[ind++] = c1;
                colors[ind++] = c2;
                colors[ind++] = c3;
                colors[ind++] = c1;
                colors[ind++] = c2;
                colors[ind++] = c3;
                colors[ind++] = c1;
                colors[ind++] = c2;
                colors[ind++] = c3;
            }
        }
        
	QuadArray plane = new QuadArray(n1*n1*4, QuadArray.COORDINATES |
		QuadArray.COLOR_3);
        

	plane.setCoordinates(0, verts);
	plane.setColors(0, colors);

	this.setGeometry(plane);

	scale = 1.0;
    }


    /**
     * Constructs a color cube with the specified scale.  The corners of the
     * color cube are [-scale,-scale,-scale] and [scale,scale,scale].
     * @param scale the scale of the cube
     */
    public CheckeredPlane(double scale) {
	QuadArray cube = new QuadArray(24, QuadArray.COORDINATES |
		QuadArray.COLOR_3);

	float scaledVerts[] = new float[verts.length];
	for (int i = 0; i < verts.length; i++)
	    scaledVerts[i] = verts[i] * (float)scale;

	cube.setCoordinates(0, scaledVerts);
	cube.setColors(0, colors);

	this.setGeometry(cube);

	this.scale = scale;
    }

    /**
     * @deprecated ColorCube now extends shape so it is no longer necessary
     * to call this method.
     */
    public Shape3D getShape() {
	return this;
    }

    /**
     * Returns the scale of the Cube
     *
     * @since Java 3D 1.2.1
     */
    public double getScale() {
	return scale;
    }
}
