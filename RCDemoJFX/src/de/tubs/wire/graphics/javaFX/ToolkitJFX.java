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
package de.tubs.wire.graphics.javaFX;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import de.tubs.wire.graphics.RHS;
import de.tubs.wire.graphics.Toolkit;

/**
 *
 * @author ezander
 */
class ToolkitJFX extends Toolkit<Point3D, Node, Group> {

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
        return new Sphere((float) d, 32);
    }

    @Override
    public Node createColorCube() {
        return new Box();
    }

    @Override
    public Group translate(Node node, Point3D vector, boolean modifiable) {
        if (modifiable) {
            Group group = newGroup();
            add(group, node);
            group.getTransforms().add(new Translate(vector.getX(), vector.getY(), vector.getZ()));
            return group;
        } else {
            return translate(node, vector);
        }
    }

    @Override
    public Group translate(Node node, Point3D vector) {
        return translate(node, vector.getX(), vector.getY(), vector.getZ());
    }

    @Override
    public Group translate(Node node, double d1, double d2, double d3) {
        Group group = new Group(node);
        group.setTranslateX(d1);
        group.setTranslateY(d2);
        group.setTranslateZ(d3);
        return group;
    }

    @Override
    public Group scale(Node node, double d1, double d2, double d3) {
        Group group = new Group(node);
        group.setScaleX(d1);
        group.setScaleY(d2);
        group.setScaleZ(d3);
        return group;
    }

    @Override
    public Group scale(Node node, Point3D vector) {
        return scale(node, vector.getX(), vector.getY(), vector.getZ());
    }

    public static Affine createAffine(Point3D pos, Point3D xVec, Point3D yVec, Point3D zVec) {
        return new Affine(
                xVec.getX(), yVec.getX(), zVec.getX(), pos.getX(),
                xVec.getY(), yVec.getY(), zVec.getY(), pos.getY(),
                xVec.getZ(), yVec.getZ(), zVec.getZ(), pos.getZ());
    };
    
    public static Affine lookAt(Point3D from, Point3D to, Point3D upDir) {
        Point3D zVec = to.subtract(from).normalize();
        Point3D xVec = zVec.normalize().crossProduct(upDir).normalize();
        Point3D yVec = zVec.crossProduct(xVec).normalize();
        return createAffine(from, xVec, yVec, zVec);
    }

    @Override
    public void setAffineTransform(Group group, Point3D pos, RHS<Point3D> rhs) {
        Point3D xVec = rhs.getForward();
        Point3D yVec = rhs.getLeft();
        Point3D zVec = rhs.getUp();
        Affine affine = createAffine(pos, xVec, yVec, zVec);
        group.getTransforms().set(0, affine);
    }
}
