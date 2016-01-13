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

import rcdemo.graphics.WorldCreator;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import rcdemo.graphics.RHS;
import rcdemo.simulator.SimulationState;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public class WorldCreatorJ3d 
extends WorldCreator<Vector3d, Node, TransformGroup> {

    public WorldCreatorJ3d() {
        super(new TrackHelperJ3d(), new ToolkitJ3d());
    }
    


    TransformGroup makeCylinder(
            Vector3d pos0, RHS<Vector3d> rhs0, 
            Vector3d pos1, RHS<Vector3d> rhs1, 
            double radius, double dist, double zdist) {
        Cylinder cylinder;
        Vector3d v0, v1;
        v0 = helper.getShiftedPos(pos0, rhs0, 0, dist, zdist);
        v1 = helper.getShiftedPos(pos1, rhs1, 0, dist, zdist);
        double l = new Point3d(v0).distance(new Point3d(v1));
        Vector3d m = helper.addScaled(v0,  0.5, v1, 0.5);
        Vector3d d = helper.addScaled(v0, -1.0, v1, 1.0);
        cylinder = new Cylinder((float)(radius), 1.0f);
        Transform3D trans = new Transform3D();
        trans.setTranslation(m);
        Matrix3d mat = new Matrix3d();
        mat.setColumn(2, rhs0.getUp());
        mat.setColumn(0, rhs0.getLeft());
        mat.setColumn(1, d);
        trans.setRotation(mat);
        TransformGroup tg = new TransformGroup();
        tg.setTransform(trans);
        tg.addChild(cylinder);
        return tg;
    }

    TransformGroup makeCylinder2(
            Vector3d pos0, RHS<Vector3d> rhs0, 
            Vector3d pos1, RHS<Vector3d> rhs1, 
            double radius, double dist, double zdist) {
        
        int N=20;
        QuadArray quads = new QuadArray(N*4, 
                GeometryArray.COORDINATES | 
                        GeometryArray.COLOR_3 | 
                        GeometryArray.NORMALS);
        int ind=0;
        Color3f c = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3d orig = new Vector3d();
        for(int i=0; i<N; i++){
            double alpha0 = i * 2 * Math.PI / N;
            double alpha1 = (i+1) * 2 * Math.PI / N;
            double sn0 = Math.sin(alpha0);
            double cn0 = Math.cos(alpha0);
            double sn1 = Math.sin(alpha1);
            double cn1 = Math.cos(alpha1);
            double s0 = zdist+radius * Math.sin(alpha0);
            double c0 = dist+radius * Math.cos(alpha0);
            double s1 = zdist+radius * Math.sin(alpha1);
            double c1 = dist+radius * Math.cos(alpha1);
            quads.setColor(ind, c);
            quads.setCoordinate(ind, new Point3d(helper.getShiftedPos(pos0, rhs0, 0, c0, s0)));
            quads.setNormal(ind, new Vector3f(helper.getShiftedPos(orig, rhs0, 0, cn0, sn0)));
            ind++;
            quads.setColor(ind, c);
            quads.setCoordinate(ind, new Point3d(helper.getShiftedPos(pos0, rhs0, 0, c1, s1)));
            quads.setNormal(ind, new Vector3f(helper.getShiftedPos(orig, rhs0, 0, cn1, sn1)));
            ind++;
            quads.setColor(ind, c);
            quads.setCoordinate(ind, new Point3d(helper.getShiftedPos(pos1, rhs1, 0, c1, s1)));
            quads.setNormal(ind, new Vector3f(helper.getShiftedPos(orig, rhs1, 0, cn1, sn1)));
            ind++;
            quads.setColor(ind, c);
            quads.setCoordinate(ind, new Point3d(helper.getShiftedPos(pos1, rhs1, 0, c0, s0)));
            quads.setNormal(ind, new Vector3f(helper.getShiftedPos(orig, rhs1, 0, cn0, sn0)));
            ind++;

        }
        TransformGroup tg = new TransformGroup();
        Shape3D shape = new Shape3D(quads);
        tg.addChild(shape);
        Appearance appearance = new Appearance();
        Material material = new Material();
        material.setLightingEnable(true);
        //material.setEmissiveColor(new Color3f(0.6f, 0.6f, 0));
        material.setAmbientColor(new Color3f(1.3f, 0.0f, 1));
        material.setDiffuseColor(new Color3f(3.0f, 0.0f, 0));
        //material.setSpecularColor(new Color3f(10.6f, 10.6f, 10));
        //material.setShininess(3);
        //material.
        
        appearance.setMaterial(material);
        shape.setAppearance(appearance);
        return tg;
    }
    
    //public Node makeCylinder
    public Node getRailCylinders(Track track, double s0, double s1){
        double s = 0.5 * (s0 + s1);
        TransformGroup group = new TransformGroup();
        Cylinder cylinder;
        Vector3d pos0 = helper.getPosition(track, s0);
        Vector3d pos1 = helper.getPosition(track, s1);
        RHS<Vector3d> rhs0= helper.getRHS(track, s0);
        RHS<Vector3d> rhs1= helper.getRHS(track, s1);

        TransformGroup tg = makeCylinder2(pos0, rhs0, pos1, rhs1, rail_radius, rail_dist, 0);
        group.addChild(tg);
        
        tg = makeCylinder2(pos0, rhs0, pos1, rhs1, rail_radius, -rail_dist, 0);
        group.addChild(tg);

        return group;
    }

    public Node getCenterCylinders(Track track, double s0, double s1){
        double s = 0.5 * (s0 + s1);
        TransformGroup group = new TransformGroup();
        Vector3d pos0 = helper.getPosition(track, s0);
        Vector3d pos1 = helper.getPosition(track, s1);
        RHS<Vector3d> rhs0= helper.getRHS(track, s0);
        RHS<Vector3d> rhs1= helper.getRHS(track, s1);

        //TransformGroup tg = makeCylinder(pos0, rhs0, pos1, rhs1, center_radius, 0, -center_dist);
        TransformGroup tg = makeCylinder2(pos0, rhs0, pos1, rhs1, center_radius, 0, -center_dist);
        group.addChild(tg);

        return group;
    }
    

    

    public TransformGroup createLight(SimulationState state) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);
        
        Color3f light1Color = new Color3f(.9f, .9f, .9f); // white light
        Vector3d light1Direction = new Vector3d(4.0, -7.0, -12.0);
        DirectionalLight light = new DirectionalLight(light1Color, new Vector3f(light1Direction));
        light.setInfluencingBounds(bounds);
        
        Color3f lightColorGreen = new Color3f(.1f, 1.4f, .1f); // green light
        AmbientLight ambLight = new AmbientLight(lightColorGreen);
        ambLight.setInfluencingBounds(bounds);
        
        TransformGroup group = new TransformGroup();
        group.addChild(light);
        group.addChild(ambLight);
        return group;
    }
}
