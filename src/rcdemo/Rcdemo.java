/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcdemo;

import rcdemo.track.SplineTrack;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.FirstOrderConverter;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import rcdemo.io.XMLHelper;
import rcdemo.physics.ForceModel;
import rcdemo.physics.FrictionForceModel;
import rcdemo.track.CircleTrack;
import rcdemo.track.Track;
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

class StateIntegrator {
    private static FirstOrderIntegrator defaultIntegrator = new ClassicalRungeKuttaIntegrator(0.1);
    private final FirstOrderIntegrator integrator;
    
    private final FirstOrderDifferentialEquations ode;
    private double t;
    private ArrayRealVector y;

    public StateIntegrator(FirstOrderDifferentialEquations ode, RealVector y, double t, FirstOrderIntegrator integrator) {
        this.ode = ode;
        this.integrator = integrator;
        this.y = new ArrayRealVector(y);
        this.t = t;
    }

    public StateIntegrator(SecondOrderDifferentialEquations ode2, RealVector y) {
        this(new FirstOrderConverter(ode2), y, 0, defaultIntegrator);
    }
    
    public StateIntegrator integrateTo(double t) {
        integrator.integrate(ode, this.t, y.toArray(), t, y.getDataRef());
        this.t = t;
        return this;
    }
        
    public static ArrayRealVector doIntegrate2ndOrder(SecondOrderDifferentialEquations ode2, double t0, double t1, double dt, RealVector y0) {
        //integrator = new MidpointIntegrator(0.1);
        StateIntegrator stateInt = new StateIntegrator(ode2, y0);
        stateInt.setT(t0);

        while (true) {
            double t = stateInt.getT();
            System.out.format("%4.1f: %s\n", t, stateInt.getY());
            if (t >= t1 - 1e-7) {
                break;
            }
            stateInt.integrateTo(t+dt);
        }
        return stateInt.getY();
    }

    public static FirstOrderIntegrator getDefaultIntegrator() {
        return defaultIntegrator;
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
                new FrictionForceModel());
        //new ZeroForceModel());

        ArrayRealVector y = new ArrayRealVector(new double[]{0, 1});

        StateIntegrator.doIntegrate2ndOrder(ode2, 0, 20, 2.5, y);
        System.out.println(y);
    }

    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filename = "tracks/colossos.rct";
        SimulationState state = SimulationState.readFromXML(filename);
        test1();
        test2();
    }


}
