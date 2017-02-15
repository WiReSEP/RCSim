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
package de.tubs.wire.graphics.java3d;

import javax.vecmath.Vector3d;
import de.tubs.wire.simulator.math.VectorArithmetic;

/**
 *
 * @author ezander
 */
class Vector3dArithmetic extends VectorArithmetic<Vector3d> {

    @Override
    public Vector3d zero() {
        return new Vector3d();
    }

    @Override
    public Vector3d copy(Vector3d v) {
        return new Vector3d(v.getX(), v.getY(), v.getZ());
    }

    @Override
    public double[] toDouble(Vector3d v) {
        return new double[]{v.getX(), v.getY(), v.getZ()};
    }

    @Override
    public Vector3d fromDouble(double[] d) {
        assert d.length == 3;
        return new Vector3d(d[0], d[1], d[2]);
    }

    @Override
    public Vector3d add(Vector3d v1, Vector3d v2) {
        Vector3d v = copy(v1);
        v.add(v2);
        return v;
    }

    @Override
    public Vector3d multiply(Vector3d v1, double d) {
        Vector3d v = copy(v1);
        v.scale(d);
        return v;
    }

    @Override
    public Vector3d subtract(Vector3d v1, Vector3d v2) {
        Vector3d v = copy(v1);
        v.sub(v2);
        return v;
    }

    @Override
    public Vector3d crossProduct(Vector3d v1, Vector3d v2) {
        Vector3d v = copy(v1);
        v.cross(v1, v2);
        return v;
    }

    @Override
    public double dotProduct(Vector3d v1, Vector3d v2) {
        return v1.dot(v2);
    }
    
}
