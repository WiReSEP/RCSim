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

import de.tubs.wire.graphics.WorldCreator;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Light;
import javax.media.j3d.Material;
import javax.media.j3d.Node;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import de.tubs.wire.simulator.math.RHS;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.simulator.track.Track;

/**
 *
 * @author ezander
 */
public class WorldCreatorJ3d 
extends WorldCreator<Vector3d, Node, TransformGroup> {

    @Override
    public TransformGroup createGround(TrackInformation trackInfo) {
        TransformGroup group = new TransformGroup();
        //group.addChild(super.createGround(trackInfo));
        group.addChild(new CheckeredPlane(CheckeredPlane.COLORING_RANDOM));
        //group.addChild(new FractalMountains());
        return group;
    }


    public WorldCreatorJ3d() {
        super(new TrackHelperJ3d(), new ToolkitJ3d());
    }
    
    TransformGroup makeCylinder(
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
        material.setAmbientColor(new Color3f(0.8f, 0.8f, 0.98f));
        material.setDiffuseColor(new Color3f(0.8f, 0.8f, 0.98f));
        material.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f));
        material.setShininess(10);
        //material.
        
        appearance.setMaterial(material);
        shape.setAppearance(appearance);
        return tg;
    }
    
    //public Node makeCylinder
    public Node getRailCylinders(Track track, double s0, double s1){
        double s = 0.5 * (s0 + s1);
        TransformGroup group = new TransformGroup();
        Vector3d pos0 = helper.getPosition(track, s0);
        Vector3d pos1 = helper.getPosition(track, s1);
        RHS<Vector3d> rhs0= helper.getRHS(track, s0);
        RHS<Vector3d> rhs1= helper.getRHS(track, s1);

        TransformGroup tg = makeCylinder(pos0, rhs0, pos1, rhs1, rail_radius, rail_dist, 0);
        group.addChild(tg);
        
        tg = makeCylinder(pos0, rhs0, pos1, rhs1, rail_radius, -rail_dist, 0);
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

        TransformGroup tg = makeCylinder(pos0, rhs0, pos1, rhs1, center_radius, 0, -center_dist);
        group.addChild(tg);

        return group;
    }
    
    public void makeRailPiece(Track track, double s0, double s1, TransformGroup group) {
        Vector3d f0 = helper.getForward(track, s0);
        Vector3d f1 = helper.getForward(track, s1);
        double cosAlpha = helper.vecmath.dotProduct(f0, f1);
        double alpha = Math.acos(cosAlpha) * 180 / Math.PI;
        double middist = 0.5 * alpha / 180 * Math.PI * (s1 - s0);
        if (alpha<0.5 && middist < 0.01){
            //System.out.println("Alpha:" + alpha + "  middist: " + middist + "  ds: " + (s1-s0));
            group.addChild(getRailCylinders(track, s0, s1));
        }
        else {
            makeRailPiece(track, s0, (s0+s1)/2, group);
            makeRailPiece(track, (s0+s1)/2, s1, group);
        }
    }
    
    @Override
    public Node getRailPiece(Track track, double s0, double s1) {
        TransformGroup group = new TransformGroup();
        makeRailPiece(track, s0, s1, group);
        return group;
    }

    

    @Override
    public TransformGroup createLight(TrackInformation trackInfo) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);
        
        Color3f sunColor = new Color3f(0.9f, 0.9f, 0.6f); // white light
        //Vector3d light1Direction = new Vector3d(4.0, -7.0, -12.0);
        Vector3d light1Direction = new Vector3d(.0, .0, -1.0);
        Light light = new DirectionalLight(sunColor, new Vector3f(light1Direction));
        light.setInfluencingBounds(bounds);
        
        //Color3f lightColorGreen = new Color3f(.1f, 1.4f, .1f); // green light
        Color3f ambientColor = new Color3f(.2f, .2f, .2f);
        Light ambLight = new AmbientLight(ambientColor);
        ambLight.setInfluencingBounds(bounds);
        
        TransformGroup group = new TransformGroup();
        group.addChild(light);
        group.addChild(ambLight);
        return group;
    }
}
