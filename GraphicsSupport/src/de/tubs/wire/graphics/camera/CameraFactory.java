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

/**
 *
 * @author ezander
 */public class CameraFactory {

    public enum CameraType {
        TRACKING_FROM_ABOVE, TRACKING_FROM_CENTER, TRACKING_FROM_BELOW, 
        STILL_FROM_ABOVE, STILL_FROM_STRAIGHT_ABOVE, STILL_FROM_GROUND, 
        INSIDE_COACH, LEFT_OF_COACH, RIGHT_OF_COACH, BEHIND_COACH,
        MOVING1
    }

    public static <Vector> CameraTransform<Vector> buildCamera(CameraFactory.CameraType type,
            TrackHelper<Vector> helper) {
        System.out.println(type);
        switch (type) {
            case MOVING1:
                return new TrackingCamera<>(TrackingCamera.Position.MOVING, helper);
            case INSIDE_COACH:
                return new CoachCamera<>(CoachCamera.Position.INSIDE, helper);
            case LEFT_OF_COACH:
                return new CoachCamera<>(CoachCamera.Position.LEFT, helper);
            case RIGHT_OF_COACH:
                return new CoachCamera<>(CoachCamera.Position.RIGHT, helper);
            case BEHIND_COACH:
                return new CoachCamera<>(CoachCamera.Position.BEHIND, helper);
            case TRACKING_FROM_ABOVE:
                return new TrackingCamera<>(TrackingCamera.Position.MAX, helper);
            case TRACKING_FROM_BELOW:
                return new TrackingCamera<>(TrackingCamera.Position.MIN, helper);
            case TRACKING_FROM_CENTER:
                return new TrackingCamera<>(TrackingCamera.Position.MEAN, helper);
            case STILL_FROM_ABOVE:
                return new StillCamera<>(StillCamera.Position.MAX, helper);
            case STILL_FROM_GROUND:
                return new StillCamera<>(StillCamera.Position.MIN, helper);
            case STILL_FROM_STRAIGHT_ABOVE:
                return new StillCamera<>(StillCamera.Position.MEAN, helper);
            default:
                throw new RuntimeException("unknown camera type");
        }
    }
    
}
