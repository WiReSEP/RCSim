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
package rcdemo.graphics.javaFX;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Translate;
import de.tubs.wire.graphics.TrackHelper;

/**
 *
 * @author ezander
 */
public class TrackHelperJFX extends TrackHelper<Point3D> {

    public TrackHelperJFX() {
        super(new Point3DArithmetic());
    }
    
    public Node transform(Node node, Point3D p) {
        Translate t = new Translate(p.getX(), p.getY(), p.getZ());
        node.getTransforms().add(t);
        return node;
    }

    public Node transform(Node node, Point3D p, boolean mod) {
        Translate t = new Translate(p.getX(), p.getY(), p.getZ());
        node.getTransforms().add(t);
        return node;
    }
}
