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
package de.tubs.wire.simulator.track;

import de.tubs.wire.simulator.math.Spline;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

/**
 * Implements a track build from splines.
 * 
 * @author ezander
 */
public class SplineTrack implements Track {
    Spline posX;
    Spline posY;
    Spline posZ;
    
    Spline yawX;
    Spline yawY;
    Spline yawZ;
    
    Spline yawAngle;

    /**
     * Create a spline track.
     * 
     * @param posX Spline for x coords.
     * @param posY Spline for y coords.
     * @param posZ Spline for z coords.
     * @param yawX Spline for yaw x coords.
     * @param yawY Spline for yaw y coords.
     * @param yawZ Spline for yaw z coords.
     * @param yawAngle  Spline for yaw angles.
     */
    public SplineTrack(Spline posX, Spline posY, Spline posZ, Spline yawX, Spline yawY, Spline yawZ, Spline yawAngle) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yawX = yawX;
        this.yawY = yawY;
        this.yawZ = yawZ;
        this.yawAngle = yawAngle;
    }
        
    /**
     * Get period of spline track.
     * 
     * The period of a closed spline track is just its number of nodes.
     * 
     * @return The period.
     */
    @Override
    public double getPeriod() {
        return posX.length();
    }
    
    /**
     * Compute position or derivative.
     * 
     * @param s The curve parameter.
     * @param deriv The derivative order.
     * @return The value of the position or the derivative.
     */
    protected RealVector getPosAt(double s, int deriv) {
        double pos[] = {posX.compute(s, deriv), posY.compute(s, deriv), posZ.compute(s, deriv)};
        return new ArrayRealVector(pos);
    }

    @Override
    public RealVector getx(double s) {
        return getPosAt(s, 0);
    }

    @Override
    public RealVector getDxDs(double s) {
        return getPosAt(s, 1);
    }

    @Override
    public RealVector getDDxDss(double s) {
        return getPosAt(s, 2);
    }

    @Override
    public RealVector getYaw(double s) {
        double yaw[] = {yawX.compute(s, 0), yawY.compute(s, 0), yawZ.compute(s, 0)};
        return new ArrayRealVector(yaw);
    }

    @Override
    public double getYawAngle(double s) {
        return yawAngle.compute(s, 0);
    }
}
