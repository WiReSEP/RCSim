/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tubs.wire.simulator.physics;

import org.apache.commons.math3.linear.RealVector;

/**
 * A ForceModel is a general model for a physical force like gravity, friction 
 * or electormagnectic forces. The force may depend on the position in space and 
 * on velocity. Forces that aris from a potential may return the potential 
 * energy at the given position, non-conservative forces may just return zero 
 * here.
 * 
 * @author ezander
 */
public interface ForceModel {

    /**
     * Return the force on some object at position x and velocity v.
     * 
     * @param x The position of the object.
     * @param v The velocity of the object.
     * @return The force vector.
     */
    RealVector getForce(RealVector x, RealVector v);
    
    /**
     * Return the potential energy of some object at position x and velocity v. 
     * If the force is not conservative (i.e. there is no potential) just 
     * return 0.
     * 
     * @param x The position of the object.
     * @param v The velocity of the object.
     * @return The potential energy.
     */
    double getPotentialEnergy(RealVector x, RealVector v);
}
