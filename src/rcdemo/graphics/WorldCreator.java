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

import rcdemo.graphics.creators.GroundCreator;
import rcdemo.simulator.SimulationState;


public class WorldCreator<Vector, Node, Group extends Node> {
    protected TrackHelper<Vector> helper;
    protected SceneCreator<Vector, Node, Group> sc;
    protected GroundCreator<Vector, Node, Group> groundCreator;

    public WorldCreator(TrackHelper<Vector> helper, SceneCreator<Vector, Node, Group> sc) {
        this.helper = helper;
        this.sc = sc;
        this.groundCreator = new GroundCreator<>(helper, sc);
    }

    public Group createGround(SimulationState state){
        return groundCreator.createGround(state);
    }
}
