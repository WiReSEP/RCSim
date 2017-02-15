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
package de.tubs.wire.ui;

import java.awt.event.KeyEvent;
import de.tubs.wire.graphics.ViewController;
import de.tubs.wire.simulator.TrackSimulator;
import de.tubs.wire.simulator.Simulator;

/**
 *
 * @author ezander
 */
public class DefaultKeyListener {

    
    
    public static <T extends KeyProcessor> T setDefaultKeys(T kp, Simulator sim, ViewController vc, boolean withQuit) {
        //kp.add(KeyEvent.VK_SHIFT, d -> sim.getStepper().pause(), d -> sim.getStepper().resume(), "Pauses the simulation.");
        kp.add(KeyEvent.VK_SPACE, d -> sim.getStepper().pause(), d -> sim.getStepper().resume(), "Pauses the simulation (while pressed).");
        kp.add('+', d -> sim.getStepper().accelerate(1.4142), "Makes the simulation run faster.");
        kp.add('-', d -> sim.getStepper().decelerate(1.4142), "Makes the simulation run slower.");
        kp.add('p', d -> sim.getStepper().pause(), "Pauses the simulation.");
        kp.add('c', d -> sim.getStepper().resume(), "Resumes the simulation (after pausing).");
        
        kp.add(KeyEvent.VK_LEFT, null, d -> vc.prevCam(), "Selects the previous camera.");
        kp.add(KeyEvent.VK_RIGHT, null, d -> vc.nextCam(), "Selects the next camera.");
        kp.add('w', d -> vc.prevCam(), "Selects the next camera.");
        kp.add('e', d -> vc.nextCam(), "Selects the previous camera.");
        
        kp.add('r', d -> ((TrackSimulator)sim).reverse(), "Reverses the direction of the coach.");
        
        kp.add('h', d -> kp.showHelp(), "Shows the help.");
        if (withQuit)
            kp.add('q', d -> System.exit(0), "Quits the simulation.");
        return kp;
    }
}
