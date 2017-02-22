package de.tubs.wire.simulator.track;

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
import de.tubs.wire.simulator.math.VectorACMath;
import de.tubs.wire.simulator.track.Track;
import de.tubs.wire.simulator.track.TrackHelper;
import org.apache.commons.math3.linear.ArrayRealVector;

/**
 *
 * @author ezander
 */
public class TerminalTrackObserver extends TrackObserver {

    protected double delay = 0.0;
    
    public TerminalTrackObserver() {
        this(0.0);
    }
    
    public TerminalTrackObserver(double delay) {
        this.delay = delay;
    }
    
    @Override
    public void notify(double t, double[] y) {
        double s = y[0];
        double dsdt = y[1];
        
        TrackHelper<ArrayRealVector> helper = new TrackHelper<>(new VectorACMath());
        Track track = trackInfo.getTrack();
        
        System.out.format("Sim-State: t=%4.2f s=%5.2f s'=%4.2f\n", t, s % track.getPeriod(), dsdt);
        ArrayRealVector pos = helper.getPosition(track, s);
        ArrayRealVector velocity = helper.getVelocity(track, s, dsdt);
        double speed = helper.vecmath.norm(velocity);
        System.out.format("  Position: %s\n", pos);
        System.out.format("  Velocity: %s\n", velocity);
        System.out.format("  Speed:    %s\n", speed);
        
        // Sleep for a while as the simulation is fast (better choose a larger delay later) 
        if(delay>0){
            Simulator.sleep(delay);
        }
    }

}
