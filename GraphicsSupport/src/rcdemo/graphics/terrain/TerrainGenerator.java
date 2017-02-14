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
package rcdemo.graphics.terrain;

import java.util.Random;

/**
 *
 * @author ezander
 */
public abstract class TerrainGenerator {

    protected final Random rng;

    public TerrainGenerator() {
        rng = new Random(1236);
    }

    public abstract double[][] generate(int N);

    public double random() {
        return rng.nextDouble();
    }

    public static double max(double[][] zpos) {
        int M = zpos.length;
        int N = zpos[0].length;
        double maxVal = zpos[0][0];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                maxVal = Math.max(maxVal, zpos[i][j]);
            }
        }
        return maxVal;
    }

    public static double avg(double[][] zpos, int[] is, int[] js, int M, int N) {
        int n = 0;
        //System.out.println("zpos: " + zpos[i][j]);
        double sum = 0;
        for (int k = 0; k < is.length; k++) {
            int i = is[k];
            int j = js[k];
            boolean wrap = false;
            if (wrap) {
                while (i < 0) {
                    i += M;
                }
                while (i >= M) {
                    i -= M;
                }
                while (j < 0) {
                    j += N;
                }
                while (j >= N) {
                    j += N;
                }
            }
            if (i >= 0 && i < M && j >= 0 && j < N) {
                n++;
                sum += zpos[i][j];
                //System.out.println("zpos: " + zpos[i][j]);
            }
        }
        assert n >= 3;
        return sum / n;
    }

}
