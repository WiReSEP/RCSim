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
package de.tubs.wire.simulator.math;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.FirstOrderConverter;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;

/**
 * Integrate first or second order ODEs keeping the current state.
 * 
 * A StateIntegrator object can be used to integrate 1st or 2nd order ODEs and 
 * keep track of the current state. The most important methods being integrateTo 
 * which integrates from the current time to the specified time, and getY which 
 * gets the state at the current time.
 * 
 * @author ezander
 */
public class StateIntegrator {

    private static FirstOrderIntegrator defaultIntegrator = new ClassicalRungeKuttaIntegrator(0.1);
    private final FirstOrderIntegrator integrator;
    private final FirstOrderDifferentialEquations ode;
    private double t;
    private ArrayRealVector y;
    public int evals = 0;

    /**
     * Create a StateIntegrator.
     * 
     * @param ode The first order ODEs.
     * @param y The current state.
     * @param t The current time.
     * @param integrator An integrator (can use defaultIntegrator).
     */
    public StateIntegrator(FirstOrderDifferentialEquations ode, RealVector y, double t, FirstOrderIntegrator integrator) {
        this.ode = ode;
        this.integrator = integrator;
        this.y = new ArrayRealVector(y);
        this.t = t;
    }

    /**
     * Create a StateIntegrator.
     * 
     * @param ode2 The second order ODEs (defining yDDot).
     * @param y The state (containing "position" and "velocities")
     */
    public StateIntegrator(SecondOrderDifferentialEquations ode2, RealVector y) {
        this(new FirstOrderConverter(ode2), y, 0, StateIntegrator.defaultIntegrator);
    }

    /**
     * Integrate up to new time.
     * 
     * Integrate up to time tNew, stores the state and sets the current time to 
     * tNew.
     * 
     * @param tNew The new time.
     * @return The StateIntegrator itself.
     */
    public StateIntegrator integrateTo(double tNew) {
        if (Math.abs(t - tNew) == 0) {
            return this;
        }
        
        try {
            integrator.integrate(ode, t, y.toArray(), tNew, y.getDataRef());
        } 
        catch (org.apache.commons.math3.exception.NumberIsTooSmallException ex) {
            // we can safely ignore that
        }
        evals += integrator.getEvaluations();
        t = tNew;
        return this;
    }

    /**
     * Integrate a second order ode and store all intermediate results in an 
     * array.
     * 
     * @param ode2
     * @param t0
     * @param t1
     * @param dt
     * @param y0
     * @return 
     */
    @Deprecated
    public static ArrayRealVector doIntegrate2ndOrder(SecondOrderDifferentialEquations ode2, double t0, double t1, double dt, RealVector y0) {
        StateIntegrator stateInt = new StateIntegrator(ode2, y0);
        stateInt.setT(t0);
        while (true) {
            double t = stateInt.getT();
            System.out.format("%4.1f: %s\n", t, stateInt.getY());
            if (t >= t1 - 1e-7) {
                break;
            }
            stateInt.integrateTo(t + dt);
        }
        return stateInt.getY();
    }

    /**
     * Get the default ODE integrator.
     * 
     * @return The default ODE integrator.
     */
    public static FirstOrderIntegrator getDefaultIntegrator() {
        return StateIntegrator.defaultIntegrator;
    }

    /**
     * Set the default ODE integrator.
     * 
     * @param defaultIntegrator The new default ODE integrator.
     */
    public static void setDefaultIntegrator(FirstOrderIntegrator defaultIntegrator) {
        StateIntegrator.defaultIntegrator = defaultIntegrator;
    }

    /**
     * Get current time.
     * 
     * @return The current time.
     */
    public double getT() {
        return t;
    }

    /**
     * Set current time.
     * 
     * @param t The new current time.
     */
    public void setT(double t) {
        this.t = t;
    }

    /**
     * Get current state.
     * 
     * @return The current state.
     */
    public ArrayRealVector getY() {
        return y;
    }

    /**
     * Set current state.
     * 
     * @param y The new current state.
     */
    public void setY(ArrayRealVector y) {
        this.y = y.copy();
    }

}
