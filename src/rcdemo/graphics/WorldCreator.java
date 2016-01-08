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
package rcdemo.graphics;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import rcdemo.simulator.SimulationState;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public class WorldCreator extends TrackHelper {

    
//    public connectWithCylinder(Track track, double s0, double s1) {
//        
//    }
    static final float rail_radius = 0.5f / 3;
    static final float center_radius = 0.5f / 10;
    static final float rail_dist = 0.4f;
    static final float center_dist = 0.15f;
    
    public Node getRailBalls(Track track, double s0, double s1){
        double s = 0.5 * (s0 + s1);
        TransformGroup group = new TransformGroup();
        Sphere sphere;
        Vector3d pos = getPosition(track, s);
        Vector3d rhs[] = getRHS(track, s);
        Vector3d vector;

        sphere = new Sphere(rail_radius);
        vector = getShiftedPos(pos, rhs, 0, rail_dist, 0);
        group.addChild(transform(sphere, vector));

        sphere = new Sphere(rail_radius);
        vector = getShiftedPos(pos, rhs, 0, -rail_dist, 0);
        group.addChild(transform(sphere, vector));
        return group;
    }
    
    public Node getCenterBalls(Track track, double s0, double s1){
        double s = 0.5 * (s0 + s1);
        TransformGroup group = new TransformGroup();
        Sphere sphere;
        Vector3d pos = getPosition(track, s);
        Vector3d rhs[] = getRHS(track, s);
        Vector3d vector;

        sphere = new Sphere(center_radius);
        vector = getShiftedPos(pos, rhs, 0, 0, -center_dist);
        group.addChild(transform(sphere, vector));
        
        return group;
    }


    TransformGroup makeCylinder(Vector3d pos0, Vector3d[] rhs0, Vector3d pos1, Vector3d[] rhs1, 
            double radius, double dist, double zdist) {
        Cylinder cylinder;
        Vector3d v0, v1;
        v0 = getShiftedPos(pos0, rhs0, 0, dist, zdist);
        v1 = getShiftedPos(pos1, rhs1, 0, dist, zdist);
        double l = new Point3d(v0).distance(new Point3d(v1));
        Vector3d m = addScaled(v0,  0.5, v1, 0.5);
        Vector3d d = addScaled(v0, -1.0, v1, 1.0);
        cylinder = new Cylinder((float)(radius), 1.0f);
        Transform3D trans = new Transform3D();
        trans.setTranslation(m);
        Matrix3d mat = new Matrix3d();
        mat.setColumn(2, rhs0[2]);
        mat.setColumn(0, rhs0[1]);
        mat.setColumn(1, d);
        trans.setRotation(mat);
        TransformGroup tg = new TransformGroup();
        tg.setTransform(trans);
        tg.addChild(cylinder);
        return tg;
    }
    
    //public Node makeCylinder
    public Node getRailCylinders(Track track, double s0, double s1){
        double s = 0.5 * (s0 + s1);
        TransformGroup group = new TransformGroup();
        Cylinder cylinder;
        Vector3d pos0 = getPosition(track, s0);
        Vector3d pos1 = getPosition(track, s1);
        Vector3d rhs0[] = getRHS(track, s0);
        Vector3d rhs1[] = getRHS(track, s1);

        TransformGroup tg = makeCylinder(pos0, rhs0, pos1, rhs1, rail_radius, rail_dist, 0);
        group.addChild(tg);
        
        tg = makeCylinder(pos0, rhs0, pos1, rhs1, rail_radius, -rail_dist, 0);
        group.addChild(tg);

        return group;
    }

    public Node getCenterCylinders(Track track, double s0, double s1){
        double s = 0.5 * (s0 + s1);
        TransformGroup group = new TransformGroup();
        Cylinder cylinder;
        Vector3d pos0 = getPosition(track, s0);
        Vector3d pos1 = getPosition(track, s1);
        Vector3d rhs0[] = getRHS(track, s0);
        Vector3d rhs1[] = getRHS(track, s1);

        TransformGroup tg = makeCylinder(pos0, rhs0, pos1, rhs1, center_radius, 0, -center_dist);
        group.addChild(tg);

        return group;
    }
    
    public TransformGroup createTrack(SimulationState state) {
        Track track = state.getTrack();
        TransformGroup group = new TransformGroup();
        double ds = 0.01; //0.01;
        double dds = ds / 10;
        for (double s = 0; s < track.length(); s += ds) {
            Node node;
            //node = getRailBalls(track, s, s + ds);
            node = getRailCylinders(track, s - dds, s + ds + dds);
            group.addChild(node);
            
            //node = getCenterCylinders(track, s - dds, s + ds + dds);
            node = getCenterBalls(track, s - dds, s + ds + dds);
            group.addChild(node);
        }
        return group;
    }

    
    public TransformGroup createCar(SimulationState state) {
        Track track = state.getTrack();
        
        Transform3D transform = new Transform3D();
        Node node = new ColorCube(0.7);
        transform.setScale(new Vector3d(2, 0.6, 1));
        TransformGroup tg = new TransformGroup();
        tg.setTransform(transform);
        tg.addChild(node);
        node = tg;
        
        Vector3d vector = toVector3d(track.getx(0));
        return transform(node, vector, true);
    }

    public TransformGroup createGround(SimulationState state) {
        // see here: http://www.javaworld.com/article/2076745/learn-java/3d-graphic-java--render-fractal-landscapes.html
        Transform3D transform = new Transform3D();
        Node node = new Box(1000, 1000, 0.00001f, null);
        RealVector v = new ArrayRealVector(new double[]{0, 0, -40});
        Vector3d vector = toVector3d(v);
        return transform(node, vector, true);
    }

    public TransformGroup createLight() {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
        Color3f lightColorGreen = new Color3f(.1f, 1.4f, .1f); // green light
        Color3f light1Color = new Color3f(.9f, .9f, .9f); // white light
        Vector3d light1Direction = new Vector3d(4.0, -7.0, -12.0);
        DirectionalLight light = new DirectionalLight(light1Color, new Vector3f(light1Direction));
        light.setInfluencingBounds(bounds);
        AmbientLight ambLight = new AmbientLight(lightColorGreen);
        ambLight.setInfluencingBounds(bounds);
        TransformGroup group = new TransformGroup();
        group.addChild(light);
        group.addChild(ambLight);
        return group;
    }
}
