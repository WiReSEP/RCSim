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
import rcdemo.graphics.java3d.Java3dObserverSimple;
import rcdemo.simulator.ODESimulator;
import rcdemo.simulator.Simulator;
import rcdemo.ui.DefaultKeyListener;


/**
 *
 * @author ezander
 */
public class RCTeam {

    public static void run() {
        // Load simulation stuff
        //String filename = "tracks/colossos.rct";
        //String filename = "tracks/bigloop.rct";
        String filename = "tracks/foo.rct";
        SimulationState state = SimulationState.readFromXML(filename);
        
        TeamObserver observer = new TeamObserver();
        observer.setCamNum(-2);
        
        Simulator sim = new ODESimulator();
        sim.addObserver(observer);
        sim.setState(state);

        //observer.init(sim.getState());
        sim.run();
    }
    
    public static void main(String[] args) {
        run();
    }

}
