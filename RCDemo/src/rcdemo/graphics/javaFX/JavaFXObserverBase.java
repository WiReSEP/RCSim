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
import javafx.geometry.Point3D;
import javafx.scene.Group;
import rcdemo.graphics.RHS;
import rcdemo.graphics.camera.CameraFactory;
import de.tubs.wire.simulator.Observer;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.simulator.track.Track;


/**
 *
 * @author ezander
 */
public abstract class JavaFXObserverBase implements Observer {

    Group world;
    Group car;
    TrackInformation state;
    Track track;
    TrackHelperJFX helper = new TrackHelperJFX();

    Group createWorld(TrackInformation state1) {
        // Setup the branch group
        WorldCreatorJFX creator = new WorldCreatorJFX();

        Group worldNode = new Group();

        Group trackGroup = creator.createTrack(state1);
        worldNode.getChildren().add(trackGroup);
        
        car = creator.createCar(state1);
        worldNode.getChildren().add(car);
        
        Group ground = creator.createGround(state1);
        worldNode.getChildren().add(ground);
        
        Group light = creator.createLight(state1);
        worldNode.getChildren().add(light);
        return worldNode;
    }

    public void notify(double t, double[] y) {
        assert track != null;
        double s = y[0];
        double dsdt = y[1];
        Point3D currentPos = helper.getPosition(track, s);
        RHS<Point3D> rhs = helper.getRHS(track, s);
        
        WorldCreatorJFX creator = new WorldCreatorJFX();
        creator.setCarState(car, currentPos, rhs);
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
