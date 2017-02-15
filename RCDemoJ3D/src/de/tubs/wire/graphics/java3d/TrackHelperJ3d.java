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
package de.tubs.wire.graphics.java3d;

import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;
import de.tubs.wire.simulator.track.TrackHelper;

/**
 *
 * @author ezander
 */
public class TrackHelperJ3d extends TrackHelper<Vector3d> {

    public TrackHelperJ3d() {
        super(new Vector3dArithmetic());
    }

    public static TransformGroup transform(Node node, Vector3d vector) {
        return transform(node, vector, false);
    }

    public static TransformGroup transform(Node node, Vector3d vector, boolean modifyable) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(vector);
        TransformGroup tg = new TransformGroup();
        if (modifyable) {
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        }
        tg.setTransform(transform);
        tg.addChild(node);
        return tg;
    }

}
