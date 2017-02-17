/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tubs.wire.simulator.math;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author ezander
 */
public class StateIntegratorTest {
    
    public StateIntegratorTest() {
    }
    
    /**
     * Test of integrateTo method, of class StateIntegrator.
     */
    @Test @Ignore
    public void testIntegrateTo() {
        System.out.println("integrateTo");
        double tNew = 0.0;
        StateIntegrator instance = null;
        StateIntegrator expResult = null;
        StateIntegrator result = instance.integrateTo(tNew);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doIntegrate2ndOrder method, of class StateIntegrator.
     */
    @Test @Ignore
    public void testDoIntegrate2ndOrder() {
        System.out.println("doIntegrate2ndOrder");
        SecondOrderDifferentialEquations ode2 = null;
        double t0 = 0.0;
        double t1 = 0.0;
        double dt = 0.0;
        RealVector y0 = null;
        ArrayRealVector expResult = null;
        ArrayRealVector result = StateIntegrator.doIntegrate2ndOrder(ode2, t0, t1, dt, y0);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


    /**
     * Test of getT/setT method, of class StateIntegrator.
     */
    @Test
    public void testGetSetT() {
        StateIntegrator stateInt = new StateIntegrator((FirstOrderDifferentialEquations)null, new ArrayRealVector(2), 2.3, StateIntegrator.getDefaultIntegrator());
        assertEquals(2.3, stateInt.getT(), 0.0);
        stateInt.setT(3.5);
        assertEquals(3.5, stateInt.getT(), 0.0);
    }

    /**
     * Test of getY method, of class StateIntegrator.
     */
    @Test
    public void testGetSetY() {
        //double[] vals = 
        ArrayRealVector y0 = new ArrayRealVector(new double[]{1.0, 2.0});
        StateIntegrator stateInt = new StateIntegrator((FirstOrderDifferentialEquations)null, y0, 2.3, StateIntegrator.getDefaultIntegrator());

        assertArrayEquals("y0 has not changed yet", new double[]{1.0, 2.0}, stateInt.getY().getDataRef(), 1e-10);
        assertNotSame("y0 should have been copied", y0, stateInt.getY());

        ArrayRealVector y = new ArrayRealVector(new double[]{2.0, 5.0});
        stateInt.setY(y);
        assertArrayEquals("y correctly set", new double[]{2.0, 5.0}, stateInt.getY().getDataRef(), 1e-10);
        assertNotSame("y should have been copied", y, stateInt.getY());
    }
    
}
