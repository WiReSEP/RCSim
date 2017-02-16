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

import de.tubs.wire.simulator.physics.ForceModel;
import de.tubs.wire.simulator.physics.ZeroForceModel;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;

/**
 * Implements an ODE model on a track.
 * 
 * This implements a second order ordinary differential equation for a coach on 
 * a Track, which can be used in an ODESimulator. The second derivatives are 
 * computed by combining a given ForceModel with the constraints that the coach 
 * stays on the track using Newton's laws.
 * 
 * @author ezander
 * @see de.tubs.wire.simulator.ODESimulator
 * @see de.tubs.wire.simulator.physics.ForceModel
 */
public class TrackODE implements SecondOrderDifferentialEquations {
    private final Track track;
    private final ForceModel forceModel;

    /**
     * Creates a new TrackODE with a ZeroForceModel.
     * 
     * @param track The track.
     */
    public TrackODE(Track track) {
        this.track = track;
        this.forceModel = new ZeroForceModel();
    }

    /**
     * Create a new TrackODE with a given ForceModel.
     * @param track
     * @param forceModel 
     */
    public TrackODE(Track track, ForceModel forceModel) {
        this.track = track;
        this.forceModel = forceModel;
    }

    /**
     * The dimension of the parameter space of this ODE.
     * Since we have only one free parameter in this ODE, namely the curve 
     * parameter, the dimension is one (And not three).
     * 
     * @return 1 (the dimension of the ODE).
     */
    @Override
    public int getDimension() {
        return 1;
    }

    /**
     * Compute the second derivative.
     * 
     * Compute yDDot given t, y, and yDot.
     * 
     * @param t The time paramter.
     * @param y
     * @param yDot
     * @param yDDot 
     */
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

        //double E = 0.5 * v.dotProduct(v) + forceModel.getPotentialEnergy(x, v);
        //System.out.format("%4.1f: %s %s %s\n", t, s, x, E);
        //System.out.format("%7.5f: %s %s %s\n", t, s, x, E);

        yDDot[0] = ddsdtt;
    }
    
    /**
     * Compute the energy of the current state.
     * 
     * Used for reference and checking.
     * 
     * @param s
     * @param dsdt
     * @return 
     */
    public double getEnergy(double s, double dsdt) {
        RealVector x = track.getx(s);
        RealVector dxds = track.getDxDs(s);
        RealVector v = dxds.mapMultiply(dsdt);

        double E = 0.5 * v.dotProduct(v) + forceModel.getPotentialEnergy(x, v);
        return E;
    }
}
