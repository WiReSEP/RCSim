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
public class CoachCamera implements Camera {

    public enum Position {

        INSIDE, BEHIND, LEFT, RIGHT
    }
    Position pos;

    public CoachCamera(Position pos) {
        this.pos = pos;
    }

    @Override
    public void init(Track track) {
    }

    @Override
    public Transform3D getTransform(Track track, double s, double dsdt) {
        Transform3D transform = new Transform3D();
        Vector3d currentPos = TrackHelper.getPosition(track, s);
        Vector3d[] rhs = TrackHelper.getRHS(track, s);
        Vector3d forward = rhs[0];
        Vector3d right = rhs[1];
        Vector3d up = rhs[2];

        Point3d eye = new Point3d(currentPos);
        Point3d target = new Point3d(currentPos);
        Vector3d z;

        switch (pos) {
            case BEHIND:
                up.scale(2);
                forward.scale(5f);
                right.scale(1.0f);
                eye.sub(new Vector3d(forward));
                target.add(new Vector3d(forward));
                eye.add(up);
                z = new Vector3d(up);
                break;
            case INSIDE:
                up.scale(1);
                forward.scale(5f);
                right.scale(1.0f);
                target.add(new Vector3d(forward));
                eye.add(up);
                z = new Vector3d(up);
                break;
            case LEFT:
                right.scale(10.0f);
                eye.sub(right);
                target.add(right);
                z = new Vector3d(0,0,1);
                break;
            case RIGHT:
                right.scale(10.0f);
                eye.add(right);
                target.sub(right);
                z = new Vector3d(0,0,1);
                break;
            default:
                throw new RuntimeException("this cannot happen");
        }
        transform.lookAt(eye, target, z);
        return transform;
    }

}
