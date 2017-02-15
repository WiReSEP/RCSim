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

import de.tubs.wire.simulator.track.Track;

/**
 * A Camera defines for each position on the track the camera view (consisting 
 * of the position of the camera, the target position and the yaw (up) vector).
 * 
 * @author ezander
 * @param <Vector> The vector type used.
 */
public interface Camera<Vector> {
    /**
     * Initialise the camera for a new track (if necessary).
     * 
     * @param track The track.
     */
    void init(Track track);
    
    /**
     * Get the camera view for the current position and velocity of the coach.
     * 
     * @param s Position on the track.
     * @param dsdt Velocity on the track.
     * @return The camera view.
     */
    CameraView<Vector> getView(double s, double dsdt);
}
