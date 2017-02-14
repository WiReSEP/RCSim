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
package de.tubs.wire.graphics.camera;

/**
 *
 * @author ezander
 */
public class CameraView<Vector> {

    private final Vector eye;
    private final Vector target;
    private final Vector up;

    public CameraView(Vector eye, Vector target, Vector up) {
        this.eye = eye;
        this.target = target;
        this.up = up;
    }
    
    public Vector getEye() {
        return eye;
    }

    public Vector getTarget() {
        return target;
    }

    public Vector getUp() {
        return up;
    }
}
