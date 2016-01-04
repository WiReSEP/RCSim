/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcdemo;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.FirstOrderConverter;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.ODEIntegrator;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;
import org.apache.commons.math3.ode.nonstiff.RungeKuttaIntegrator;

class RollerCoasterODE implements FirstOrderDifferentialEquations {

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot)
            throws MaxCountExceededException, DimensionMismatchException {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

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

interface Track {

    RealVector getx(double s);

    RealVector getDxDs(double s);

    RealVector getDDxDss(double s);
}

class TrackHelper {
    static double speedToRate(Track track, double s, double v) {
    }
    static double rateToSpeed(Track track, double s, double v) {
    }
}

class CircleTrack implements Track {

    double omega = Math.PI * 2;

    @Override
    public RealVector getx(double s) {
        double x[] = {Math.cos(omega * s), Math.sin(omega * s), 0};
        return new ArrayRealVector(x);
    }

    @Override
    public RealVector getDxDs(double s) {
        double dxds[] = {-omega * Math.sin(omega * s), omega * Math.cos(omega * s), 0};
        return new ArrayRealVector(dxds);
    }

    @Override
    public RealVector getDDxDss(double s) {
        double dxds[] = {-omega * omega * Math.cos(omega * s), -omega * omega * Math.sin(omega * s), 0};
        return new ArrayRealVector(dxds);
    }
}

interface ForceModel {

    RealVector getForce(RealVector x, RealVector v);
}

class ZeroForceModel implements ForceModel {

    @Override
    public RealVector getForce(RealVector x, RealVector v) {
        return new ArrayRealVector(x.getDimension());
    }
}

class GravityForceModel implements ForceModel {

    double[] g = {0, 0, -9.81};

    public GravityForceModel() {
    }

    public GravityForceModel(double g) {
        this.g[2] = g;
    }

    @Override
    public RealVector getForce(RealVector x, RealVector v) {
        return new ArrayRealVector(g);
    }
}

class TrackODE implements SecondOrderDifferentialEquations {

    Track track;
    ForceModel forceModel;

    public TrackODE(Track track) {
        this.track = track;
        this.forceModel = new ZeroForceModel();
    }

    public TrackODE(Track track, ForceModel forceModel) {
        this.track = track;
        this.forceModel = forceModel;
    }

    @Override
    public int getDimension() {
        return 1;
    }

    @Override
    public void computeSecondDerivatives(double t, double[] y, double[] yDot, double[] yDDot) {
        double s = y[0];
        double dsdt = yDot[0];

        RealVector x = track.getx(s);
        RealVector dxds = track.getDxDs(s);
        RealVector ddxdss = track.getDDxDss(s);

        RealVector u = dxds;
        RealVector v = dxds.mapMultiply(dsdt);

        RealVector F = forceModel.getForce(x, v);

        double ddsdtt
                = F.subtract(ddxdss.mapMultiply(dsdt * dsdt)).dotProduct(u)
                / dxds.dotProduct(u);

        yDDot[0] = ddsdtt;
    }

}

/**
 *
 * @author ezander
 */
public class Rcdemo {

    private static ArrayRealVector doIntegrate(SecondOrderDifferentialEquations ode2, double t0, double t1, double dt, RealVector y0) {
        FirstOrderConverter ode = new FirstOrderConverter(ode2);
        FirstOrderIntegrator integrator;
        integrator = new MidpointIntegrator(0.1);
        ArrayRealVector y = new ArrayRealVector(y0);

        double t = t0;
        while (true) {
            System.out.format("%4.1f: %s\n", t, y);
            if (t >= t1 - 1e-7) {
                break;
            }
            integrator.integrate(ode, t, y.toArray(), t + dt, y.getDataRef());
            t += dt;
        }
        return y;
    }

    static void test1() {
        System.out.println("Foobar1");
        SecondOrderDifferentialEquations ode2 = new BallODE();
        ArrayRealVector y = new ArrayRealVector(new double[]{0, 5});

        doIntegrate(ode2, 0, 2, 0.5, y);
        System.out.println(y);
    }

    static void test2() {
        System.out.println("Foobar2");
        SecondOrderDifferentialEquations ode2 = new TrackODE(
                new CircleTrack(), 
                new ZeroForceModel());
        
        ArrayRealVector y = new ArrayRealVector(new double[]{0, 1});

        doIntegrate(ode2, 0, 2, 0.5, y);
        System.out.println(y);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        test1();
        test2();
    }

}
