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

import rcdemo.graphics.camera.Camera;

/**
 *
 * @author ezander
 */
public class CameraFactory {

    public enum CameraType {
        TRACKING_FROM_ABOVE, TRACKING_FROM_CENTER, TRACKING_FROM_BELOW, 
        STILL_FROM_ABOVE, STILL_FROM_STRAIGHT_ABOVE, STILL_FROM_GROUND, 
        INSIDE_COACH, LEFT_OF_COACH, RIGHT_OF_COACH, BEHIND_COACH
    }

    public static Camera buildCamera(CameraFactory.CameraType type) {
        switch (type) {
            case INSIDE_COACH:
                return new CoachCamera(CoachCamera.Position.INSIDE);
            case LEFT_OF_COACH:
                return new CoachCamera(CoachCamera.Position.LEFT);
            case RIGHT_OF_COACH:
                return new CoachCamera(CoachCamera.Position.RIGHT);
            case BEHIND_COACH:
                return new CoachCamera(CoachCamera.Position.BEHIND);
            case TRACKING_FROM_ABOVE:
                return new TrackingCamera(TrackingCamera.Position.MAX);
            case TRACKING_FROM_BELOW:
                return new TrackingCamera(TrackingCamera.Position.MIN);
            case TRACKING_FROM_CENTER:
                return new TrackingCamera(TrackingCamera.Position.MEAN);
            case STILL_FROM_ABOVE:
                return new StillCamera(StillCamera.Position.MAX);
            case STILL_FROM_GROUND:
                return new StillCamera(StillCamera.Position.MIN);
            case STILL_FROM_STRAIGHT_ABOVE:
                return new StillCamera(StillCamera.Position.MEAN);
            default:
                throw new RuntimeException("unknown camera type");
        }
    }
    
}
