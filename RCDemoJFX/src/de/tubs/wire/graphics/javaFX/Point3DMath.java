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
package de.tubs.wire.graphics.javaFX;

import javafx.geometry.Point3D;
import de.tubs.wire.simulator.math.VectorMath;

/**
 *
 * @author ezander
 */
public class Point3DMath extends VectorMath<Point3D> {

    @Override
    public Point3D zero() {
        return Point3D.ZERO;
    }

    @Override
    public Point3D copy(Point3D v) {
        return new Point3D(v.getX(), v.getY(), v.getZ());
    }

    @Override
    public double[] toDouble(Point3D v) {
        return new double[]{v.getX(), v.getY(), v.getZ()};
    }

    @Override
    public Point3D fromDouble(double[] d) {
        assert d.length == 3;
        return new Point3D(d[0], d[1], d[2]);
    }

    @Override
    public Point3D add(Point3D v1, Point3D v2) {
        return v1.add(v2);
    }

    @Override
    public Point3D multiply(Point3D v1, double d) {
        return v1.multiply(d);
    }

    @Override
    public Point3D subtract(Point3D v1, Point3D v2) {
        return v1.subtract(v2);
    }

    @Override
    public Point3D crossProduct(Point3D v1, Point3D v2) {
        return v1.crossProduct(v2);
    }

    @Override
    public double dotProduct(Point3D v1, Point3D v2) {
        return v1.dotProduct(v2);
    }
    
}
