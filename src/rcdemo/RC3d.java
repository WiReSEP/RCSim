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
import java.awt.Container;
import java.awt.Dimension;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.SwingUtilities;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.ode.nonstiff.HighamHall54Integrator;
import rcdemo.graphics.Camera;
import rcdemo.graphics.CoachCamera;
import rcdemo.graphics.StillCamera;
import rcdemo.graphics.TrackHelper;
import rcdemo.graphics.TrackingCamera;
import rcdemo.math.StateIntegrator;
import rcdemo.physics.CombinedForceModel;
import rcdemo.physics.ConstantForceModel;
import rcdemo.physics.FrictionForceModel;
import rcdemo.track.Track;
import rcdemo.track.TrackODE;

class CameraFactory {

    enum CameraType {

        TRACKING_FROM_ABOVE,
        TRACKING_FROM_CENTER,
        TRACKING_FROM_BELOW,
        STILL_FROM_ABOVE,
        STILL_FROM_STRAIGHT_ABOVE,
        STILL_FROM_GROUND,
        INSIDE_COACH,
        LEFT_OF_COACH,
        RIGHT_OF_COACH,
        BEHIND_COACH,
    }

    static Camera buildCamera(CameraType type) {
        switch (type) {
            case INSIDE_COACH:
                return new CoachCamera(CoachCamera.Position.INSIDE);
            case LEFT_OF_COACH:
                return new CoachCamera(CoachCamera.Position.LEFT);
            case RIGHT_OF_COACH:
                return new CoachCamera(CoachCamera.Position.RIGHT);
            case BEHIND_COACH:
                return new CoachCamera(CoachCamera.Position.BEHIND);

            case TRACKING_FROM_ABOVE:
                return new TrackingCamera(TrackingCamera.Position.MAX);
            case TRACKING_FROM_BELOW:
                return new TrackingCamera(TrackingCamera.Position.MIN);
            case TRACKING_FROM_CENTER:
                return new TrackingCamera(TrackingCamera.Position.MEAN);

            case STILL_FROM_ABOVE:
                return new StillCamera(StillCamera.Position.MAX);
            case STILL_FROM_GROUND:
                return new StillCamera(StillCamera.Position.MIN);
            case STILL_FROM_STRAIGHT_ABOVE:
                return new StillCamera(StillCamera.Position.MEAN);

            default:
                throw new RuntimeException("unknown camera type");
        }
    }
}

class WorldCreator extends TrackHelper {

    TransformGroup createTrack(SimulationState state) {
        TransformGroup group = new TransformGroup();
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        for (double s = 0; s < state.track.length(); s += 0.01) {
            Sphere sphere = new Sphere(0.5f);
            Vector3d vector = trackToWorld(state.track.getx(s));
            group.addChild(transform(sphere, vector));
        }
        return group;
    }

    static TransformGroup createCar(SimulationState state) {
        Transform3D transform = new Transform3D();
        Node node = new ColorCube(0.7);
        Vector3d vector = trackToWorld(state.track.getx(0));
        return transform(node, vector, true);
    }

    static TransformGroup createGround(SimulationState state) {
        Transform3D transform = new Transform3D();
        Node node = new com.sun.j3d.utils.geometry.Box(1000, 1000, 0.00001f, null);
        RealVector v = new ArrayRealVector(new double[]{0, 0, -40});
        Vector3d vector = trackToWorld(v);
        return transform(node, vector, true);
    }

    static Node createLight() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
        Color3f lightColorGreen = new Color3f(.1f, 1.4f, .1f); // green light
        Color3f light1Color = new Color3f(.9f, .9f, .9f); // white light
        Vector3d light1Direction = new Vector3d(4.0, -7.0, -12.0);
        DirectionalLight light = new DirectionalLight(light1Color, new Vector3f(light1Direction));
        light.setInfluencingBounds(bounds);

        AmbientLight ambLight = new AmbientLight(lightColorGreen);
        ambLight.setInfluencingBounds(bounds);
        TransformGroup group = new TransformGroup();
        group.addChild(light);
        group.addChild(ambLight);
        return group;
    }
}

/**
 *
 * @author ezander
 */
public class RC3d {

    static TransformGroup camera;

    public static void run() {
        run3();
    }

    static void animateCarSimple(SimulationState state, TransformGroup car) {
        // Start animating the car
        for (double s = 0; s < 10 * state.track.length(); s += 0.1) {
            System.out.println(s);
            Vector3d vector = TrackHelper.getPosition(state.track, s);
            Transform3D transform = new Transform3D();
            transform.setTranslation(vector);
            car.setTransform(transform);

            sleep(0.01);
        }
    }

    static double getTime() {
        return (double) System.currentTimeMillis() / 1000.0;

    }

    static void animateCar(SimulationState state, TransformGroup car) {
        Camera camTransform;
        camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.TRACKING_FROM_ABOVE);
        camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.TRACKING_FROM_CENTER);
        camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.TRACKING_FROM_BELOW);

        camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.STILL_FROM_ABOVE);
        camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.STILL_FROM_GROUND);
        camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.STILL_FROM_STRAIGHT_ABOVE);

        camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.INSIDE_COACH);
        camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.LEFT_OF_COACH);
        camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.RIGHT_OF_COACH);
        camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.BEHIND_COACH);

        camTransform.init(state.track);

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
            Vector3d startPos = TrackHelper.getPosition(state.track, 0);
            Vector3d currentPos = TrackHelper.getPosition(state.track, s);
            Transform3D transform = new Transform3D();
            transform.setTranslation(currentPos);

            Matrix3d rot = new Matrix3d();
            Vector3d rhs[] = TrackHelper.getRHS(state.track, s);
            rot.setColumn(0, rhs[0]);
            rot.setColumn(1, rhs[2]);
            rot.setColumn(2, rhs[1]);
            transform.setRotation(rot);
            car.setTransform(transform);

            transform = camTransform.getTransform(state.track, s, dsdt0);
            transform.invert();
            camera.setTransform(transform);
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

    public static void run3() {
        // Load simulation stuff
        //String filename = "tracks/colossos.rct";
        String filename = "tracks/bigloop.rct";
        SimulationState state = SimulationState.readFromXML(filename);
        state.v0 *= 10;

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
        WorldCreator creator = new WorldCreator();

        TransformGroup track = creator.createTrack(state);
        world.addChild(track);

        TransformGroup car = creator.createCar(state);
        world.addChild(car);

        TransformGroup ground = creator.createGround(state);
        world.addChild(ground);

        Node light = creator.createLight();
        world.addChild(light);

        // Create the universe and add the group of objects
        SimpleUniverse universe = new SimpleUniverse();
        universe.getViewingPlatform().setNominalViewingTransform();
        camera = universe.getViewingPlatform().getViewPlatformTransform();

        Canvas3D canvas = universe.getCanvas();
        canvas.setDoubleBufferEnable(true);
        SwingUtilities.windowForComponent(canvas).setSize(160 * 6, 90 * 6);

        BranchGroup branchGroup = new BranchGroup();
        branchGroup.addChild(world);
        universe.addBranchGraph(branchGroup);

        View view = canvas.getView();
        view.setBackClipDistance(1000);

        //animateCarSimple(state, car);
        animateCar(state, car);
    }

}
