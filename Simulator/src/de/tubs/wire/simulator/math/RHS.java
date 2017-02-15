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
 *
 * @author ezander
 */
public class RHS<Vector> {

    private final Vector forward;
    private final Vector left;
    private final Vector up;

    protected RHS(Vector forward, Vector left, Vector up) {
        this.forward = forward;
        this.left = left;
        this.up = up;
    }

    public static <Vector> RHS<Vector> createRHS(Vector forward, Vector up, VectorMath<Vector> va) {
        Vector left = va.normalize(va.crossProduct(up, forward));
        up = va.normalize(va.crossProduct(forward, left));
        forward = va.normalize(forward);
        return new RHS<>(forward, left, up);
    }

    public Vector getForward() {
        return forward;
    }

    public Vector getLeft() {
        return left;
    }

    public Vector getUp() {
        return up;
    }
}
