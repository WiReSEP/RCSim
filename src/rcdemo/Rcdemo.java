/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcdemo;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator;
import org.apache.commons.math3.ode.nonstiff.AdamsMoultonIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince54Integrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.nonstiff.GraggBulirschStoerIntegrator;
import org.apache.commons.math3.ode.nonstiff.HighamHall54Integrator;
import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;
import rcdemo.math.StateIntegrator;
import rcdemo.physics.CombinedForceModel;
import rcdemo.physics.ConstantForceModel;
import rcdemo.physics.FrictionForceModel;
import rcdemo.physics.ZeroForceModel;
import rcdemo.track.CircleTrack;
import rcdemo.track.TrackODE;

class BallODE implements SecondOrderDifferentialEquations {
    @Override
    public int getDimension() {
        return 1;
    }
    @Override
    public void computeSecondDerivatives(double t, double[] y, double[] yDot, double[] yDDot) {
        yDDot[0] = -10;
    }
}


/**
 *
 * @author ezander
 */
public class Rcdemo {

    static void test1() {
        System.out.println("Foobar1");
        SecondOrderDifferentialEquations ode2 = new BallODE();
        ArrayRealVector y = new ArrayRealVector(new double[]{0, 5});

        StateIntegrator.doIntegrate2ndOrder(ode2, 0, 2, 0.5, y);
        System.out.println(y);
    }

    static void test2() {
        System.out.println("Foobar2");
        SecondOrderDifferentialEquations ode2 = new TrackODE(
                new CircleTrack(),
                //new FrictionForceModel());
        new ZeroForceModel());

        ArrayRealVector y = new ArrayRealVector(new double[]{0, 1});

        StateIntegrator.doIntegrate2ndOrder(ode2, 0, 20, 2.5, y);
        System.out.println(y);
    }

    static void test3() {
        String filename = "tracks/colossos.rct";
        SimulationState state = SimulationState.readFromXML(filename);
        TrackODE ode2 = new TrackODE(
                state.getTrack(),
                new CombinedForceModel()
                        .add(ConstantForceModel.createGravityForceModel(1, 9.81), 1)
                .add(new FrictionForceModel(), 0.01));
                //new ZeroForceModel());

        ArrayRealVector y = new ArrayRealVector(new double[]{0, 1});

        StateIntegrator.setDefaultIntegrator(new HighamHall54Integrator(1e-6, 1, 1e-8, 1e-8));
        StateIntegrator stateInt = new StateIntegrator(ode2, y);
        double t1 = 200, dt = 0.5;
        while (true) {
            double t = stateInt.getT();
            y = stateInt.getY();
            
            System.out.format("--%4.1f: %s  %s\n", t, y, ode2.getEnergy(y.getEntry(0), y.getEntry(1)));
            if (t >= t1 - 1e-7) {
                break;
            }
            stateInt.integrateTo(t + dt);
        }
        System.out.println(stateInt.evals);
    }
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //test1();
        //test2();
        //test3();
        RC3d.run();
        //RCGui.main(new String []{});
    }


}
