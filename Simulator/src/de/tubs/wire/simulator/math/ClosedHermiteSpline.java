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
 * https://en.wikipedia.org/wiki/Cubic_Hermite_spline
 * @author ezander
 */
public class ClosedHermiteSpline implements Spline {
    private final RealMatrix a;

    public ClosedHermiteSpline(RealMatrix coeffs) {
        this.a = coeffs;
    }

    public ClosedHermiteSpline(RealVector p) {
        this(ClosedHermiteSpline.generate(p));
    }
    
    @Override
    public double length() {
        return a.getColumnDimension();
    }
    
    @Override
    public double compute(double t, int deriv) {
        int n = (int)length();
        int k = (int)Math.floor(t);
        t = t - k;
        k = k % n; 
        if (k<0) {k+=n;}
        
        double t2 = t * t;
        double t3 = t2 * t;
        double d[];
        switch (deriv) {
            case 0:
                d = new double[]{
                    2 * t3 - 3 * t2 + 1, t3 - 2 * t2 + t, -2 * t3 + 3 * t2, t3 - t2};
                break;
            case 1:
                d = new double[]{
                    6 * t2 - 6 * t, 3 * t2 - 4 * t + 1, -6 * t2 + 6 * t, 3 * t2 - 2 * t};
                break;
            case 2:
                d = new double[]{
                    12 * t - 6, 6 * t - 4, -12 * t + 6, 6 * t - 2};
                break;
            default:
                throw new RuntimeException("Derivative requested to high");
        }
        RealVector h = new ArrayRealVector(d);
        RealVector c = a.getColumnVector(k);
        return h.dotProduct(c);
    }
    
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
