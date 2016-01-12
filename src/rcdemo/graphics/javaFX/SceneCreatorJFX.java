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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Sphere;
import rcdemo.graphics.SceneCreator;

/**
 *
 * @author ezander
 */
class SceneCreatorJFX extends SceneCreator<Point3D, Node, Group> {

    @Override
    public Group translate(Node node, Point3D vector) {
        return translate(node, vector, false);
    }

    @Override
    public Group translate(Node node, Point3D vector, boolean modifiable) {
        Group group = new Group(node);
        group.setTranslateX(vector.getX());
        group.setTranslateY(vector.getY());
        group.setTranslateZ(vector.getZ());
        return group;
    }

    @Override
    public Group add(Group group, Node node) {
        group.getChildren().add(node);
        return group;
    }

    @Override
    public Group newGroup() {
        return new Group();
    }

    @Override
    public Node createSphere(double d) {
        return new Sphere((float) d);
    }
}
