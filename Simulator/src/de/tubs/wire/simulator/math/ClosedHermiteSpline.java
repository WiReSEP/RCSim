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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Implementation of closed cubic Hermite splines.
 * 
 * @author ezander
 * @see <a href="https://en.wikipedia.org/wiki/Cubic_Hermite_spline">Wikipedia: Cubic Hermite spline</a>
 */
public class ClosedHermiteSpline implements Spline {
    
    /** Matrix of coefficient for the Hermite basis function, array of size 4xN */
    private final RealMatrix A;

    /**
     * Generate Hermite Spline from coefficient matrix.
     * 
     * @param coeffs A 4xn matrix of coefficients.
     */
    public ClosedHermiteSpline(RealMatrix coeffs) {
        this.A = coeffs;
    }

    /**
     * Generate closed Hermite spline from path.
     * 
     * @param p Vector containing the points to be interpolated.
     */
    public ClosedHermiteSpline(RealVector p) {
        this(ClosedHermiteSpline.generate(p));
    }
    
    /**
     * Get length of spline.
     * @return The length.
     */
    @Override
    public double length() {
        return A.getColumnDimension();
    }
    
    @Override
    public double compute(double s, int deriv) {
        // Compute the section of the spline curve we're in (k) and the 
        // fractional remaining there (s).
        int n = (int)length();
        int k = (int)Math.floor(s);
        s = s - k;
        k = k % n; 
        if (k<0) {k+=n;}

        // Compute Hermite basis functions
        RealVector h = getHermiteBasis(s, deriv);
        
        // The final value is the inner product between the column k of the 
        // coefficent matrix and the values of the Hermite basis functions.
        RealVector ak = A.getColumnVector(k);
        return h.dotProduct(ak);
    }

    /**
     * Compute the Hermite basis functions or their derivatives.
     * 
     * @param s The parameter in [0,1].
     * @param deriv The derivative order.
     * @return The values of the basis functions as a RealVector.
     */
    RealVector getHermiteBasis(double s, int deriv) throws RuntimeException {
        double s2 = s * s;
        double s3 = s2 * s;
        double d[];
        switch (deriv) {
            case 0:
                d = new double[]{
                    2 * s3 - 3 * s2 + 1, s3 - 2 * s2 + s, -2 * s3 + 3 * s2, s3 - s2};
                break;
            case 1:
                d = new double[]{
                    6 * s2 - 6 * s, 3 * s2 - 4 * s + 1, -6 * s2 + 6 * s, 3 * s2 - 2 * s};
                break;
            case 2:
                d = new double[]{
                    12 * s - 6, 6 * s - 4, -12 * s + 6, 6 * s - 2};
                break;
            default:
                throw new RuntimeException("Derivative requested to high");
        }
        return new ArrayRealVector(d);
    }
    
    /**
     * Compute the coefficient matrix with respect to the Hermite basis 
     * functions the node values in p.
     * 
     * @param p The values that shall be interpolated by the Hermite splines.
     * @return The coefficient matrix.
     */
    public static RealMatrix generate(RealVector p) {
        int n = p.getDimension();
        RealMatrix A = MatrixUtils.createRealMatrix(n, n);
        RealMatrix B = MatrixUtils.createRealMatrix(n, n);
        for (int i = 0; i < n; i++) {
            int ip = (i + 1) % n;
            int im = (i - 1 + n) % n;
            A.setEntry(i, im, 2);
            A.setEntry(i, i, 8);
            A.setEntry(i, ip, 2);
            B.setEntry(i, im, -6);
            B.setEntry(i, ip, 6);
        }
        RealVector Bp = B.operate(p);
        DecompositionSolver solver = new CholeskyDecomposition(A).getSolver();
        RealVector m = solver.solve(Bp);
        RealMatrix a = MatrixUtils.createRealMatrix(4, n);
        for (int i = 0; i < n; i++) {
            int ip = (i + 1) % n;
            a.setEntry(0, i, p.getEntry(i));
            a.setEntry(1, i, m.getEntry(i));
            a.setEntry(2, i, p.getEntry(ip));
            a.setEntry(3, i, m.getEntry(ip));
        }
        return a;
    }
    
}
