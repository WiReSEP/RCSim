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
package rcdemo.track;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import rcdemo.math.ClosedHermiteSpline;
import rcdemo.math.Spline;
import rcdemo.track.Track;

/**
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

    public SplineTrack(Spline posX, Spline posY, Spline posZ, Spline yawX, Spline yawY, Spline yawZ, Spline yawAngle) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.yawX = yawX;
        this.yawY = yawY;
        this.yawZ = yawZ;
        this.yawAngle = yawAngle;
    }
        
    @Override
    public double length() {
        return posX.length();
    }
    
    public RealVector getPosAt(double s, int deriv) {
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
