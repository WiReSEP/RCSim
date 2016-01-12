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
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import rcdemo.graphics.RHS;
import rcdemo.simulator.SimulationState;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public class WorldCreator extends TrackHelperJFX {

    
//    public connectWithCylinder(Track track, double s0, double s1) {
//        
//    }
    static final float rail_radius = 0.5f / 3;
    static final float center_radius = 0.5f / 10;
    static final float rail_dist = 0.4f;
    static final float center_dist = 0.15f;
    
    public Node getRailBalls(Track track, double s0, double s1){
        double s = 0.5 * (s0 + s1);
        Group group = new Group();
        Sphere sphere;
        Point3D pos = getPosition(track, s);
        RHS<Point3D> rhs = getRHS(track, s);
        Point3D vector;

        sphere = new Sphere(rail_radius, 5);
        vector = getShiftedPos(pos, rhs, 0, rail_dist, 0);
        group.getChildren().add(transform(sphere, vector));

        sphere = new Sphere(rail_radius, 5);
        vector = getShiftedPos(pos, rhs, 0, -rail_dist, 0);
        group.getChildren().add(transform(sphere, vector));
        return group;
    }
    
//    public Node getCenterBalls(Track track, double s0, double s1){
//        double s = 0.5 * (s0 + s1);
//        TransformGroup group = new TransformGroup();
//        Sphere sphere;
//        Point3D pos = getPosition(track, s);
//        Point3D rhs[] = getRHS(track, s);
//        Point3D vector;
//
//        sphere = new Sphere(center_radius);
//        vector = getShiftedPos(pos, rhs, 0, 0, -center_dist);
//        group.getChildren().add(transform(sphere, vector));
//        
//        return group;
//    }
//
//
//    TransformGroup makeCylinder(Point3D pos0, Point3D[] rhs0, Point3D pos1, Point3D[] rhs1, 
//            double radius, double dist, double zdist) {
//        Cylinder cylinder;
//        Point3D v0, v1;
//        v0 = getShiftedPos(pos0, rhs0, 0, dist, zdist);
//        v1 = getShiftedPos(pos1, rhs1, 0, dist, zdist);
//        double l = new Point3d(v0).distance(new Point3d(v1));
//        Point3D m = addScaled(v0,  0.5, v1, 0.5);
//        Point3D d = addScaled(v0, -1.0, v1, 1.0);
//        cylinder = new Cylinder((float)(radius), 1.0f);
//        Transform3D trans = new Transform3D();
//        trans.setTranslation(m);
//        Matrix3d mat = new Matrix3d();
//        mat.setColumn(2, rhs0[2]);
//        mat.setColumn(0, rhs0[1]);
//        mat.setColumn(1, d);
//        trans.setRotation(mat);
//        TransformGroup tg = new TransformGroup();
//        tg.setTransform(trans);
//        tg.getChildren().add(cylinder);
//        return tg;
//    }
//
//    TransformGroup makeCylinder2(Point3D pos0, Point3D[] rhs0, Point3D pos1, Point3D[] rhs1, 
//            double radius, double dist, double zdist) {
//        
//        int N=20;
//        QuadArray quads = new QuadArray(N*4, 
//                GeometryArray.COORDINATES | 
//                        GeometryArray.COLOR_3 | 
//                        GeometryArray.NORMALS);
//        int ind=0;
//        Color3f c = new Color3f(1.0f, 1.0f, 1.0f);
//        Point3D orig = new Point3D();
//        for(int i=0; i<N; i++){
//            double alpha0 = i * 2 * Math.PI / N;
//            double alpha1 = (i+1) * 2 * Math.PI / N;
//            double sn0 = Math.sin(alpha0);
//            double cn0 = Math.cos(alpha0);
//            double sn1 = Math.sin(alpha1);
//            double cn1 = Math.cos(alpha1);
//            double s0 = zdist+radius * Math.sin(alpha0);
//            double c0 = dist+radius * Math.cos(alpha0);
//            double s1 = zdist+radius * Math.sin(alpha1);
//            double c1 = dist+radius * Math.cos(alpha1);
//            quads.setColor(ind, c);
//            quads.setCoordinate(ind, new Point3d(getShiftedPos(pos0, rhs0, 0, c0, s0)));
//            quads.setNormal(ind, new Vector3f(getShiftedPos(orig, rhs0, 0, cn0, sn0)));
//            ind++;
//            quads.setColor(ind, c);
//            quads.setCoordinate(ind, new Point3d(getShiftedPos(pos0, rhs0, 0, c1, s1)));
//            quads.setNormal(ind, new Vector3f(getShiftedPos(orig, rhs0, 0, cn1, sn1)));
//            ind++;
//            quads.setColor(ind, c);
//            quads.setCoordinate(ind, new Point3d(getShiftedPos(pos1, rhs1, 0, c1, s1)));
//            quads.setNormal(ind, new Vector3f(getShiftedPos(orig, rhs1, 0, cn1, sn1)));
//            ind++;
//            quads.setColor(ind, c);
//            quads.setCoordinate(ind, new Point3d(getShiftedPos(pos1, rhs1, 0, c0, s0)));
//            quads.setNormal(ind, new Vector3f(getShiftedPos(orig, rhs1, 0, cn0, sn0)));
//            ind++;
//
//        }
//        TransformGroup tg = new TransformGroup();
//        Shape3D shape = new Shape3D(quads);
//        tg.getChildren().add(shape);
//        Appearance appearance = new Appearance();
//        Material material = new Material();
//        material.setLightingEnable(true);
//        //material.setEmissiveColor(new Color3f(0.6f, 0.6f, 0));
//        material.setAmbientColor(new Color3f(1.3f, 0.0f, 1));
//        material.setDiffuseColor(new Color3f(3.0f, 0.0f, 0));
//        //material.setSpecularColor(new Color3f(10.6f, 10.6f, 10));
//        //material.setShininess(3);
//        //material.
//        
//        appearance.setMaterial(material);
//        shape.setAppearance(appearance);
//        return tg;
//    }
//    
//    //public Node makeCylinder
//    public Node getRailCylinders(Track track, double s0, double s1){
//        double s = 0.5 * (s0 + s1);
//        TransformGroup group = new TransformGroup();
//        Cylinder cylinder;
//        Point3D pos0 = getPosition(track, s0);
//        Point3D pos1 = getPosition(track, s1);
//        Point3D rhs0[] = getRHS(track, s0);
//        Point3D rhs1[] = getRHS(track, s1);
//
//        TransformGroup tg = makeCylinder2(pos0, rhs0, pos1, rhs1, rail_radius, rail_dist, 0);
//        group.getChildren().add(tg);
//        
//        tg = makeCylinder2(pos0, rhs0, pos1, rhs1, rail_radius, -rail_dist, 0);
//        group.getChildren().add(tg);
//
//        return group;
//    }
//
//    public Node getCenterCylinders(Track track, double s0, double s1){
//        double s = 0.5 * (s0 + s1);
//        TransformGroup group = new TransformGroup();
//        Cylinder cylinder;
//        Point3D pos0 = getPosition(track, s0);
//        Point3D pos1 = getPosition(track, s1);
//        Point3D rhs0[] = getRHS(track, s0);
//        Point3D rhs1[] = getRHS(track, s1);
//
//        //TransformGroup tg = makeCylinder(pos0, rhs0, pos1, rhs1, center_radius, 0, -center_dist);
//        TransformGroup tg = makeCylinder2(pos0, rhs0, pos1, rhs1, center_radius, 0, -center_dist);
//        group.getChildren().add(tg);
//
//        return group;
//    }
    
    public Group createTrack(SimulationState state) {
        Track track = state.getTrack();
        Group group = new Group();
        double ds = 0.01; //0.01;
        for (double s = 0; s < track.length(); s += ds) {
            Node node;
            node = getRailBalls(track, s, s + ds);
            //node = getRailCylinders(track, s, s + ds);
            group.getChildren().add(node);
            

            
            //Point3D p = toPoint3D(track.getx(s));
            //p.multiply(10);
            //Node sphere = new Sphere(3.0);
            //group.getChildren().add(transform(sphere, p));
            //System.out.println(p);
            
        }
            
        ds = 0.01; //0.01;
        for (double s = 0; s < track.length(); s += ds) {
            Node node;
            //node = getCenterCylinders(track, s, s + ds);
            //node = getCenterBalls(track, s, s + ds);
            //group.getChildren().add(node);
        }
        return group;
    }

    
//    public TransformGroup createCar(SimulationState state) {
//        Track track = state.getTrack();
//        
//        Transform3D transform = new Transform3D();
//        Node node = new ColorCube(0.7);
//        transform.setScale(new Point3D(2, 0.6, 1));
//        TransformGroup tg = new TransformGroup();
//        tg.setTransform(transform);
//        tg.getChildren().add(node);
//        node = tg;
//        
//        Point3D vector = toPoint3D(track.getx(0));
//        return transform(node, vector, true);
//    }

    public Group createGround(SimulationState state) {
        // see here: http://www.javaworld.com/article/2076745/learn-java/3d-graphic-java--render-fractal-landscapes.html
//        Transform3D transform = new Transform3D();
//        Node node = new Box(1000, 1000, 0.00001f, null);
//        RealVector v = new ArrayRealVector(new double[]{0, 0, -40});
//        Point3D vector = toPoint3D(v);
//        CheckeredPlane plane = new CheckeredPlane();
//        node = plane;
//        TransformGroup t = transform(node, vector, true);
//        t = new TransformGroup();
        Group t = new Group();
        //Point3D[] stats = TrackHelperJFX.getStatistics(state.track);
        for (int i = 0; i < 1000; i++) {
            double x[] = new double[3];
            x[0] = Math.random()*2000.0;
            x[1] = Math.random()*2000.0;
            x[2] = Math.random()*1000.0-500;
            Point3D p = super.va.fromDouble(x);
            Sphere s = new Sphere(3, 20);
            s.setMaterial(new PhongMaterial(Color.CRIMSON));
            t.getChildren().add(transform(s, p));
        }
        
        return t;
    }
//
//    public TransformGroup createLight() {
//        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
//        Color3f lightColorGreen = new Color3f(.1f, 1.4f, .1f); // green light
//        Color3f light1Color = new Color3f(.9f, .9f, .9f); // white light
//        Point3D light1Direction = new Point3D(4.0, -7.0, -12.0);
//        DirectionalLight light = new DirectionalLight(light1Color, new Vector3f(light1Direction));
//        light.setInfluencingBounds(bounds);
//        AmbientLight ambLight = new AmbientLight(lightColorGreen);
//        ambLight.setInfluencingBounds(bounds);
//        TransformGroup group = new TransformGroup();
//        group.getChildren().add(light);
//        group.getChildren().add(ambLight);
//        return group;
//    }
}
