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
package rcdemo.graphics.camera;

import rcdemo.graphics.RHS;
import rcdemo.graphics.TrackHelper;

/**
 *
 * @author ezander
 */
public class CoachCamera<Vector> extends BaseCamera<Vector> {
    public enum Position {
        INSIDE, BEHIND, LEFT, RIGHT
    }
    Position pos;

    public CoachCamera(Position pos, TrackHelper<Vector> helper) {
        super(helper);
        this.pos = pos;
    }

    @Override
    public CameraView<Vector> getTransform(double s, double dsdt) {
        Vector currentPos = helper.getPosition(track, s);
        RHS<Vector> rhs = helper.getRHS(track, s);
        Vector up = rhs.getUp();

        double fe, re, ue;
        double ft, rt, ut;
        Vector z;

        switch (pos) {
            case BEHIND:
                fe=-5.0; re=0.0; ue=2.0;
                ft= 5.0; rt=0.0; ut=0.0;
                z = helper.va.copy(up);
                break;
            case INSIDE:
                fe= 0.0; re=0.0; ue=1.0;
                ft= 5.0; rt=0.0; ut=0.0;
                z = helper.va.copy(up);
                break;
            case LEFT:
                fe= 0.0; re=-10.0; ue=0.0;
                ft= 0.0; rt= 10.0; ut=0.0;
                z = helper.va.fromDouble(new double[]{0,0,1});
                break;
            case RIGHT:
                fe= 0.0; re= 10.0; ue=0.0;
                ft= 0.0; rt=-10.0; ut=0.0;
                z = helper.va.fromDouble(new double[]{0,0,1});
                break;
            default:
                throw new RuntimeException("this cannot happen");
        }
        Vector eye = helper.getShiftedPos(currentPos, rhs, fe, re, ue);
        Vector target = helper.getShiftedPos(currentPos, rhs, ft, rt, ut);
        return new CameraView<Vector>(eye, target, z);
    }

}
