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
package rcdemo.graphics.java3d;

import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import rcdemo.graphics.camera.CameraFactory;
import rcdemo.simulator.Observer;
import rcdemo.simulator.SimulationState;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public abstract class Java3dObserverBase implements Observer {

    BranchGroup branchGroup;
    TransformGroup world;
    TransformGroup car;
    SimulationState state;
    Track track;

    TransformGroup createWorld(SimulationState state1) {
        // Setup the branch group
        TransformGroup worldNode = new TransformGroup();
        WorldCreator creator = new WorldCreator();
        TransformGroup trackGroup = creator.createTrack(state1);
        worldNode.addChild(trackGroup);
        car = creator.createCar(state1);
        worldNode.addChild(car);
        TransformGroup ground = creator.createGround(state1);
        worldNode.addChild(ground);
        Node light = creator.createLight();
        worldNode.addChild(light);
        return worldNode;
    }

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
    }

    static final List<CameraFactory.CameraType> camList = new ArrayList<>();

    static {
        camList.add(CameraFactory.CameraType.TRACKING_FROM_ABOVE);
        camList.add(CameraFactory.CameraType.TRACKING_FROM_CENTER);
        camList.add(CameraFactory.CameraType.TRACKING_FROM_BELOW);
        camList.add(CameraFactory.CameraType.STILL_FROM_ABOVE);
        camList.add(CameraFactory.CameraType.STILL_FROM_GROUND);
        camList.add(CameraFactory.CameraType.STILL_FROM_STRAIGHT_ABOVE);
        camList.add(CameraFactory.CameraType.INSIDE_COACH);
        camList.add(CameraFactory.CameraType.LEFT_OF_COACH);
        camList.add(CameraFactory.CameraType.RIGHT_OF_COACH);
        camList.add(CameraFactory.CameraType.BEHIND_COACH);
        camList.add(CameraFactory.CameraType.MOVING1);
    }

}
