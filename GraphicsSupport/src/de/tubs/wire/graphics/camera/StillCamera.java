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
package de.tubs.wire.graphics.camera;

import de.tubs.wire.simulator.track.TrackHelper;
import de.tubs.wire.simulator.math.VectorMath;
import de.tubs.wire.simulator.track.Track;

/**
 *
 * @author ezander
 */
public class StillCamera<Vector> extends BaseCamera<Vector>{
    public enum Position {
        MIN, MAX, MEAN
    }
    Vector eye, target, z;
    Position pos;

    public StillCamera(Position pos, TrackHelper<Vector> helper) {
        super(helper);
        this.pos = pos;
    }
    
    
    @Override
    public void init(Track track) {
        super.init(track);
        
        TrackHelper.TrackStats<Vector> stats = helper.getStatistics(track);
        VectorMath<Vector> vecmath = helper.TrackHelper.this.vecmath;
        
        switch (pos) {
            case MIN:
                eye = vecmath.add(stats.min, vecmath.multiply(stats.dim, -0.5));
                eye = vecmath.setEntry(eye, 2, vecmath.getEntry(stats.min, 2));
                z = helper.TrackHelper.this.vecmath.fromDouble(new double[]{0,0,1});
                break;
            case MAX:
                eye = vecmath.add(stats.max, vecmath.multiply(stats.dim, 0.5));
                z = helper.TrackHelper.this.vecmath.fromDouble(new double[]{0,0,1});
                break;
            case MEAN:
                eye = vecmath.add(stats.mean, vecmath.multiply(stats.dim, 0.5));
                eye = vecmath.setEntry(eye, 2, vecmath.getEntry(eye, 2)+1000);
                z = helper.TrackHelper.this.vecmath.fromDouble(new double[]{0,1,0});
                break;
        }
        target = stats.mean;
    }

    @Override
    public CameraView<Vector> getView(double s, double dsdt) {
        return new CameraView<Vector>(eye, target, z);
    }
    

}
