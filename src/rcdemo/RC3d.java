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
package rcdemo;

import rcdemo.simulator.SimulationState;
import rcdemo.graphics.Java3dObserverSimple;
import rcdemo.simulator.ODESimulator;
import rcdemo.simulator.Simulator;
import rcdemo.simulator.TextBasedObserver;
import rcdemo.ui.DefaultKeyListener;
import rcdemo.ui.KeyProcessor;


/**
 *
 * @author ezander
 */
public class RC3d {

    public static void run() {
        // Load simulation stuff
        String filename = "tracks/colossos.rct";
        //String filename = "tracks/bigloop.rct";
        SimulationState state = SimulationState.readFromXML(filename);
        
        Java3dObserverSimple observer3d = new Java3dObserverSimple();
        observer3d.setCamNum(-1);
        
        Simulator sim = new ODESimulator();
        sim.addObserver(observer3d);
        sim.addObserver( new TextBasedObserver());
        sim.setState(state);

        observer3d.init(sim.getState());
        observer3d.getCanvas().addKeyListener(DefaultKeyListener.getDefaultKeyListener(sim, observer3d));
        sim.run();
    }

}
