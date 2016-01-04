/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcdemo;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.FirstOrderConverter;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.SecondOrderDifferentialEquations;
import org.apache.commons.math3.ode.nonstiff.MidpointIntegrator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import rcdemo.physics.ForceModel;
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

class XMLReader {

    void readTrack() {
        //DocumentBuilder builder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }
}

/**
 *
 * @author ezander
 */
public class Rcdemo {

    public static class FrictionForceModel implements ForceModel {

        @Override
        public RealVector getForce(RealVector x, RealVector v) {
            double rho = 1;
            double Cd = 1;
            double A = 1;
            double factor = 0.5 * rho * v.getNorm() * Cd * A;
            return v.mapMultiply(-factor);
        }
    }

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
                new FrictionForceModel());
        //new ZeroForceModel());

        ArrayRealVector y = new ArrayRealVector(new double[]{0, 1});

        doIntegrate(ode2, 0, 20, 2.5, y);
        System.out.println(y);
    }

    public static class XMLHelper {

        public static Element getChildByName(Element parent, String name) {
            return (Element) parent.getElementsByTagName(name).item(0);
        }

        public static ArrayList<Element> getChildrenByName(Element parent, String name) {
            ArrayList<Element> nodes = new ArrayList<>();
            NodeList list = parent.getElementsByTagName(name);
            for (int i = 0; i < list.getLength(); i++) {
                nodes.add((Element) list.item(i));
            }
            return nodes;
        }

        public static double getDouble(Element parent, String name) {
            Element node = XMLHelper.getChildByName(parent, name);
            String s = node.getTextContent();
            return Double.parseDouble(s);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        test1();
        DocumentBuilder builder;
        try {
            builder = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse("tracks/colossos.rct");
            Element root = doc.getDocumentElement();
            
            Element track = XMLHelper.getChildByName(root, "Track");

            Element general = XMLHelper.getChildByName(track, "General");
            Element params = XMLHelper.getChildByName(track, "SimulationParameters");
            Element pillarList = XMLHelper.getChildByName(track, "PillarList");
            
            ArrayList<Element> pillars = XMLHelper.getChildrenByName(pillarList, "Pillar");
            
            int n = pillars.size();
            double pos[][] = new double[3][n];
            for (int i = 0; i < pillars.size(); i++) {
                pos[0][i] = XMLHelper.getDouble(pillars.get(i), "PosX");
                pos[1][i] = XMLHelper.getDouble(pillars.get(i), "PosY");
                pos[2][i] = XMLHelper.getDouble(pillars.get(i), "PosZ");
            }
            RealMatrix m = MatrixUtils.createRealMatrix(pos);
            System.out.println(m.transpose());

            

        } catch (Exception foo) {
            System.out.println(foo);
            throw new RuntimeException(foo);
        }
        System.out.println(builder);
        test2();
    }

}
