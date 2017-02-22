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

import java.io.PrintStream;
import org.apache.commons.math3.linear.ArrayRealVector;

/**
 * An observer that just outputs the simulation state to the terminal.
 * 
 * @author ezander
 * @param <SimulationInfo> The simulation info class.
 */
public class TextBasedObserver<SimulationInfo> implements Observer<SimulationInfo> {

    PrintStream out = System.out;
    
    @Override
    public void init(SimulationInfo info) {
    }

    @Override
    public void notify(double t, double[] y) {
        out.format("Simumlatorstate: t=%4.2f y=%s\n", t, new ArrayRealVector(y));
        
    }

}
