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
 * Helper class to facilitate computations that are often needed when working 
 * with tracks.
 * 
 * @author ezander
 * @param <Vector> Vector class for which this TrackHelper is instantiated.
 */
public class TrackHelper<Vector> {
 
    public final VectorMath<Vector> vecmath;

    /**
     * Create a TrackHelper with an instance of VectorMath.
     * 
     * @param vecmath The VectorMath helper.
     */
    public TrackHelper(VectorMath<Vector> vecmath) {
        this.vecmath = vecmath;
    }

    /**
     * Convert RealVector to vector class.
     * 
     * This needed quite often since the Track itself is implemented in terms
     * of RealVectors.
     * 
     * @param v RealVector object.
     * @return Object of class Vector.
     */
    public Vector toVector(RealVector v) {
        double[] x = v.toArray();
        return vecmath.fromDouble(x);
    }
    
    /**
     * Add a scaled version of a vector to another.
     * 
     * @param v1 A vector.
     * @param v2 Another vector which will be scaled.
     * @param alpha2 Scaling for v2.
     * @return v1 + alpha2 * v2
     */
    public Vector addScaled(Vector v1, Vector v2, double alpha2) {
        return vecmath.add(v1, vecmath.multiply(v2, alpha2));
    }

    /**
     * Add two scaled vectors.
     * 
     * @param v1 A vector.
     * @param alpha1 Scaling for v1.
     * @param v2 Another vector.
     * @param alpha2 Scaling for v2.
     * @return alpha1 + v1 + alpha2 * v2
     */
    public Vector addScaled(Vector v1, double alpha1, Vector v2, double alpha2) {
        return vecmath.add(vecmath.multiply(v1, alpha1), vecmath.multiply(v2, alpha2));
    }
    
    /**
     * Get a position shift by some amount in one of three directions.
     * 
     * Takes a position and a right hand coordinate system (forward, left, and 
     * up vector) and shift the position by f in forward, r in right (i.e. -left) 
     * and by z in up direction
     * 
     * @param pos The position.
     * @param rhs The right hand coordinate system.
     * @param f The amount to move forward.
     * @param r The amount to move right.
     * @param z The amount to move up.
     * @return The shifted position.
     */
    public Vector getShiftedPos(Vector pos, RHS<Vector> rhs, double f, double r, double z) {
        Vector res = pos;
        if( f!=0 ) res = addScaled(res, rhs.getForward(), f);
        if( r!=0 ) res = addScaled(res, rhs.getLeft(), -r);
        if( z!=0 ) res = addScaled(res, rhs.getUp(), z);
        return res;
    }
    

    /**
     * Get current position on the track.
     * 
     * @param track The track.
     * @param s The curve parameter.
     * @return The position.
     */
    public Vector getPosition(Track track, double s) {
        return toVector(track.getx(s));
    }

    /**
     * Get the forward direction on the track.
     * 
     * @param track The track.
     * @param s The curve parameter.
     * @return The forward direction at the current position.
     */
    public Vector getForward(Track track, double s) {
        return vecmath.normalize(toVector(track.getDxDs(s)));
    }

    /**
     * Get the velocity on the track.
     * 
     * @param track The track.
     * @param s The curve parameter.
     * @param dsdt Time derivative of curve parameter.
     * @return The velocity.
     */
    public Vector getVelocity(Track track, double s, double dsdt) {
        return vecmath.multiply(toVector(track.getDxDs(s)), dsdt);
    }
    
    /**
     * Get the (normalised) yaw vector.
     * 
     * @param track The track.
     * @param s The curve parameter.
     * @return The current yaw parameter (normalised).
     */
    public Vector getYaw(Track track, double s) {
        return vecmath.normalize(toVector(track.getYaw(s)));
    }
    
    /**
     * Get the RHS at some position on the track.
     * 
     * @param track The track.
     * @param s The curve parameter.
     * @return The RHS (right hand coordinate system).
     */
    public RHS<Vector> getRHS(Track track, double s) {
        Vector forward = getForward(track, s);
        Vector up = getYaw(track, s);
        return RHS.createRHS(forward, up, vecmath);
    }


    /** 
     * Get position of a rail.
     * 
     * @param track The track.
     * @param s The curve parameter.
     * @param dist Horizontal distance from track.
     * @return Position of the rail.
     */
    public Vector getRailPos(Track track, double s, double dist) {
        Vector pos = getPosition(track, s);
        RHS<Vector> rhs = getRHS(track, s);
        return addScaled(pos, rhs.getLeft(), -dist);
    }

    /**
     * Class to collect statistics about a track.
     * 
     * @param <Vector> The underlying vector class.
     */
    public static class TrackStats<Vector> {
        public Vector min;
        public Vector max;
        public Vector mean;
        public Vector dim;

        public TrackStats(double[] min, double[] max, double[] mean, double[] dim, VectorMath<Vector> vecmath) {
            this.min = vecmath.fromDouble(min);
            this.max = vecmath.fromDouble(max);
            this.mean = vecmath.fromDouble(mean);
            this.dim = vecmath.fromDouble(dim);
        }
    }
    
    /**
     * Collect statistics about the track (min, max, mean position and dimensions).
     * 
     * @param track The tracks.
     * @return The statistics.
     */
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
            double[] pos = vecmath.toDouble(getPosition(track, s));
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
