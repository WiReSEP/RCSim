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
package de.tubs.wire.graphics;

import static java.lang.Math.random;
import de.tubs.wire.simulator.track.TrackHelper;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.simulator.track.Track;

/**
 *
 * @author ezander
 */
public class GroundCreator<Vector, Node, Group extends Node> {
    TrackHelper<Vector> helper;
    Toolkit<Vector, Node, Group> sc;

    public GroundCreator(TrackHelper<Vector> helper, Toolkit<Vector, Node, Group> sc) {
        this.helper = helper;
        this.sc = sc;
    }

    public Group createGround(TrackInformation trackInfo) {
        Track track = trackInfo.getTrack();
        Group t = sc.newGroup();
        TrackHelper.TrackStats<Vector> stats = helper.getStatistics(track);
        double mean[] = helper.vecmath.toDouble(stats.mean);
        double dim[] = helper.vecmath.toDouble(stats.dim);
        for (int i = 0; i < 300; i++) {
            double[] x = new double[3];
            x[0] = mean[0] + 3 * dim[0] * (Math.random()-0.5);
            x[1] = mean[1] + 3 * dim[1] * (Math.random()-0.5);
            x[2] = mean[2] + 3 * dim[2] * (Math.random()-0.5);
            Vector p = helper.vecmath.fromDouble(x);
            Node s = sc.createSphere(3);
            sc.add(t, sc.translate(s, p));
        }
        return t;
    }
    
}
