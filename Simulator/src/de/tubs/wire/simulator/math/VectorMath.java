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
 * Abstraction of common vector math operations.
 * 
 * Different Java vector classes neither have a common base class nor are all 
 * operations the same, so that it they can neither be used in generic code via 
 * polymorphism nor as generic type. This class abstracts the most important 
 * vector operations and can be used in generic code. For each vector type the 
 * generic code is used for, a subclass implementing the abstract methods for the
 * concrete vector type is necessary.
 * 
 * 
 * @author ezander
 * @param <Vector> The vector class.
 */
public abstract class VectorMath<Vector> {
    /**
     * Create zero vector.
     * @return A zero vector of type Vector.
     */
    public abstract Vector zero();
    
    /**
     * Make a copy of the vector.
     * 
     * @param v The vector to be copied.
     * @return The copy of the vector.
     */
    public abstract Vector copy(Vector v);
    
    /**
     * Create double array from the vector.
     * 
     * @param v The vector.
     * @return The double array.
     */
    public abstract double[] toDouble(Vector v);
    
    /**
     * Create vector from double array.
     * @param d The double array.
     * @return The vector.
     */
    public abstract Vector fromDouble(double[] d);
    
    /**
     * Add to vector.
     * 
     * @param v1 A vector.
     * @param v2 Another vector.
     * @return The sum of the two vectors.
     */
    public abstract Vector add(Vector v1, Vector v2);
    
    /**
     * Subtract a vector from another.
     * 
     * @param v1 A vector.
     * @param v2 Another vector.
     * @return The difference of the two vectors.
     */
    public abstract Vector subtract(Vector v1, Vector v2);
    
    /**
     * Multiply a vector with a scalar.
     * 
     * @param v1 The vector.
     * @param d The scalar.
     * @return The product.
     */
    public abstract Vector multiply(Vector v1, double d);
    
    /**
     * Compute the cross product of two vectors (in 3D).
     * 
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return Their cross product.
     */
    public abstract Vector crossProduct(Vector v1, Vector v2);
    
    /**
     * Compute the scalar product of two vectors.
     * 
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return Their scalar product.
     */
    public abstract double dotProduct(Vector v1, Vector v2);
   
    /**
     * Set a component of the vector.
     * 
     * @param v The vector.
     * @param index Index of the component to set.
     * @param d New value of the component.
     * @return The vector itself (for chaining).
     */
    public Vector setEntry(Vector v, int index, double d) {
        double[] x = toDouble(v);
        x[index]=d;
        return fromDouble(x);
    }
    
    /**
     * Get a component of the vector.
     * 
     * @param v The vector.
     * @param index The index of the component.
     * @return The value of the component.
     */
    public double getEntry(Vector v, int index) {
        double[] x = toDouble(v);
        return x[index];
    }
    
    /**
     * Set vector to unit vector in given direction.
     * 
     * @param index
     * @return The vector itself.
     */
    public Vector unit(int index){
        return setEntry(zero(), index, 1);
    }
    
    /**
     * Compute the Euclidean norm of the vector.
     * 
     * @param v The vector.
     * @return The norm.
     */
    public double norm(Vector v) {
        return Math.sqrt(dotProduct(v, v));
    }
    
    /**
     * Normalise a vector.
     * 
     * @param v The vector.
     * @return The vector itself, now having norm one.
     */
    public Vector normalize(Vector v) {
        return multiply(v, 1.0/norm(v));
    }
    
    /**
     * Compute distance between two vectors (regarded as points).
     * 
     * @param v1 The first vector.
     * @param v2 The second vector.
     * @return The distance between them.
     */
    public double distance(Vector v1, Vector v2) {
        return norm(subtract(v1, v2));
    }
    
}


