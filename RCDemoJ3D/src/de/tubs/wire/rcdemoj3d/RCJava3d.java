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
package de.tubs.wire.rcdemoj3d;

import de.tubs.wire.graphics.java3d.Java3dObserverSimple;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.simulator.TrackSimulator;
import de.tubs.wire.simulator.track.StockTracks;
import de.tubs.wire.keyboard.AWTKeyProcessor;
import de.tubs.wire.graphics.DefaultKeyMapping;
import de.tubs.wire.simulator.TextBasedObserver;


/**
 *
 * @author ezander
 */
public class RCJava3d {

    public static void run() {
        // Load simulation stuff
        //String filename = "../tracks/colossos.rct";
        //String filename = "../tracks/bigloop.rct";
        String filename = "../Simulator/tracks/foo.rct";
        TrackInformation trackInfo = TrackInformation.readFromXML(StockTracks.TEST);
        
        TrackSimulator sim = new TrackSimulator();
        sim.setSimulationInfo(trackInfo);
        
        Java3dObserverSimple observer3d = new Java3dObserverSimple();
        observer3d.setCamNum(-1);

        AWTKeyProcessor keyprocessor = new AWTKeyProcessor();
        DefaultKeyMapping.setDefaultKeys(keyprocessor, sim, observer3d, true);
        observer3d.setKeyProcessor(keyprocessor);
        
        sim.addObserver(observer3d);
        sim.addObserver( new TextBasedObserver());
        sim.run();
    }
    
    public static void main(String[] args) {
        run();
    }

}
