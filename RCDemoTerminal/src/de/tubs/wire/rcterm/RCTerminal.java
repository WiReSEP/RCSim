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


import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.simulator.TrackSimulator;
import de.tubs.wire.simulator.Simulator;
import de.tubs.wire.simulator.track.StockTracks;


/**
 *
 * @author ezander
 */
public class RCTerminal {

    public static void run() {
        // Load simulation stuff
        TrackInformation state = TrackInformation.readFromXML(StockTracks.TEST);
        
        TermObserver observer = new TermObserver();
        
        TrackSimulator sim = new TrackSimulator();
        sim.addObserver(observer);
        sim.setSimulationInfo(state);

        //observer.init(sim.getState());
        sim.run();
    }
    
    public static void main(String[] args) {
        run();
    }

}
