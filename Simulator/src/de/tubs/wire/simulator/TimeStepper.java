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
 *
 * @author ezander
 */
public class TimeStepper {
    double tReal;
    double tSim;
    double simSpeed;
    boolean paused;

    static double getSystemTime() {
        return (double) System.currentTimeMillis() / 1000.0;
    }

    public double getSimTime() {
        step();
        return tSim;
    }

    public void setSimTime(double tSim) {
        this.tSim = tSim;
    }

    public double getSimSpeed() {
        return simSpeed;
    }

    public void setSimSpeed(double simSpeed) {
        this.simSpeed = simSpeed;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        if (paused) {
            step();
        } else {
            tReal = getSystemTime();
        }
        this.paused = paused;
    }

    public TimeStepper() {
        this(false);
    }

    public TimeStepper(boolean paused) {
        this(0.0, 1.0, paused);
    }

    public TimeStepper(double tSim, double simSpeed, boolean paused) {
        this(getSystemTime(), tSim, simSpeed, paused);
    }

    public TimeStepper(double tReal, double tSim, double simSpeed, boolean paused) {
        this.tReal = tReal;
        this.tSim = tSim;
        this.simSpeed = simSpeed;
        this.paused = paused;
    }

    public void step() {
        step(getSystemTime());
    }

    protected void step(double tRealNew) {
        double dtReal = tRealNew - tReal;
        double dtSim = paused ? 0 : dtReal * simSpeed;
        tReal = tRealNew;
        tSim += dtSim;
    }

    public void accelerate(double factor) {
        step();
        simSpeed *= factor;
    }

    public void decelerate(double factor) {
        step();
        simSpeed /= factor;
    }

    public void reverse() {
        step();
        simSpeed -= simSpeed;
    }

    public void pause() {
        setPaused(true);
    }

    public void resume() {
        setPaused(false);
    }
    
}
