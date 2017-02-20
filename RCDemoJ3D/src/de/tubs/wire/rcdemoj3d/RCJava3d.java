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
import de.tubs.wire.graphics.java3d.Screen;
import de.tubs.wire.simulator.TextBasedObserver;
import javax.swing.SwingUtilities;


/**
 *
 * @author ezander
 */
public class RCJava3d {

    public static void run() {
        // Instantiate new simulator
        TrackSimulator sim = new TrackSimulator();

        // Load a track
        //String filename = "../tracks/colossos.rct";
        TrackInformation trackInfo = TrackInformation.readFromXML(StockTracks.TEST);
        sim.setSimulationInfo(trackInfo);
        
        // Create a Java3d observer
        Java3dObserverSimple observer3d = new Java3dObserverSimple();
        observer3d.setCamNum(-1);

        // Add keyprocessor to observer
        AWTKeyProcessor keyprocessor = new AWTKeyProcessor();
        DefaultKeyMapping.setDefaultKeys(keyprocessor, sim, observer3d, true);
        keyprocessor.add('f', d -> Screen.toggleParentFullScreen(observer3d.getCanvas()));
        observer3d.setKeyProcessor(keyprocessor);
        
        // Tell the simulator about the observer and start it...
        sim.addObserver(observer3d);
        //fsim.addObserver( new TextBasedObserver());
        sim.run();
    }
    
    public static void main(String[] args) {
        run();
    }

}
