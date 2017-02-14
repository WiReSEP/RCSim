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
package de.tubs.wire.simulator.track;

import java.util.ArrayList;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.w3c.dom.Element;
import de.tubs.wire.simulator.io.XMLHelper;
import de.tubs.wire.simulator.math.ClosedHermiteSpline;

/**
 * Contains information about a track.
 * 
 * This includes name of the track, author, some comments, the starting velocity 
 * and of course a track representation.
 * 
 * @author ezander
 */
public class TrackInformation {
    private String name;
    private String author;
    private String comments;
    private double v0;
    private Track track;

    /**
     * Get the name of the track.
     * 
     * @return The name of the track.
     */
    public String getName() {
        return name;
    }

    /**
     * Get author of the track, i.e. the creator of the XML file.
     * 
     * @return Name of the author.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Get comments about the track.
     * 
     * @return Comments about the track, if any.
     */
    public String getComments() {
        return comments;
    }

    /**
     * Get intial velocity.
     * 
     * @return Initial velocity in direction track.getDxDs().
     */
    public double getV0() {
        return v0;
    }

    /**
     * Set initial velocity at starting point of the track.
     * 
     * This is a scalar in direction track.getDxDs().
     * 
     * @param v0 Initial velocity.
     */
    public void setV0(double v0) {
        this.v0 = v0;
    }

    /**
     * Return the track object containing the mathematical description.
     * 
     * @return The Track object.
     */
    public Track getTrack() {
        return track;
    }
    
    /**
     * Load a track from an XML file.
     * 
     * @param filename Name of the XML file (e.g. colossus.rct).
     * @return A TrackInformation object.
     */
    protected static TrackInformation readFromXML(String filename, boolean fromJar) {
        TrackInformation sim = new TrackInformation();
        Element rootElement = XMLHelper.loadAndGetDocRoot(filename, fromJar);
        Element trackElement = XMLHelper.getChildByName(rootElement, "Track");
        
        Element general = XMLHelper.getChildByName(trackElement, "General");
        sim.name = XMLHelper.getString(general, "Name");
        sim.author = XMLHelper.getString(general, "Author");
        sim.comments = XMLHelper.getString(general, "Comment");
        
        Element parameterElement = XMLHelper.getChildByName(trackElement, "SimulationParameters");
        sim.v0 = XMLHelper.getDouble(parameterElement, "Speed");
        double scale = XMLHelper.getDouble(parameterElement, "Scale");
        
        Element pillarListElement = XMLHelper.getChildByName(trackElement, "PillarList");
        ArrayList<Element> pillars = XMLHelper.getChildrenByName(pillarListElement, "Pillar");
        int n = pillars.size();
        
        ArrayRealVector posXVector = new ArrayRealVector(n);
        ArrayRealVector posYVector = new ArrayRealVector(n);
        ArrayRealVector posZVector = new ArrayRealVector(n);
        ArrayRealVector yawXVector = new ArrayRealVector(n);
        ArrayRealVector yawYVector = new ArrayRealVector(n);
        ArrayRealVector yawZVector = new ArrayRealVector(n);
        ArrayRealVector yawAngleVector = new ArrayRealVector(n);
        
        for (int i = 0; i < n; i++) {
            posXVector.setEntry(i, XMLHelper.getDouble(pillars.get(i), "PosX"));
            posYVector.setEntry(i, XMLHelper.getDouble(pillars.get(i), "PosY"));
            posZVector.setEntry(i, XMLHelper.getDouble(pillars.get(i), "PosZ"));
            
            yawXVector.setEntry(i, XMLHelper.getDouble(pillars.get(i), "YawX"));
            yawYVector.setEntry(i, XMLHelper.getDouble(pillars.get(i), "YawY"));
            yawZVector.setEntry(i, XMLHelper.getDouble(pillars.get(i), "YawZ"));
            
            yawAngleVector.setEntry(i, XMLHelper.getDouble(pillars.get(i), "YawAngle"));
        }
        
        sim.track = new SplineTrack(
                new ClosedHermiteSpline(posXVector),
                new ClosedHermiteSpline(posYVector),
                new ClosedHermiteSpline(posZVector),
                new ClosedHermiteSpline(yawXVector),
                new ClosedHermiteSpline(yawYVector),
                new ClosedHermiteSpline(yawZVector),
                new ClosedHermiteSpline(yawAngleVector));
                
        return sim;
    }
    
    public static TrackInformation readFromXML(String filename) {
        return readFromXML(filename, false);
    }
    
    public static TrackInformation readFromXML(StockTracks st) {
        return readFromXML(st.getResourceName(), true);
    }
    
}

