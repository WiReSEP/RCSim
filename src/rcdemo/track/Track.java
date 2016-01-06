/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcdemo.track;

import org.apache.commons.math3.linear.RealVector;

/**
 * Interface for tracks. Implementations can use real physical models or just
 * describe parametric curves.
 *
 * @author ezander
 */
public interface Track {
    double length();

    RealVector getx(double s);

    RealVector getDxDs(double s);

    RealVector getDDxDss(double s);

    RealVector getYaw(double s);
    
    double getYawAngle(double s);
}
