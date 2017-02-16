package de.tubs.wire.graphics.java3d;

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

    public static final ColoringFunction COLORING_BLACK_WHITE
            = (i, j) -> new Color3f((i + j) % 2, (i + j) % 2, (i + j) % 2);

    public static final ColoringFunction COLORING_RED_WHITE
            = (i, j) -> new Color3f(1, (i + j) % 2, (i + j) % 2);

    //private static final float[][] RGBY = {{1,0,0}, {0,0.9f,0}, {0,0,1}, {0.9f,0.9f,0}};
    private static final float[][] RGBY = {{1, 0, 0}, {0, 0.9f, 0}, {0.9f, 0.9f, 0}, {0, 0, 1}};
    public static final ColoringFunction COLORING_RGBY
            = (i, j) -> {
                int k = (i % 2) + 2 * (j % 2);
                return new Color3f(RGBY[k][0], RGBY[k][1], RGBY[k][2]);
            };

    public static final ColoringFunction COLORING_STRIPED
            = (i, j) -> {
                float f = 0.6f;
                int ind = (i + j) % 8;
                float c1 = f * (ind & 1);
                float c2 = f * ((ind & 2) / 2);
                float c3 = f * ((ind & 4) / 4);
                return new Color3f(c1 + (0.8f - f), c2 + (0.8f - f), c3 + (0.8f - f));
            };

    public static final ColoringFunction COLORING_RANDOM
            = (i, j) -> new Color3f((float)Math.random(), (float)Math.random(), (float)Math.random());

    /**
     * Constructs a checkered plane.
     */
    public CheckeredPlane() {
        this(COLORING_RED_WHITE);
    }
    
    public CheckeredPlane(ColoringFunction coloring) {
        createPlane(coloring);
    }

    protected final void createPlane(ColoringFunction func) {
        float dx = 10;
        float dy = 10;
        int n = 200, n1 = n + 1;
        verts = new float[4 * n * n * 3];
        colors = new Color3f[4 * n * n];
        float z0 = 1.0f;
        int ind = 0;
        for (int i = -n / 2; i < n / 2; i++) {
            for (int j = -n / 2; j < n / 2; j++) {
                verts[ind++] = i * dx;
                verts[ind++] = j * dy;
                verts[ind++] = z0;
                verts[ind++] = (i + 1) * dx;
                verts[ind++] = j * dy;
                verts[ind++] = z0;
                verts[ind++] = (i + 1) * dx;
                verts[ind++] = (j + 1) * dy;
                verts[ind++] = z0;
                verts[ind++] = i * dx;
                verts[ind++] = (j + 1) * dy;
                verts[ind++] = z0;
            }
        }
        ind = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Color3f col = func.getColor(i, j);
                colors[ind++] = col;
                colors[ind++] = col;
                colors[ind++] = col;
                colors[ind++] = col;
            }
        }
        QuadArray plane = new QuadArray(n * n * 4,
                QuadArray.COORDINATES | QuadArray.COLOR_3);
        plane.setCoordinates(0, verts);
        plane.setColors(0, colors);
        this.setGeometry(plane);
    }
}
