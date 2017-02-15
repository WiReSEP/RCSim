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
package de.tubs.wire.simulator.track;

import de.tubs.wire.simulator.math.RHS;
import de.tubs.wire.simulator.math.VectorMath;
import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author ezander
 */
public class TrackHelper<Vector> {
    
    public VectorMath<Vector> vecmath;

    public TrackHelper(VectorMath<Vector> va) {
        this.vecmath = va;
    }

    public Vector toVector(RealVector v) {
        double[] x = v.toArray();
        return vecmath.fromDouble(x);
    }
    
    public Vector addScaled(Vector v1, Vector v2, double alpha2) {
        return vecmath.add(v1, vecmath.multiply(v2, alpha2));
    }

    public Vector addScaled(Vector v1, double alpha1, Vector v2, double alpha2) {
        return vecmath.add(vecmath.multiply(v1, alpha1), vecmath.multiply(v2, alpha2));
    }
    
    public Vector getShiftedPos(Vector pos, RHS<Vector> rhs, double f, double r, double z) {
        Vector res = pos;
        if( f!=0 ) res = addScaled(res, rhs.getForward(), f);
        if( r!=0 ) res = addScaled(res, rhs.getLeft(), -r);
        if( z!=0 ) res = addScaled(res, rhs.getUp(), z);
        return res;
    }
    

    public Vector getPosition(Track track, double s) {
        return toVector(track.getx(s));
    }

    public Vector getForward(Track track, double s) {
        return vecmath.normalize(toVector(track.getDxDs(s)));
    }

    public Vector getSpeed(Track track, double s, double dsdt) {
        return vecmath.multiply(toVector(track.getDxDs(s)), dsdt);
    }
    
    public Vector getYaw(Track track, double s) {
        return vecmath.normalize(toVector(track.getYaw(s)));
    }
    
    public RHS<Vector> getRHS(Track track, double s) {
        Vector forward = getForward(track, s);
        Vector up = getYaw(track, s);
        return RHS.createRHS(forward, up, vecmath);
    }


    public Vector getRailPos(Track track, double s, double dist) {
        Vector pos = getPosition(track, s);
        RHS<Vector> rhs = getRHS(track, s);
        return addScaled(pos, rhs.getLeft(), -dist);
    }

    public double[] getDoubles(Vector v) {
        return vecmath.toDouble(v);
    }

    public static class TrackStats<Vector> {
        public Vector min;
        public Vector max;
        public Vector mean;
        public Vector dim;

        public TrackStats(double[] min, double[] max, double[] mean, double[] dim, VectorMath<Vector> va) {
            this.min = va.fromDouble(min);
            this.max = va.fromDouble(max);
            this.mean = va.fromDouble(mean);
            this.dim = va.fromDouble(dim);
        }
    }
    public TrackStats<Vector> getStatistics(Track track) {
        double big = 100000, small = -big;
        double[] min = {big, big, big};
        double[] max = {small, small, small};
        double[] mean = {0, 0, 0};
        double[] dim = {0, 0, 0};

        double ds = 0.01;
        double n = track.getPeriod();
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
        return new TrackStats<>(min, max, mean, dim, vecmath);
    }

}
