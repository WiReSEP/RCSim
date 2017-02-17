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
package de.tubs.wire.simulator.physics;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * A {@link ForceModel} modelling constant forces like {@link #createGravityForceModel gravity}.
 * 
 * @author ezander
 */
public class ConstantForceModel implements ForceModel {
    
    /** Gravitational acceleration on Earth ({@value}) */
    public static final double MASS_ACCEL_EARTH = 9.80665;
    /** Gravitational acceleration on Earth at the poles ({@value}) */
    public static final double MASS_ACCEL_EARTH_POLE = 9.832;
    /** Gravitational acceleration on Earth on the equator ({@value}) */
    public static final double MASS_ACCEL_EARTH_EQUATOR = 9.780;
    
    /** Gravitational acceleration on the Moon ({@value}) */
    public static final double MASS_ACCEL_MOON = 1.622;
    
    
    /** Gravitational acceleration on Mercury ({@value}) */
    public static final double MASS_ACCEL_MERCURY = 3.7;
    /** Gravitational acceleration on Venus ({@value}) */
    public static final double MASS_ACCEL_VENUS = 8.87;
    /** Gravitational acceleration on Mars ({@value}) */
    public static final double MASS_ACCEL_MARS = 3.711;
    /** Gravitational acceleration on Jupiter ({@value}) */
    public static final double MASS_ACCEL_JUPITER = 24.79;
    /** Gravitational acceleration on Saturn ({@value}) */
    public static final double MASS_ACCEL_SATURN = 10.44;
    /** Gravitational acceleration on Uranus ({@value}) */
    public static final double MASS_ACCEL_URANUS = 8.69;
    /** Gravitational acceleration on Neptun ({@value}) */
    public static final double MASS_ACCEL_NEPTUN = 11.15;
    
    
            
    /** The constant force vector */
    private final double[] F;

    /**
     * Create a constant force model.
     * 
     * @param F The constant force.
     */
    public ConstantForceModel(double[] F) {
        this.F = F.clone();
    }

    /**
     * Create a gravity force model.
     * 
     * The force here is [0, 0, -m*g], i.e. m*g in negative z direction.
     * 
     * @param m The mass.
     * @param g Gravitational acceleration.
     * @return The gravity force model.
     */
    public static ConstantForceModel createGravityForceModel(double m, double g) {
        double F[] = {0, 0, -m*g};
        return new ConstantForceModel(F);
    }

    /**
     * Create a gravity force model with standard earth gravity.
     * 
     * The force here is [0, 0, -m*g], i.e. m*g in negative z direction.
     * 
     * @param m The mass.
     * @return The gravity force model.
     */
    public static ConstantForceModel createGravityForceModel(double m) {
        return createGravityForceModel(m, MASS_ACCEL_EARTH);
    }
    
    @Override
    public RealVector getForce(RealVector x, RealVector v) {
        return new ArrayRealVector(F);
    }

    /**
     * {@inheritDoc}
     * <p>
     * For a constant force the potential energy is equal to the inner product 
     * of the force vector and the position vector.
     */
    @Override
    public double getPotentialEnergy(RealVector x, RealVector v) {
        return -getForce(x, v).dotProduct(x);
    }
}
