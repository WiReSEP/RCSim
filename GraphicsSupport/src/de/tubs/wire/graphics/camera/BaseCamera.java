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

import de.tubs.wire.graphics.TrackHelper;
import de.tubs.wire.simulator.track.Track;

/**
 *
 * @author ezander
 */
public abstract class BaseCamera<Vector> implements CameraTransform<Vector> {
    protected TrackHelper<Vector> helper;
    protected Track track;

    public BaseCamera(TrackHelper<Vector> helper) {
        this(helper, null);
    }

    public BaseCamera(TrackHelper<Vector> helper, Track track) {
        this.helper = helper;
        this.track = track;
    }

    @Override
    public void init(Track track) {
        this.track = track;
    }
}
