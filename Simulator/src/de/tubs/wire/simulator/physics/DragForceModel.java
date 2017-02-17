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

import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.linear.RealVector;

/**
 * A ForceModel implementing drag by fluids (like air).
 * 
 * @author ezander
 * @see <a href="https://en.wikipedia.org/wiki/Drag_equation">Wikipedia: Drag equation</a>
 * @see <a href="https://en.wikipedia.org/wiki/Drag_coefficient">Wikipedia: Drag coefficient</a>
 */
public class DragForceModel implements ForceModel {
    
    /** Drag coefficient of a sphere ({@value}) */
    public static final double CD_SPHERE = 0.47;
    
    /** Drag coefficient of a half-sphere ({@value}) */
    public static final double CD_HALFSPHERE = 0.42;

    /** Drag coefficient of a cone ({@value}) */
    public static final double CD_CONE = 0.50;

    /** Drag coefficient of a cube ({@value}) */
    public static final double CD_CUBE = 1.05;

    /** Drag coefficient of a cube at 45 degrees ({@value}) */
    public static final double CD_CUBE_ANGLED = 0.80;

    /** Drag coefficient of a long cylinder ({@value}) */
    public static final double CD_CYLINDER_LONG = 0.82;

    /** Drag coefficient of a short cylinder ({@value}) */
    public static final double CD_CYLINDER_SHORT = 1.15;

    /** Drag coefficient of a very streamlined object ({@value}) */
    public static final double CD_STREAMLINED = 0.04;

    /** Density of the fluid */
    public final double rho;
    
    /** Drag coefficient of the object moving in the fluid */
    public final double Cd;
    
    /** Effective area (w.r.t.&nbsp;the movement of the object) */
    public final double A;

    /**
     * Compute air density from temperature.
     * 
     * Linear interpolates air density for temperatures given in the rand from 
     * -25 degrees to +35 degrees Celsius. For the table 
     * see here <a href="https://en.wikipedia.org/wiki/Density#Air">on Wikipedia</a>.
     * 
     * @param temp Temperature in degree Celsius.
     * @return Density in kg/m^3
     */
    public static double getAirDensity(double temp) {
        double[] airTemps = {-25, -20, -15, -10, -5, 0 , 5, 10, 15, 20, 25, 30, 35};
        double[] airDensity = {1.423, 1.395, 1.368, 1.342, 1.316, 1.293, 1.269, 1.247, 1.225, 1.204, 1.184, 1.164, 1.146};
        
        return new LinearInterpolator().interpolate(airTemps, airDensity).value(temp);
    }


    /**
     * Generate a DragForce model.
     * 
     * @param rho The mass density of the fluid.
     * @param Cd The drag coefficient.
     * @param A The reference area of body moving in the fluid.
     */
    public DragForceModel(double rho, double Cd, double A) {
        this.rho = rho;
        this.Cd = Cd;
        this.A = A;
    }
    
    /**
     * Return the force on some object by friction.
     * 
     * @param x The position of the object.
     * @param v The velocity of the object.
     * @return The force vector.
     */    
    @Override
    public RealVector getForce(RealVector x, RealVector v) {
        double factor = 0.5 * rho * v.getNorm() * Cd * A;
        return v.mapMultiply(-factor);
    }

    /**
     * Compute potential energy from drag force (i.e. zero).
     * 
     * Potential energy is zero, since drag force is not a conservative force.
     * 
     * @param x Position.
     * @param v Velosicty.
     * @return Zero energy.
     */
    @Override
    public double getPotentialEnergy(RealVector x, RealVector v) {
        // Frictional force is just lost, and not stored
        return 0;
    }
    
}
