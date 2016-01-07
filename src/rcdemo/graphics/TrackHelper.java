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

import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
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

    public static Vector3d trackToWorld(RealVector v) {
        //return trackToWorld2(v.mapSubtract(200));
        return trackToWorld2(v);
    }

    public static Vector3d trackToWorld2(RealVector v) {
        //double alpha = 0.003;
        //double x[] = v.mapMultiply(alpha).toArray();
        double[] x = v.toArray();
        return toVector3d(x);
    }

    public static Vector3d toVector3d(double[] x) {
        //return new Vector3d(x[0], x[2], x[1]);
        return new Vector3d(x[0], x[1], x[2]);
    }

    public static Vector3d[] getRHS(Track track, double s) {
        Vector3d[] rhs = {trackToWorld2(track.getDxDs(s)), new Vector3d(), trackToWorld2(track.getYaw(s))};
        rhs[1].cross(rhs[0], rhs[2]);
        rhs[2].cross(rhs[1], rhs[0]);
        //right.scale(-1);
        for (int i = 0; i < 3; i++) {
            rhs[i].normalize();
        }
        return rhs;
    }

    public static Vector3d getPosition(Track track, double s) {
        return trackToWorld2(track.getx(s));
    }
    
    public static double[] getDoubles(Vector3d v) {
        double d[]=new double[3];
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
            for( int i=0; i<3; i++){
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
