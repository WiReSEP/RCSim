/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tubs.wire.simulator;

import de.tubs.wire.simulator.math.StateIntegrator;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.ode.nonstiff.HighamHall54Integrator;

/**
 * A Simulator class that works with ODEs.
 * 
 * @author ezander
 * @param <SimulationInfo> The simulation info class.
 */
public abstract class ODESimulator<SimulationInfo> extends Simulator<SimulationInfo> {
    
    protected StateIntegrator stateInt;

    @Override
    protected double[] stepTo(double simTime) {
        assert stateInt != null : "Need to set stateIntegrator before calling stepTo()";
        stateInt.integrateTo(simTime);
        return stateInt.getY().toArray();
    }
    
    @Override
    protected void reset() {
        StateIntegrator.setDefaultIntegrator(new HighamHall54Integrator(1e-6, 1, 1e-8, 1e-8));
    }
    
    public void reverse() {
        assert stateInt != null : "Need to set stateIntegrator before calling reverse()";
        ArrayRealVector y = stateInt.getY();
        y.setEntry(1, -y.getEntry(1));
        stateInt.setY(y);
    }
    
}
