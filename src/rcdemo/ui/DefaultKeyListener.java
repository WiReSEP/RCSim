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
package rcdemo.ui;

import java.awt.event.KeyEvent;
import rcdemo.graphics.ViewController;
import rcdemo.simulator.ODESimulator;
import rcdemo.simulator.Simulator;

/**
 *
 * @author ezander
 */
public class DefaultKeyListener {

    public static KeyProcessor getDefaultKeyListener(Simulator sim, ViewController vc) {
        return getDefaultKeyListener(sim, vc, false);
    }
    
    public static KeyProcessor getDefaultKeyListener(Simulator sim, ViewController vc, boolean withQuit) {
        KeyProcessor kp = new KeyProcessor();
        kp.add(KeyEvent.VK_SHIFT, d -> sim.getStepper().pause(), d -> sim.getStepper().resume());
        kp.add(KeyEvent.VK_SPACE, d -> sim.getStepper().pause(), d -> sim.getStepper().resume());
        kp.add('+', d -> sim.getStepper().accelerate(1.4142));
        kp.add('-', d -> sim.getStepper().decelerate(1.4142));
        kp.add('p', d -> sim.getStepper().pause());
        kp.add('c', d -> sim.getStepper().resume());
        kp.add(KeyEvent.VK_LEFT, null, d -> vc.prevCam());
        kp.add(KeyEvent.VK_RIGHT, null, d -> vc.nextCam());
        kp.add('r', d -> ((ODESimulator)sim).reverse());
        if (withQuit)
            kp.add('q', d -> System.exit(0));
        return kp;
    }
}
