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
package rcdemo.graphics;

import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.SwingUtilities;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import rcdemo.graphics.camera.Camera;
import rcdemo.graphics.camera.CameraFactory;
import rcdemo.simulator.Observer;
import rcdemo.simulator.SimulationState;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public class Java3dObserver implements Observer {
    TransformGroup camera;
    Camera camTransform;
    TransformGroup car;
    SimulationState state;
    Track track;

    @Override
    public void init(SimulationState state) {
        this.state = state;
        this.track = state.getTrack();
        assert (track != null);
        // Setup the branch group
        TransformGroup world = new TransformGroup();
        WorldCreator creator = new WorldCreator();
        TransformGroup trackGroup = creator.createTrack(state);
        world.addChild(trackGroup);
        car = creator.createCar(state);
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
        //camTransform = CameraFactory.buildCamera(CameraFactory.CameraType.TRACKING_FROM_ABOVE);
        camTransform.init(track);
    }

    @Override
    public void notify(double t, double[] y) {
        assert track != null;
        double s = y[0];
        double dsdt = y[1];
        Vector3d currentPos = TrackHelper.getPosition(track, s);
        Transform3D transform = new Transform3D();
        transform.setTranslation(currentPos);
        Matrix3d rot = new Matrix3d();
        Vector3d[] rhs = TrackHelper.getRHS(track, s);
        rot.setColumn(0, rhs[0]);
        rot.setColumn(1, rhs[2]);
        rot.setColumn(2, rhs[1]);
        transform.setRotation(rot);
        car.setTransform(transform);
        transform = camTransform.getTransform(track, s, dsdt);
        transform.invert();
        camera.setTransform(transform);
    }
    
}
