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
package rcdemo.simulator;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author ezander
 */
public abstract class Simulator {
    List<Observer> observers = new LinkedList<>();
    TimeStepper stepper = new TimeStepper();
    SimulationState state;

    public SimulationState getState() {
        return state;
    }

    public void setState(SimulationState state) {
        this.state = state;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers(double t, double[] y) {
        for (Observer observer : observers) {
            observer.notify(t, y);
        }
    }

    static void sleep(double secs) {
        try {
            double msecs = secs * 1000;
            Thread.sleep((int) msecs);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    abstract void init();

    abstract double[] stepTo(double simTime);

    public final void update() {
        double t = stepper.getSimTime();
        double[] y = stepTo(t);
        notifyObservers(t, y);
    }

    public final void run() {
        init();
        for (Observer observer : observers) {
            observer.init(state);
        }
        while (true) {
            update();
            sleep(0.01);
        }
    }
    
}
