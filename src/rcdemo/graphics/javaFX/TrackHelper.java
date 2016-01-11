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

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Translate;
import org.apache.commons.math3.linear.RealVector;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public class TrackHelper {

    public static Node transform(Node node, Point3D p) {
        Translate t = new Translate(p.getX(), p.getY(), p.getZ());
        node.getTransforms().add(t);
        return node;
    }

    public static Node transform(Node node, Point3D p, boolean mod) {
        Translate t = new Translate(p.getX(), p.getY(), p.getZ());
        node.getTransforms().add(t);
        return node;
    }

    public static Point3D toPoint3D(double[] x) {
        return new Point3D(x[0], x[1], x[2]);
    }

    public static Point3D toPoint3D(RealVector v) {
        double[] x = v.toArray();
        return new Point3D(x[0], x[1], x[2]);
    }
    
    public static double[] getDoubles(Point3D v) {
        double d[] = {v.getX(), v.getY(), v.getZ()};
        return d;
    }
    

    public static Point3D[] getRHS(Track track, double s) {
        Point3D[] rhs = {toPoint3D(track.getDxDs(s)), Point3D.ZERO, toPoint3D(track.getYaw(s))};
        rhs[1] = rhs[0].crossProduct(rhs[2]);
        rhs[2] = rhs[1].crossProduct(rhs[0]);
        for (int i = 0; i < 3; i++) {
            rhs[i] = rhs[i].normalize();
        }
        return rhs;
    }

    public static Point3D addScaled(Point3D v1, Point3D v2, double alpha2) {
        return v1.add(v2.multiply(alpha2));
    }

    public static Point3D addScaled(Point3D v1, double alpha1, Point3D v2, double alpha2) {
        return v1.multiply(alpha1).add(v2.multiply(alpha2));
    }
    
    public static Point3D getShiftedPos(Point3D pos, Point3D rhs[], double f, double r, double z) {
        Point3D res = pos;
        if( f!=0 ) res = addScaled(res, rhs[0], f);
        if( r!=0 ) res = addScaled(res, rhs[1], -r);
        if( z!=0 ) res = addScaled(res, rhs[2], z);
        return res;
    }
    
    public static Point3D getRailPos(Track track, double s, double dist) {
        Point3D pos = getPosition(track, s);
        Point3D rhs[] = getRHS(track, s);

        Point3D left = rhs[1];
        left.multiply(-dist);

        pos.add(left);
        //return pos;
        return addScaled(pos, left, -dist);
    }

    public static Point3D getPosition(Track track, double s) {
        return toPoint3D(track.getx(s));
    }


    public static Point3D[] getStatistics(Track track) {
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
        return new Point3D[]{
            toPoint3D(min),
            toPoint3D(max),
            toPoint3D(mean),
            toPoint3D(dim)};
    }

}
