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
import rcdemo.graphics.TrackHelper;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public class StillCamera implements Camera {
    public enum Position {
        MIN, MAX, MEAN
    }
    Point3d eye, target;
    Vector3d z;
    Position pos;

    public StillCamera(Position pos) {
        this.pos = pos;
    }
    
    @Override
    public void init(Track track) {
        Vector3d[] stats = TrackHelper.getStatistics(track);
        //eye = new Point3d(200, 200, 200);
        Vector3d dim = stats[3];
        switch (pos) {
            case MIN:
                eye = new Point3d(stats[0]);
                dim.scale(-0.5);
                eye.add(dim);
                eye.setZ(stats[0].getZ());
                z = new Vector3d(0, 0, 1);
                break;
            case MAX:
                eye = new Point3d(stats[1]);
                dim.scale(0.5);
                eye.add(dim);
                z = new Vector3d(0, 0, 1);
                break;
            case MEAN:
                eye = new Point3d(stats[2]);
                eye.setZ(eye.getZ() + 1000);
                z = new Vector3d(0, 1, 0);
                break;
        }
        target = new Point3d(stats[2]);
    }

    @Override
    public Transform3D getTransform(Track track, double s, double dsdt) {
        Transform3D transform = new Transform3D();
        transform.lookAt(eye, target, z);
        return transform;
    }
    

}
