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
package rcdemo.graphics.javaFX;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import rcdemo.graphics.camera.CameraFactory;
import rcdemo.simulator.Observer;
import rcdemo.simulator.SimulationState;
import rcdemo.track.Track;


/**
 *
 * @author ezander
 */
public abstract class JavaFXObserverBase implements Observer {

    Group world;
    Group car;
    SimulationState state;
    Track track;

    Group createWorld(SimulationState state1) {
        // Setup the branch group
        Group worldNode = new Group();
        WorldCreatorJFX creator = new WorldCreatorJFX();
        Group trackGroup = creator.createTrack(state1);
        worldNode.getChildren().add(trackGroup);
        //car = creator.createCar(state1);
        //worldNode.getChildren().add(car);
        Group ground = creator.createGround(state1);
        worldNode.getChildren().add(ground);
        //Node light = creator.createLight();
        //worldNode.getChildren().add(light);
        return worldNode;
    }

    public void notify(double t, double[] y) {
        assert track != null;
        double s = y[0];
        double dsdt = y[1];
//        Point3D currentPos = TrackHelper.getPosition(track, s);
//        Transform3D transform = new Transform3D();
//        transform.setTranslation(currentPos);
//        Matrix3d rot = new Matrix3d();
//        Point3D[] rhs = TrackHelper.getRHS(track, s);
//        rot.setColumn(0, rhs[0]);
//        rot.setColumn(1, rhs[2]);
//        rot.setColumn(2, rhs[1]);
//        transform.setRotation(rot);
//        car.setTransform(transform);
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
