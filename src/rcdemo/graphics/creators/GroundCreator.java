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
package rcdemo.graphics.creators;

import rcdemo.graphics.SceneCreator;
import rcdemo.graphics.TrackHelper;
import rcdemo.simulator.SimulationState;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public class GroundCreator<Vector, Node, Group extends Node> {
    TrackHelper<Vector> helper;
    SceneCreator<Vector, Node, Group> sc;

    public GroundCreator(TrackHelper<Vector> helper, SceneCreator<Vector, Node, Group> sc) {
        this.helper = helper;
        this.sc = sc;
    }

    public Group createGround(SimulationState state) {
        Track track = state.getTrack();
        Group t = sc.newGroup();
        TrackHelper.TrackStats<Vector> stats = helper.getStatistics(track);
        for (int i = 0; i < 6000; i++) {
            double[] x = new double[3];
            x[0] = Math.random() * 2000.0;
            x[1] = Math.random() * 2000.0;
            x[2] = Math.random() * 1000.0 - 500;
            Vector p = helper.va.fromDouble(x);
            Node s = sc.createSphere(3);
            sc.add(t, sc.translate(s, p));
        }
        return t;
    }
    
}
