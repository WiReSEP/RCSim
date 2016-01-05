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
package rcdemo;

import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.w3c.dom.Element;
import rcdemo.io.XMLHelper;
import rcdemo.track.Track;

/**
 *
 * @author ezander
 */
class SimulationState {
    String name;
    String author;
    String comments;
    double v0;
    Track track;

    public static SimulationState readFromXML(String filename) {
        SimulationState sim = new SimulationState();
        Element rootElement = XMLHelper.loadAndGetDocRoot(filename);
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
        
        ArrayList<Vector3D> pillarPosList = new ArrayList<>(n);
        ArrayList<Vector3D> pillarYawList = new ArrayList<>(n);
        ArrayList<Double> pillarYawAngleList = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double posX = XMLHelper.getDouble(pillars.get(i), "PosX");
            double posY = XMLHelper.getDouble(pillars.get(i), "PosY");
            double posZ = XMLHelper.getDouble(pillars.get(i), "PosZ");
            pillarPosList.add(new Vector3D(posX, posY, posZ));
            
            double yawX = XMLHelper.getDouble(pillars.get(i), "YawX");
            double yawY = XMLHelper.getDouble(pillars.get(i), "YawY");
            double yawZ = XMLHelper.getDouble(pillars.get(i), "YawZ");
            pillarYawList.add(new Vector3D(yawX, yawY, yawZ));
            
            double yawAngle = XMLHelper.getDouble(pillars.get(i), "YawAngle");
            pillarYawAngleList.add(yawAngle);
        }
        return sim;
    }
    
}
