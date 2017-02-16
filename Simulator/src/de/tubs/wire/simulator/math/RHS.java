/*
 * Copyright (C) 2016 ezander
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.tubs.wire.simulator.math;

/**
 * Creates and represents a right handed coordinate system (RHS). 
 * 
 * An RHS is defined by three vectors, which determine the (local) up, left and 
 * forward direction. 
 * 
 * @author ezander
 * @param <Vector> The vector class.
 */
public class RHS<Vector> {

    private final Vector forward;
    private final Vector left;
    private final Vector up;

    /**
     * Generates a RHS from three vectors.
     * 
     * Note, that no checking is done on the vectors.
     * @param forward The forward pointing vector.
     * @param left The left pointing vector.
     * @param up The upward pointing vector.
     */
    protected RHS(Vector forward, Vector left, Vector up) {
        this.forward = forward;
        this.left = left;
        this.up = up;
    }

    /**
     * Generates a RHS from the forward and up vector.
     * 
     * The result left vector is orthogonal to forward and up and all three 
     * vectors are normalised. For a moving observer, the forward vector is 
     * given by the current velocity and the up vector by the yaw vector.
     * 
     * @param <Vector> The vector class.
     * @param forward The forward pointing vector.
     * @param up The upward pointing vector.
     * @param vecmath The VectorMath object.
     * @return An RHS.
     */
    public static <Vector> RHS<Vector> createRHS(Vector forward, Vector up, VectorMath<Vector> vecmath) {
        Vector left = vecmath.normalize(vecmath.crossProduct(up, forward));
        up = vecmath.normalize(vecmath.crossProduct(forward, left));
        forward = vecmath.normalize(forward);
        return new RHS<>(forward, left, up);
    }

    /**
     * Get the forward pointing vector.
     * 
     * @return The forward pointing vector.
     */
    public Vector getForward() {
        return forward;
    }

    /**
     * Get the left pointing vector.
     * 
     * @return The left pointing vector.
     */
    public Vector getLeft() {
        return left;
    }

    /**
     * Get the upward pointing vector.
     * 
     * @return The upward pointing vector.
     */
    public Vector getUp() {
        return up;
    }
}
