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

import java.util.LinkedList;
import java.util.List;

/**
 * Base class for simulators.
 * 
 * Contains a simulationInfo, a time stepper which dictates the flow of time (i.e. 
 * simulation time versus real time, and a list of observers, which get notified 
 * of changes in the simulation.
 * 
 * Derived classes must implement the methods 'reset' and 'stepTo', which reset 
 * the simulation state or continue the simulation up to a given time (given in 
 * simulation time).
 * 
 * Clients of this class usually call 'addObserver' in order to be notified of 
 * changes in the simulation state. 
 * 
 * @author ezander
 * @param <SimulationInfo> Class containing information essential to the specific simulation.
 */
public abstract class Simulator<SimulationInfo> {

    List<Observer<SimulationInfo>> observers = new LinkedList<>();
    TimeStepper stepper = new TimeStepper();
    SimulationInfo simulationInfo;

    /**
     * Get current simulation info.
     * @return The current simulation info.
     */
    public SimulationInfo getSimulationInfo() {
        return simulationInfo;
    }

    /**
     * Set a new simulation simulationInfo.
     * 
     * @param simulationInfo The info struct to be set as new simulation info.
     */
    public void setSimulationInfo(SimulationInfo simulationInfo) {
        this.simulationInfo = simulationInfo;
    }

    /**
     * Get the time stepper.
     * 
     * @return The time stepper.
     */
    public TimeStepper getStepper() {
        return stepper;
    }

    /**
     * Add an observer to this simulator.
     * 
     * @param observer A new observer.
     */
    public void addObserver(Observer<SimulationInfo> observer) {
        observers.add(observer);
    }

    /** 
     * Notifies the observers if the simulation state has changed. 
     * 
     * Usually called by update, no need to call it yourself.
     * 
     * @param t The current time (simulation time).
     * @param y The current state.
     */
    protected void notifyObservers(double t, double[] y) {
        for (Observer<SimulationInfo> observer : observers) {
            observer.notify(t, y);
        }
    }

    /**
     * Let the simulation sleep/wait for a while.
     * 
     * @param secs The time to sleep (real time).
     */
    public static void sleep(double secs) {
        try {
            double msecs = secs * 1000;
            Thread.sleep((int) msecs);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Reset the simulation to the initial state.
     * 
     * Needs to be implemented in derived classes.
     */
    protected abstract void reset();

    /**
     * Let the simulation go forward in time.
     * 
     * This needs to be implemented in derived classes (e.g. by some ODE 
     * integration scheme).
     * 
     * @param simTime The new simulation time up to which needs to be simulated.
     * @return The current state of the simulation.
     */
    protected abstract double[] stepTo(double simTime);

    /**
     * Update the simulation.
     * 
     * Gets the current time from the time stepper, lets the derived simulation 
     * class compute the new state and inform all observers about the new state.
     */
    public final void update() {
        double t = stepper.getSimTime();
        double[] y = stepTo(t);
        notifyObservers(t, y);
    }

    /**
     * Initialise the simulation.
     * 
     * Initialises the time stepper, calls reset on the simulation and informs 
     * the observers.
     */
    public final void init() {
        stepper = new TimeStepper();
        reset();
        for (Observer<SimulationInfo> observer : observers) {
            observer.init(simulationInfo);
        }
    }
    
    /**
     * Runs the simulator in an endless loop.
     * 
     * For simple applications, just runs the simulator in an endless loop, 
     * waiting 0.01 second before each update.
     */
    public final void run() {
        init();
        while (true) {
            update();
            sleep(0.01);
        }
    }
    
}
