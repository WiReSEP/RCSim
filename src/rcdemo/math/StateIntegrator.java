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
package rcdemo.math;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.FirstOrderConverter;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;

/**
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

    public StateIntegrator(FirstOrderDifferentialEquations ode, RealVector y, double t, FirstOrderIntegrator integrator) {
        this.ode = ode;
        this.integrator = integrator;
        this.y = new ArrayRealVector(y);
        this.t = t;
    }

    public StateIntegrator(SecondOrderDifferentialEquations ode2, RealVector y) {
        this(new FirstOrderConverter(ode2), y, 0, StateIntegrator.defaultIntegrator);
    }

    public StateIntegrator integrateTo(double t) {
        integrator.integrate(ode, this.t, y.toArray(), t, y.getDataRef());
        this.t = t;
        this.evals += integrator.getEvaluations();
        return this;
    }

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

    public static FirstOrderIntegrator getDefaultIntegrator() {
        return StateIntegrator.defaultIntegrator;
    }

    public static void setDefaultIntegrator(FirstOrderIntegrator defaultIntegrator) {
        StateIntegrator.defaultIntegrator = defaultIntegrator;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public ArrayRealVector getY() {
        return y;
    }

    public void setY(ArrayRealVector y) {
        this.y = y.copy();
    }
    
}
