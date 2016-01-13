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
package rcdemo.graphics.java3d;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import rcdemo.graphics.RHS;
import rcdemo.graphics.Toolkit;

/**
 *
 * @author ezander
 */
class ToolkitJ3d extends Toolkit<Vector3d, Node, TransformGroup> {

    @Override
    public TransformGroup translate(Node node, Vector3d vector) {
        return translate(node, vector, false);
    }

    @Override
    public TransformGroup translate(Node node, Vector3d vector, boolean modifiable) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(vector);
        TransformGroup group = add(new TransformGroup(transform), node);
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        return group;
    }
    
    @Override
    public TransformGroup translate(Node node, double d1, double d2, double d3) {
        return translate(node, new Vector3d(d1, d2, d3));
    }
    
    @Override
    public TransformGroup scale(Node node, Vector3d vector) {
        Transform3D transform = new Transform3D();
        transform.setScale(vector);
        TransformGroup group = add(new TransformGroup(transform), node);
        return group;
    }
    
    @Override
    public TransformGroup scale(Node node, double d1, double d2, double d3) {
        return scale(node, new Vector3d(d1, d2, d3));
    }
    

    @Override
    public TransformGroup add(TransformGroup group, Node node) {
        group.addChild(node);
        return group;
    }

    @Override
    public TransformGroup newGroup() {
        return new TransformGroup();
    }

    @Override
    public Node createSphere(double d) {
        return new Sphere((float) d);
    }

    public static void showTransformPath(Node n) {
        Transform3D trans = new Transform3D();
        while( n != null ) {
            System.out.println(n);
            if (n instanceof TransformGroup) {
                TransformGroup t = (TransformGroup)n;
                Transform3D t2 = new Transform3D();
                t.getTransform(t2);
                trans.mul(t2, trans);
            }
            System.out.println(trans);
            n=n.getParent();
        }
    }


    @Override
    public Node createColorCube() {
        return new ColorCube(1);
    }

    @Override
    public void setAffineTransform(TransformGroup group, Vector3d pos, RHS<Vector3d> rhs) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(pos);
        Matrix3d rot = new Matrix3d();
        rot.setColumn(0, rhs.getForward());
        rot.setColumn(1, rhs.getLeft());
        rot.setColumn(2, rhs.getUp());
        transform.setRotation(rot);
        group.setTransform(transform);
    }
    
}
