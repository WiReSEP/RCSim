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
package rcdemo.graphics.camera;

import rcdemo.graphics.camera.Camera;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import rcdemo.graphics.java3d.TrackHelper;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public class TrackingCamera implements Camera {
    public enum Position {
        MIN, MAX, MEAN, MOVING
    }
    Point3d eye;
    Position pos;

    public TrackingCamera(Position pos) {
        this.pos = pos;
    }
    
    @Override
    public void init(Track track) {
        Vector3d[] stats = TrackHelper.getStatistics(track);
        //eye = new Point3d(200, 200, 200);
        switch (pos) {
            case MIN:
                eye = new Point3d(stats[0]);
                break;
            case MAX:
                eye = new Point3d(stats[1]);
                break;
            case MEAN:
                eye = new Point3d(stats[2]);
                break;
            case MOVING:
                eye = new Point3d(stats[2]);
                break;
            default:
                assert false;
        }
    }
    
    @Override
    public Transform3D getTransform(Track track, double s, double dsdt) {
        Transform3D transform = new Transform3D();
        Vector3d currentPos = TrackHelper.getPosition(track, s);
        Point3d target = new Point3d(currentPos);
        if (pos == Position.MOVING){
            Point3d pos = new Point3d(currentPos);
            double l = eye.distance(pos);
            double mdist = 100.0d;
            if (l>mdist) {
                eye = pos;
                Vector3d[] rhs = TrackHelper.getRHS(track, s);
                eye = new Point3d(TrackHelper.addScaled(new Vector3d(pos), rhs[0], Math.signum(dsdt)*mdist*0.95f));
                eye = new Point3d(TrackHelper.addScaled(new Vector3d(eye), rhs[2], 10));
            }
        }
        Vector3d z = new Vector3d(0, 0, 1);
        transform.lookAt(eye, target, z);
        return transform;
    }

}
