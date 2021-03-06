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

import de.tubs.wire.simulator.math.RHS;
import de.tubs.wire.simulator.track.TrackHelper;
import de.tubs.wire.simulator.math.VectorMath;
import de.tubs.wire.simulator.track.Track;

/**
 * A camera that 
 * @author ezander
 * @param <Vector> The vector class.
 */
public class TrackingCamera<Vector> extends BaseCamera<Vector> {
    public enum Position {
        MIN, MAX, MEAN, MOVING
    }
    Position pos;
    Vector eye;
    
    public TrackingCamera(Position pos, TrackHelper<Vector> helper) {
        super(helper);
        this.pos = pos;
    }
    
    @Override
    public void init(Track track) {
        super.init(track);
        
        TrackHelper.TrackStats<Vector> stats = helper.getStatistics(track);
        VectorMath<Vector> va = helper.vecmath;

        switch (pos) {
            case MIN:
                eye = va.copy(stats.min);
                break;
            case MAX:
                eye = va.copy(stats.max);
                break;
            case MEAN:
                eye = va.copy(stats.mean);
                break;
            case MOVING:
                eye = va.copy(stats.mean);
                break;
            default:
                assert false;
        }
    }
    
    @Override
    public CameraView<Vector> getView(double s, double dsdt) {
        VectorMath<Vector> vecmath = helper.vecmath;

        Vector currentPos = helper.getPosition(track, s);
        Vector target = vecmath.copy(currentPos);
        if (pos == Position.MOVING){
            double l = vecmath.distance(eye, currentPos);
            double mdist = 100.0d;
            if (l>mdist) {
                eye = currentPos;
                RHS<Vector> rhs = helper.getRHS(track, s);
                eye = helper.addScaled(currentPos, rhs.getForward(), Math.signum(dsdt)*mdist*0.95);
                eye = helper.addScaled(eye, rhs.getUp(), 10);
            }
        }
        Vector z = vecmath.unit(2);
        return new CameraView<>(eye, target, z);
    }

}
