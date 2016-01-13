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

import rcdemo.simulator.SimulationState;
import rcdemo.track.Track;


public class WorldCreator<Vector, Node, Group extends Node> {
    protected TrackHelper<Vector> helper;
    protected Toolkit<Vector, Node, Group> sc;
    protected GroundCreator<Vector, Node, Group> groundCreator;

    public WorldCreator(TrackHelper<Vector> helper, Toolkit<Vector, Node, Group> sc) {
        this.helper = helper;
        this.sc = sc;
        this.groundCreator = new GroundCreator<>(helper, sc);
    }

    
    static public final float rail_radius = 0.5f / 3;
    static public final float center_radius = 0.5f / 10;
    static public final float rail_dist = 0.4f;
    static public final float center_dist = 0.15f;
    
    
    public Group createSphereAt(Group group, double d, Vector pos, RHS<Vector> rhs, 
            double fore, double right, double up) {
        if( group == null )
            group = sc.newGroup();

        Node sphere = sc.createSphere(d);
        Vector vector = helper.getShiftedPos(pos, rhs, fore, right, up);
        sc.add(group, sc.translate(sphere, vector));
        
        return group;
    }
    public Node getRailBalls(Track track, double s0, double s1){
        double s = 0.5 * (s0 + s1);
        Vector pos = helper.getPosition(track, s);
        RHS<Vector> rhs= helper.getRHS(track, s);
        
        Group group = sc.newGroup();
        createSphereAt(group, rail_radius, pos, rhs, 0, rail_dist, 0);
        createSphereAt(group, rail_radius, pos, rhs, 0, -rail_dist, 0);
        return group;
    }
    
    public Node getCenterBalls(Track track, double s0, double s1){
        double s = 0.5 * (s0 + s1);
        Vector pos = helper.getPosition(track, s);
        RHS<Vector> rhs = helper.getRHS(track, s);
        return createSphereAt(null, center_radius, pos, rhs, 0, 0, -center_dist);
    }
    
    public Node getRailPiece(Track track, double s0, double s1) {
        Group group = sc.newGroup();
        double ds = 0.01; //0.01;
        for( double s=s0; s<s1; s+=ds ) {
            sc.add(group, getRailBalls(track, s, s+ds));
        }
        return group;
    }
    
    public Group createTrack(SimulationState state) {
        Track track = state.getTrack();
        Group group = sc.newGroup();
        double ds = 0.01; //0.01;
        for (double s = 0; s < track.length(); s += 1) {
            //node = getRailCylinders(track, s, s + ds);
            sc.add(group, getRailPiece(track, s, s+1));
        }
            
        ds = 0.01; //0.01;
        for (double s = 0; s < track.length(); s += ds) {
            Node node;
            //node = getCenterCylinders(track, s, s + ds);
            sc.add(group, getCenterBalls(track, s, s + ds));
        }
        return group;
    }

    
    
    public Group createCar(SimulationState state) {
        Track track = state.getTrack();
        
        Node node = sc.createColorCube();
        Group group = sc.scale(node, 4, 1, 0.3);
        group = sc.translate(group, 0, 0, 0.2);

        Vector vector = helper.getPosition(track, 0);
        return sc.translate(group, vector, true);
    }

    public void setCarState(Group car, Vector pos, RHS<Vector> rhs) {
        sc.setAffineTransform(car, pos, rhs);
    }
    
    public Group createGround(SimulationState state){
        return groundCreator.createGround(state);
    }
    
    public Group createLight(SimulationState state){
        // empty group currently
        return sc.newGroup();
    }
}
