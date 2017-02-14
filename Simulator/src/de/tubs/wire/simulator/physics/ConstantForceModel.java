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
 *
 * @author ezander
 */
public class ConstantForceModel implements ForceModel {

    private final double[] F;

    public ConstantForceModel(double[] F) {
        this.F = F;
    }

    public static ConstantForceModel createGravityForceModel(double m, double g) {
        double F[] = {0, 0, -m*g};
        return new ConstantForceModel(F);
    }
        
    @Override
    public RealVector getForce(RealVector x, RealVector v) {
        return new ArrayRealVector(F);
    }

    @Override
    public double getPotentialEnergy(RealVector x, RealVector v) {
        return -getForce(x, v).dotProduct(x);
    }
}
