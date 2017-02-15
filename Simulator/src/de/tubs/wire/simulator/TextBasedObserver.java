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
package de.tubs.wire.simulator;

/**
 *
 * @author ezander
 */
public class TextBasedObserver<SimulationInfo> implements Observer<SimulationInfo> {

    @Override
    public void init(SimulationInfo info) {
    }

    @Override
    public void notify(double t, double[] y) {
        System.out.format("t=%4.2f  s=%4.2f  s'=%4.2f\n", t, y[0], y[1]);
    }

}
