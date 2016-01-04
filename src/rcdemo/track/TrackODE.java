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

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;
import rcdemo.physics.ForceModel;
import rcdemo.physics.ZeroForceModel;

/**
 *
 * @author ezander
 */
public class TrackODE implements SecondOrderDifferentialEquations {
    Track track;
    ForceModel forceModel;

    public TrackODE(Track track) {
        this.track = track;
        this.forceModel = new ZeroForceModel();
    }

    public TrackODE(Track track, ForceModel forceModel) {
        this.track = track;
        this.forceModel = forceModel;
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeSecondDerivatives(double t, double[] y, double[] yDot, double[] yDDot) {
        double s = y[0];
        double dsdt = yDot[0];
        RealVector x = track.getx(s);
        RealVector dxds = track.getDxDs(s);
        RealVector ddxdss = track.getDDxDss(s);
        RealVector u = dxds;
        RealVector v = dxds.mapMultiply(dsdt);
        RealVector F = forceModel.getForce(x, v);
        double ddsdtt = F.subtract(ddxdss.mapMultiply(dsdt * dsdt)).dotProduct(u) / dxds.dotProduct(u);
        yDDot[0] = ddsdtt;
    }
    
}
