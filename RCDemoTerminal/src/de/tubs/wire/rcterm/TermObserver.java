package de.tubs.wire.rcterm;

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


import de.tubs.wire.simulator.Observer;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.simulator.Simulator;

/**
 *
 * @author ezander
 */
public class TermObserver implements Observer<TrackInformation> {

    @Override
    public void init(TrackInformation trackInfo) {
    }
    
    public TermObserver() {
    }

    @Override
    public void notify(double t, double[] y) {
        // TODO: Send t (simulation time), s, and dsdt
        double s = y[0];
        double dsdt = y[1];
        System.out.format("Sim-State: %s %s %s\n", t, s, dsdt);

        // TODO: Send pos, speed and velocity
        //Vector3d pos = helper.getPosition(track, s);
        //Vector3d speed = helper.getSpeed(track, s, dsdt);
        //double velocity = speed.length();
        //System.out.format("Position: %s\n", pos);
        //System.out.format("Speed: %s\n", speed);
        //System.out.format("Velocity: %s\n", velocity);
        
        // Sleep for a while as the simulation is fast (better choose a larger delay later) 
        Simulator.sleep(0.5);
    }

}
