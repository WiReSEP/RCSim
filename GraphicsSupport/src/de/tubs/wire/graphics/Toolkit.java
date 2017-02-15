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
package de.tubs.wire.graphics;

import de.tubs.wire.simulator.math.RHS;

/**
 *
 * @author ezander
 */
public abstract class Toolkit<Vector, Node, Group extends Node> {

    public abstract Group newGroup();

    public abstract Group add(Group group, Node node);

    public abstract Group scale(Node node, Vector vector);
    public abstract Group translate(Node node, Vector vector);
    
    public abstract Group scale(Node node, double d1, double d2, double d3);
    public abstract Group translate(Node node, double d1, double d2, double d3);

    public abstract void setAffineTransform(Group group, Vector pos, RHS<Vector> rhs);
    public Group translate(Node node, Vector vector, boolean modifiable) {
        return translate(node, vector);
    }

    public abstract Node createSphere(double d);
    public abstract Node createColorCube();
    
}
