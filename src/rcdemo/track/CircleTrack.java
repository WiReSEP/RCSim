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

/**
 * Implement a Track that just goes round in a circle.
 * 
 * @author ezander
 */
public class CircleTrack implements Track {
    private final double omega = Math.PI * 2;
    private final double alpha = 0.1;

    @Override
    public RealVector getx(double s) {
        double[] x = {Math.cos(omega * s), Math.sin(omega * s), 0};
        x[2] = alpha * x[0];
        return new ArrayRealVector(x);
    }

    @Override
    public RealVector getDxDs(double s) {
        double[] dxds = {-omega * Math.sin(omega * s), omega * Math.cos(omega * s), 0};
        dxds[2] = alpha * dxds[0];
        return new ArrayRealVector(dxds);
    }

    @Override
    public RealVector getDDxDss(double s) {
        double[] ddxdss = {-omega * omega * Math.cos(omega * s), -omega * omega * Math.sin(omega * s), 0};
        ddxdss[2] = alpha * ddxdss[0];
        return new ArrayRealVector(ddxdss);
    }

    @Override
    public RealVector getYaw(double s) {
        return new ArrayRealVector(3);
    }

    @Override
    public double getYawAngle(double s) {
        return 0;
    }
}
