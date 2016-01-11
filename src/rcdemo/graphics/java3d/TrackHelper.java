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

import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import org.apache.commons.math3.linear.RealVector;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public class TrackHelper {

    public static TransformGroup transform(Node node, Vector3d vector) {
        return transform(node, vector, false);
    }

    public static TransformGroup transform(Node node, Vector3d vector, boolean modifyable) {
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

    public static Vector3d toVector3d(RealVector v) {
        double[] x = v.toArray();
        return new Vector3d(x);
    }

    public static Vector3d[] getRHS(Track track, double s) {
        Vector3d[] rhs = {toVector3d(track.getDxDs(s)), new Vector3d(), toVector3d(track.getYaw(s))};
        rhs[1].cross(rhs[0], rhs[2]);
        rhs[2].cross(rhs[1], rhs[0]);
        //right.scale(-1);
        for (int i = 0; i < 3; i++) {
            rhs[i].normalize();
        }
        return rhs;
    }

    public static Vector3d addScaled(Vector3d v1, Vector3d v2, double alpha2) {
        Vector3d res = new Vector3d(v2);
        res.scaleAdd(alpha2, v1);
        return res;
    }

    public static Vector3d addScaled(Vector3d v1, double alpha1, Vector3d v2, double alpha2) {
        Vector3d c1 = new Vector3d(v1);
        c1.scale(alpha1);
        return addScaled(c1, v2, alpha2);
    }
    
    public static Vector3d getShiftedPos(Vector3d pos, Vector3d rhs[], double f, double r, double z) {
        Vector3d res = pos;
        if( f!=0 ) res = addScaled(res, rhs[0], f);
        if( r!=0 ) res = addScaled(res, rhs[1], -r);
        if( z!=0 ) res = addScaled(res, rhs[2], z);
        return res;
    }
    
    public static Vector3d getRailPos(Track track, double s, double dist) {
        Vector3d pos = getPosition(track, s);
        Vector3d rhs[] = getRHS(track, s);

        Vector3d left = rhs[1];
        left.scale(-dist);

        pos.add(left);
        //return pos;
        return addScaled(pos, left, -dist);
    }

    public static Vector3d getPosition(Track track, double s) {
        return toVector3d(track.getx(s));
    }

    public static double[] getDoubles(Vector3d v) {
        double d[] = new double[3];
        v.get(d);
        return d;
    }

    public static Vector3d[] getStatistics(Track track) {
        double big = 100000, small = -big;
        double[] min = {big, big, big};
        double[] max = {small, small, small};
        double[] mean = {0, 0, 0};
        double[] dim = {0, 0, 0};

        double ds = 0.01;
        double n = track.length();
        double inv_n = ds / n;

        for (double s = 0; s <= n; s += ds) {
            double[] pos = getDoubles(getPosition(track, s));
            for (int i = 0; i < 3; i++) {
                min[i] = Math.min(min[i], pos[i]);
                max[i] = Math.max(max[i], pos[i]);
                mean[i] = mean[i] + pos[i] * inv_n;
                dim[i] = max[i] - min[i];
            }
        }
        return new Vector3d[]{
            new Vector3d(min),
            new Vector3d(max),
            new Vector3d(mean),
            new Vector3d(dim)};
    }

}
