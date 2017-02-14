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
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import rcdemo.graphics.RHS;
import rcdemo.graphics.camera.CameraFactory;
import de.tubs.wire.simulator.Observer;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.simulator.track.Track;

/**
 *
 * @author ezander
 */
public abstract class Java3dObserverBase implements Observer {

    protected BranchGroup branchGroup;
    protected TransformGroup world;
    protected TransformGroup car;
    protected TrackInformation state;
    protected Track track;
    protected TrackHelperJ3d helper = new TrackHelperJ3d();

    TransformGroup createWorld(TrackInformation state1) {
        // Setup the branch group
        WorldCreatorJ3d creator = new WorldCreatorJ3d();
        
        TransformGroup worldNode = new TransformGroup();
        
        TransformGroup trackGroup = creator.createTrack(state1);
        worldNode.addChild(trackGroup);
        
        car = creator.createCar(state1);
        worldNode.addChild(car);
        
        TransformGroup ground = creator.createGround(state1);
        worldNode.addChild(ground);
        
        Node light = creator.createLight(state1);
        worldNode.addChild(light);
        
        return worldNode;
    }

    public void notify(double t, double[] y) {
        assert track != null;
        double s = y[0];
        double dsdt = y[1];
        Vector3d currentPos = helper.getPosition(track, s);
        RHS<Vector3d> rhs = helper.getRHS(track, s);
        
        WorldCreatorJ3d creator = new WorldCreatorJ3d();
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
