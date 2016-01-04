/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcdemo.physics;

import org.apache.commons.math3.linear.RealVector;

/**
 *
 * @author ezander
 */
public interface ForceModel {

    RealVector getForce(RealVector x, RealVector v);
    
}
