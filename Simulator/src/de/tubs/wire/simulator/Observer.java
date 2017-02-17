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

/**
 * An observer interface for receiving notifications from simulations.
 * 
 * An Observer can be registered with a {@link Simulator} and gets 
 * {@link #notify notifications} about state changes in the simulation.
 * 
 * @author ezander
 * @param <SimulationInfo> The simulation info class.
 * @see Simulator
 */
public interface Observer<SimulationInfo> {
    /**
     * Called when the simulator is first run or reset.
     * 
     * @param simulationInfo The simulation info object.
     */
    void init(SimulationInfo simulationInfo);

    /**
     * Called when the simulation has computed a new state.
     * 
     * @param t The new simulation time.
     * @param y The new state of the simulation.
     */
    void notify(double t, double[] y);
    
}
