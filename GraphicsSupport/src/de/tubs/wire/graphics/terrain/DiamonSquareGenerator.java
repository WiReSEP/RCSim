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
package de.tubs.wire.graphics.terrain;

/**
 *
 * @author ezander
 */
public class DiamonSquareGenerator extends TerrainGenerator {
    final double roughness = 0.54; //0.4;
    static final double maxHeight = 1500;

    
    public DiamonSquareGenerator() {
    }


    @Override
    public double[][] generate(int N) {
        double[][] zpos = new double[N][N];
        fractalGen(zpos, N - 1, maxHeight, 1, N, N);
        return zpos;
    }

    public void fractalGen(double[][] zpos, int s, double r, double alpha, int M, int N) {
        double sq = 1.0 / Math.sqrt(2);
        while (s >= 2) {
            int s2 = s / 2;
            alpha *= roughness;
            for (int i = 0; i + s2 < M; i += s) {
                for (int j = 0; j + s2 < N; j += s) {
                    double d = r * alpha * (2 * random() - 1);
                    int[] is = {i, i + s, i + s, i};
                    int[] js = {j, j, j + s, j + s};
                    double mean = avg(zpos, is, js, M, N);
                    //System.out.format("zpos: %s", zpos[i + s2][j + s2]);
                    zpos[i + s2][j + s2] = mean + d;
                    //System.out.format("=> %s\n", zpos[i + s2][j + s2]);
                }
            }
            for (int i = 0; i + s2 < M; i += s) {
                for (int j = 0; j < N; j += s) {
                    int[] is = {i, i + s2, i + s2, i + s};
                    int[] js = {j, j + s2, j - s2, j};
                    double mean = avg(zpos, is, js, M, N);
                    double d = r * alpha * (2 * random() - 1) * sq;
                    //System.out.format("zpos: %s", zpos[i + s2][j]);
                    zpos[i + s2][j] = mean + d;
                    //System.out.format("=> %s\n", zpos[i + s2][j]);
                }
            }
            for (int i = 0; i < M; i += s) {
                for (int j = 0; j + s2 < N; j += s) {
                    int[] is = {i, i + s2, i - s2, i};
                    int[] js = {j, j + s2, j + s2, j + s};
                    double mean = avg(zpos, is, js, M, N);
                    double d = r * alpha * (2 * random() - 1) * sq;
                    //System.out.format("zpos: %s", zpos[i][j + s2]);
                    zpos[i][j + s2] = mean + d;
                    //System.out.format("=> %s\n", zpos[i][j + s2]);
                }
            }
            s = s2;
        }
    }

    
}
