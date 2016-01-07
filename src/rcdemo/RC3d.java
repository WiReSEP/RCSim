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
package rcdemo;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.nonstiff.HighamHall54Integrator;
import rcdemo.math.StateIntegrator;
import rcdemo.physics.CombinedForceModel;
import rcdemo.physics.ConstantForceModel;
import rcdemo.physics.FrictionForceModel;
import rcdemo.track.TrackODE;

/**
 *
 * @author ezander
 */
public class RC3d {
    
    static TransformGroup camera;

    public static void run() {
        run3();
    }

    public static TransformGroup transform(Node node, Vector3f vector) {
        return transform(node, vector, false);
    }

    public static TransformGroup transform(Node node, Vector3f vector, boolean modifyable) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(vector);

        TransformGroup tg = new TransformGroup();
        if (modifyable) {
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        }
        tg.setTransform(transform);
        tg.addChild(node);
        return tg;
    }

    static Vector3f trackToWorld(RealVector v) {
        return trackToWorld2(v.mapSubtract(200));
    }
    
    static Vector3f trackToWorld2(RealVector v) {
        double alpha = 0.003;
        double x[] = v.mapMultiply(alpha).toArray();
        return toVector3f(x);
    }

    static Vector3f toVector3f(double[] x) {
        return new Vector3f((float) x[0], (float) x[2], (float) x[1]);
    }
    
    
    static TransformGroup createTrack(SimulationState state) {
        TransformGroup group = new TransformGroup();
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        for (double s = 0; s < state.track.length(); s += 0.01) {
            Sphere sphere = new Sphere(0.005f);
            Vector3f vector = trackToWorld(state.track.getx(s));
            group.addChild(transform(sphere, vector));
        }
        return group;
    }
    
    static TransformGroup createCar(SimulationState state){
        Transform3D transform = new Transform3D();
        Node node = new ColorCube(0.006);
        Vector3f vector = trackToWorld(state.track.getx(0));
        return transform(node, vector, true);
    }

    static TransformGroup createGround(SimulationState state){
        Transform3D transform = new Transform3D();
        Node node = new com.sun.j3d.utils.geometry.Box(6,6,0.00001f,null);
        RealVector v = new ArrayRealVector(new double[]{0,0,-40});
        Vector3f vector = trackToWorld(v);
        //Vector3f vector = trackToWorld(state.track.getx(0));
        return transform(node, vector, true);
    }
    
    static Node createLight() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Color3f lightColorGreen = new Color3f(.1f, 1.4f, .1f); // green light
        Color3f light1Color = new Color3f(.9f, .9f, .9f); // white light
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light = new DirectionalLight(light1Color, light1Direction);
        light.setInfluencingBounds(bounds);
        
        AmbientLight ambLight = new AmbientLight(lightColorGreen);
        ambLight.setInfluencingBounds(bounds);
        TransformGroup group = new TransformGroup();
        group.addChild(light);
        group.addChild(ambLight);
        return group;
    }
    
    static void animateCarSimple(SimulationState state, TransformGroup car) {
        // Start animating the car
        for (double s = 0; s < 10*state.track.length(); s += 0.1) {
            System.out.println(s);
            Vector3f vector = trackToWorld(state.track.getx(s));            
            Transform3D transform = new Transform3D();
            transform.setTranslation(vector);
            car.setTransform(transform);

            sleep(0.01);
        }
    }

    static double getTime() {
        return (double)System.currentTimeMillis() / 1000.0;
        
    }
    static void animateCar(SimulationState state, TransformGroup car) {
        // Start animating the car
        TrackODE ode2 = new TrackODE(
                state.track,
                new CombinedForceModel()
                .add(ConstantForceModel.createGravityForceModel(1, 9.81), 100)
                .add(new FrictionForceModel(), 0 * 0.01));
        //new ZeroForceModel());

        double v0 = state.v0;
        double dxds = state.track.getDxDs(0).getNorm();
        double dsdt0 = v0 / dxds;
        
        ArrayRealVector y = new ArrayRealVector(new double[]{0, dsdt0});
        StateIntegrator.setDefaultIntegrator(new HighamHall54Integrator(1e-6, 1, 1e-8, 1e-8));
        StateIntegrator stateInt = new StateIntegrator(ode2, y);
        
        double tReal = getTime();
        while (true) {
            sleep(0.01);
            double tSim = stateInt.getT();
            double tRealNew = getTime();
            double dtReal = tRealNew - tReal;
            double dtSim = dtReal * .10;
            tReal = tRealNew;
            
            y = stateInt.getY();
            //System.out.format("--%4.1f: %s  %s\n", tSim, y, ode2.getEnergy(y.getEntry(0), y.getEntry(1)));
            stateInt.integrateTo(tSim + dtSim);

            double s = y.getEntry(0);
            Vector3f start = trackToWorld(state.track.getx(0));
            Vector3f vector = trackToWorld(state.track.getx(s));
            Transform3D transform = new Transform3D();
            transform.setTranslation(vector);
            
            
            Vector3f up = trackToWorld2(state.track.getYaw(s));
            Vector3f forward = trackToWorld2(state.track.getDxDs(s));
            Vector3f right = new Vector3f();
            right.cross(forward, up);
            up.cross(right, forward);
            
            Matrix3f rot = new Matrix3f();
            up.normalize();
            right.normalize();
            forward.normalize();
            right.scale(-1);
            rot.setColumn(0, forward);
            rot.setColumn(2, up);
            rot.setColumn(1, right);
            transform.setRotation(rot);
            car.setTransform(transform);
            
            transform = new Transform3D();
            Point3d eye = new Point3d(vector);
            Point3d target = new Point3d(vector);
            Vector3d z = new Vector3d(up);

            z.scale(0.001);
            forward.scale(0.001f);
            right.scale(1.0f);

            //eye.sub(new Vector3d(right));
            //target.add(new Vector3d(right));
            //eye.sub(new Vector3d(forward));
            target.add(new Vector3d(forward));
            eye.add(z);
            target.add(z);
            //eye = new Point3d(-2,-2,-2);
            //eye = new Point3d(2,2,2);
            //target = new Point3d(vector);
            
            //z = new Vector3d(0,0,1);
            
            transform.lookAt(eye, target, z);
            transform.invert();
            camera.setTransform(transform);
            
            
        }
        
    }

    
    static void sleep(double secs) {
        try {
            double msecs = secs * 1000;
            Thread.sleep((int)msecs);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    
    public static void run3() {
        // Load simulation stuff
        String filename = "tracks/colossos.rct";
        //String filename = "tracks/bigloop.rct";
        SimulationState state = SimulationState.readFromXML(filename);

        // Setup the branch group
        TransformGroup world = new TransformGroup();
        Transform3D transform = new Transform3D();
//        Matrix3f rotMat = new Matrix3f(
//                -1,0,0,
//                0,0,1,
//                0,1,0);
//        rotMat.transpose();
//        transform.set(rotMat);
        //world.setTransform(transform);
        
        
        TransformGroup track = createTrack(state);
        world.addChild(track);

        TransformGroup car = createCar(state);
        world.addChild(car);

        TransformGroup ground = createGround(state);
        //world.addChild(ground);

        Node light = createLight();
        world.addChild(light);

        // Create the universe and add the group of objects
        SimpleUniverse universe = new SimpleUniverse();
        universe.getViewingPlatform().setNominalViewingTransform();
        camera = universe.getViewingPlatform().getViewPlatformTransform();

        BranchGroup branchGroup = new BranchGroup();
        branchGroup.addChild(world);
        universe.addBranchGraph(branchGroup);

        
        Canvas3D canvas = universe.getCanvas();
        canvas.setDoubleBufferEnable(true);
        
        View view = canvas.getView();
        view.setBackClipDistance(1000);
        
        //animateCarSimple(state, car);
        animateCar(state, car);
    }

  
}
