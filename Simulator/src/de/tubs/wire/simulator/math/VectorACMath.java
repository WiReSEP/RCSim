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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.ArrayRealVector;


/**
 *
 * @author ezander
 */
public class VectorACMath extends VectorMath<ArrayRealVector> {

    @Override
    public ArrayRealVector zero() {
        return new ArrayRealVector();
    }

    @Override
    public ArrayRealVector copy(ArrayRealVector v) {
        return v.copy();
    }

    @Override
    public double[] toDouble(ArrayRealVector v) {
        return v.toArray();
    }

    @Override
    public ArrayRealVector fromDouble(double[] d) {
        return new ArrayRealVector(d);
    }

    @Override
    public ArrayRealVector add(ArrayRealVector v1, ArrayRealVector v2) {
        return v1.add(v2);
    }

    @Override
    public ArrayRealVector multiply(ArrayRealVector v1, double d) {
        ArrayRealVector v = copy(v1);
        v.mapMultiplyToSelf(d);
        return v;
    }

    @Override
    public ArrayRealVector subtract(ArrayRealVector v1, ArrayRealVector v2) {
        return v1.subtract(v2);
    }

    @Override
    public ArrayRealVector crossProduct(ArrayRealVector v1, ArrayRealVector v2) {
        assert( v1.getDimension()==3 && v2.getDimension()==3 );
        Vector3D v3d1 = new Vector3D(v1.getDataRef());
        Vector3D v3d2 = new Vector3D(v2.getDataRef());
        return new ArrayRealVector( Vector3D.crossProduct(v3d1, v3d2).toArray() );
    }

    @Override
    public double dotProduct(ArrayRealVector v1, ArrayRealVector v2) {
        return v1.dotProduct(v2);
    }
    
}
