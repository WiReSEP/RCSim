/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tubs.wire.simulator.track;

import org.apache.commons.math3.linear.RealVector;

/**
 * Interface for the mathematical description of rollercoaster tracks.
 * 
 * Implementations of this interface can use real physical models, maybe loaded 
 * from filed, or just describe parametric curves like circles.
 * 
 * Implementations must be described with respect to some curve parameter s. 
 * Closed curves must return the periodicity in getPeriod. The choice of curve 
 * parameter is up to the implementation: e.g. a circle could be implemented 
 * with a curve parameter repeating after s=1, or after s=2*pi, or after s=360. 
 * Of course, dependent functions (getx, getDxDx,...) must be modified 
 * accordingly.
 *
 * @author ezander
 */
public interface Track {
    /**
     * Return the period in curve parameter after which the curve repeats.
     * 
     * Meaningful for closed curved. Open curves should return a value of 0.
     * E.g. if <code>double p = track.getPeriod()</code>; then 
     * <code>assert getx(s)==getx(s+p)<code> should be true (at least up to some 
     * epsilon.
     * 
     * @return Periodicity of the curve.
     */
    double getPeriod();

    /**
     * Get position of the track.
     * 
     * @param s The curve parameter.
     * @return The position of the track at s.
     */
    RealVector getx(double s);

    /**
     * Get derivative of the track.
     * 
     * @param s The curve parameter.
     * @return The first derivative dx/ds of the track at s with respect to s.
     */
    RealVector getDxDs(double s);

    /**
     * Get second derivative of the track.
     * 
     * @param s The curve parameter.
     * @return The second derivative d^2x/dx^2 of the track at s with respect to s.
     */
    RealVector getDDxDss(double s);

    /**
     * Returns the yaw vector (i.e. the up vector).
     * 
     * The yaw vector is the local up vector, i.e. where your head is pointing 
     * to when you are inside the coach. Should always be orthogonal to getDxDs.
     * 
     * Need not be normalised, but should never be zero.
     * 
     * @param s The curve parameter
     * @return The yaw vector.
     */
    RealVector getYaw(double s);
    
    /**
     * In some coasters (e.g. Wilde Maus) you are around the yaw vector, 
     * sometimes pointing backwards against the driving direction. This is 
     * indicated by a counter clockwise (mathematically positive) rotation 
     * around the yaw vector. Goes from 0 to 2*pi.
     * 
     * @param s
     * @return The yaw angle in [0, 2*pi].
     */
    double getYawAngle(double s);
}
