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
 * A TimeStepper controls the flow of simulation time in relation to real time.
 * 
 * @author ezander
 */
public class TimeStepper {
    double tReal;
    double tSim;
    double simSpeed;
    boolean paused;

    /**
     * Construct a TimeStepper.
     */
    public TimeStepper() {
        this(false);
    }

    /**
     * Construct a TimeStepper.
     * 
     * @param paused True, if stepper should start in pause mode.
     */
    public TimeStepper(boolean paused) {
        this(0.0, 1.0, paused);
    }

    /**
     * Construct a time stepper.
     * 
     * @param tSim Current simulation time.
     * @param simSpeed Current simulation speed ratio.
     * @param paused Paused or not.
     */
    public TimeStepper(double tSim, double simSpeed, boolean paused) {
        this(getSystemTime(), tSim, simSpeed, paused);
    }

    /**
     * Construct a time stepper.
     * 
     * @param tReal Real time of last update.
     * @param tSim Simulation time corresponding to last real time tReal.
     * @param simSpeed Current simulation speed ratio.
     * @param paused Paused or not.
     */
    public TimeStepper(double tReal, double tSim, double simSpeed, boolean paused) {
        this.tReal = tReal;
        this.tSim = tSim;
        this.simSpeed = simSpeed;
        this.paused = paused;
    }
    
    
    /**
     * Get the current system time in seconds.
     * 
     * @return The system time in seconds.
     */
    static double getSystemTime() {
        return (double) System.currentTimeMillis() / 1000.0;
    }

    /**
     * Get current simulation time in seconds.
     * 
     * @return The current simulation time.
     */
    public double getSimTime() {
        step();
        return tSim;
    }

    /**
     * Set current simulation time.
     * 
     * Note: this just sets the time, but does not cause the simulation to run 
     * up to that time.
     * 
     * @param tSim The new time.
     */
    public void setSimTime(double tSim) {
        this.tSim = tSim;
    }

    /**
     * Get the relative speed.
     * 
     * The relative speed is the ratio between the flow of simulation time and 
     * the flow of real time. Higher values (higher than 1) mean the simulation 
     * runs faster than real time and vice versa.
     * 
     * @return The simulation speed ratio.
     */
    public double getSimSpeed() {
        return simSpeed;
    }

    /**
     * Set relative speed.
     * 
     * @param simSpeed The new simulation speed ratio.
     */
    public void setSimSpeed(double simSpeed) {
        this.simSpeed = simSpeed;
    }

    /**
     * Determine whether simulation is paused.
     * 
     * @return True, if paused.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Pause or resume simulation.
     * @param paused 
     */
    public void setPaused(boolean paused) {
        if (paused) {
            step();
        } else {
            tReal = getSystemTime();
        }
        this.paused = paused;
    }


    /**
     * Step forward to current time.
     */
    public void step() {
        step(getSystemTime());
    }

    /**
     * Step to given real time.
     * 
     * This updates the real time to the one passed to the method, and the 
     * simulation according to the product of the simulation speed ratio and the 
     * difference between the new and old real time (if not paused).
     * @param tRealNew 
     */
    protected void step(double tRealNew) {
        double dtReal = tRealNew - tReal;
        double dtSim = paused ? 0 : dtReal * simSpeed;
        tReal = tRealNew;
        tSim += dtSim;
    }

    /**
     * Accelerates simulation speed.
     * 
     * Multiplies the simulation speed ratio by a given factor.
     * 
     * @param factor The factor.
     */
    public void accelerate(double factor) {
        step();
        simSpeed *= factor;
    }

    /**
     * Decelerates simulation speed.
     * 
     * Divides the simulation speed ratio by a given factor.
     * 
     * @param factor The factor.
     */
    public void decelerate(double factor) {
        step();
        simSpeed /= factor;
    }

    /**
     * Reverses the simulation flow of time.
     * 
     * Note: not all simulations might like this, but want the time value only 
     * to increase.
     */
    public void reverse() {
        step();
        simSpeed -= simSpeed;
    }

    /**
     * Pause the simulation.
     */
    public void pause() {
        setPaused(true);
    }

    /**
     * Resume from paused state.
     */
    public void resume() {
        setPaused(false);
    }
    
}
