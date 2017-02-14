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

import org.apache.commons.math3.linear.RealVector;

/**
 * (class should be named AirDragForceModel)
 * https://en.wikipedia.org/wiki/Drag_equation
 * https://en.wikipedia.org/wiki/Drag_coefficient
 * @author ezander
 */
public class FrictionForceModel implements ForceModel {
    public double rho = 1;
    public double Cd = 1;
    public double A = 1;

//https://en.wikipedia.org/wiki/Density#Air
//−25 	1.423 (in kg/m^3)
//−20 	1.395
//−15 	1.368
//−10 	1.342
//−5 	1.316
//0 	1.293
//5 	1.269
//10 	1.247
//15 	1.225
//20 	1.204
//25 	1.184
//30 	1.164
//35 	1.146

    
    public static double CD_SPHERE = 0.47;
    public static double CD_HALFSPHERE = 0.42;
    public static double CD_CONE = 0.50;
    public static double CD_CUBE = 1.05;
    public static double CD_CUBE_ANGLED = 0.80;
    public static double CD_CYLINDER_LONG = 0.82;
    public static double CD_CYLINDER_SHORT = 1.15;
    public static double CD_STREAMLINED = 0.04;

    public FrictionForceModel() {
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

    @Override
    public double getPotentialEnergy(RealVector x, RealVector v) {
        // Frictional force is just lost, and not stored
        return 0;
    }
    
}
