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
package de.tubs.wire.simulator.math;

/**
 * Abstract interface for spline curves.
 * 
 * @author ezander
 */
public interface Spline {
    /**
     * Return the (parametric) length of the curve.
     * 
     * That means if length() returns L, then compute can be called with values 
     * of s from 0 to L.
     * 
     * @return Length of spline.
     */
    double length();

    /**
     * Compute value or derivative of spline.
     * 
     * Compute value spline or derivatives up to second order. deriv=0 means 
     * just compute the value, deriv=1 means compute first derivative, 
     * deriv=2 mean compute second derivative.
     * 
     * @param s The curve parameter.
     * @param deriv Order of derivative.
     * @return The value.
     */
    double compute(double s, int deriv);
    
}
