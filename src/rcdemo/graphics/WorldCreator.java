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
import com.sun.j3d.utils.geometry.Sphere;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import rcdemo.SimulationState;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
public class WorldCreator extends TrackHelper {

    public static TransformGroup createTrack(SimulationState state) {
        Track track = state.getTrack();
        TransformGroup group = new TransformGroup();
        group.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        for (double s = 0; s < track.length(); s += 0.01) {
            Sphere sphere = new Sphere(0.5f);
            Vector3d vector = trackToWorld(track.getx(s));
            group.addChild(transform(sphere, vector));
        }
        return group;
    }

    public static TransformGroup createCar(SimulationState state) {
        Track track = state.getTrack();
        Transform3D transform = new Transform3D();
        Node node = new ColorCube(0.7);
        Vector3d vector = trackToWorld(track.getx(0));
        return transform(node, vector, true);
    }

    public static TransformGroup createGround(SimulationState state) {
        Transform3D transform = new Transform3D();
        Node node = new Box(1000, 1000, 0.00001f, null);
        RealVector v = new ArrayRealVector(new double[]{0, 0, -40});
        Vector3d vector = trackToWorld(v);
        return transform(node, vector, true);
    }

    public static TransformGroup createLight() {
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
