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
package de.tubs.wire.simulator;

import org.apache.commons.math3.linear.ArrayRealVector;
import de.tubs.wire.simulator.math.StateIntegrator;
import de.tubs.wire.simulator.physics.CombinedForceModel;
import de.tubs.wire.simulator.physics.ConstantForceModel;
import de.tubs.wire.simulator.physics.ForceModel;
import de.tubs.wire.simulator.physics.FrictionForceModel;
import de.tubs.wire.simulator.track.Track;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.simulator.track.TrackODE;

/**
 * A simulator 
 * @author ezander
 */
public class TrackSimulator extends ODESimulator<TrackInformation> {

    @Override
    protected void reset() {
        // Get the track
        TrackInformation trackInfo = getSimulationInfo();
        Track track = trackInfo.getTrack();
        
        // Set the force model
        ForceModel gravity = ConstantForceModel.createGravityForceModel(1, 9.81);
        ForceModel friction = new FrictionForceModel();
        ForceModel combinedForce = new CombinedForceModel().add(gravity, 1).add(friction, 0 * 0.01);
        TrackODE ode2 = new TrackODE(track, combinedForce);
        
        // Compute initial velocity (in terms of curve parameter s)
        double v0 = trackInfo.getV0();
        double dxds = track.getDxDs(0).getNorm();
        double dsdt0 = v0 / dxds;
        
        // Set initial values for the ODE simulationInfo (s0, dots0)
        ArrayRealVector y = new ArrayRealVector(new double[]{0, dsdt0});
        stateInt = new StateIntegrator(ode2, y);
    }

    
}
