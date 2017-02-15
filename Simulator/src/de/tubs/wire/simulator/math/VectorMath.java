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
public abstract class VectorMath<Vector> {
    public abstract Vector zero();
    public abstract Vector copy(Vector v);
    public abstract double[] toDouble(Vector v);
    public abstract Vector fromDouble(double[] d);
    
    public abstract Vector add(Vector v1, Vector v2);
    public abstract Vector subtract(Vector v1, Vector v2);
    public abstract Vector multiply(Vector v1, double d);
    
    public abstract Vector crossProduct(Vector v1, Vector v2);
    public abstract double dotProduct(Vector v1, Vector v2);
   
    public Vector setEntry(Vector v, int index, double d) {
        double[] x = toDouble(v);
        x[index]=d;
        return fromDouble(x);
    }
    public double getEntry(Vector v, int index) {
        double[] x = toDouble(v);
        return x[index];
    }
    public Vector unit(int index){
        return setEntry(zero(), index, 1);
    }
    public double norm(Vector v) {
        return Math.sqrt(dotProduct(v, v));
    }
    public Vector normalize(Vector v) {
        return multiply(v, 1.0/norm(v));
    }
    public double distance(Vector v1, Vector v2) {
        return norm(subtract(v1, v2));
    }
    
}


