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
package de.tubs.wire.ui;

/**
 * Interface for functions that can handle events.
 * 
 * An instance of HandlerFunction can be registered with a KeyProcessor to 
 * handle a key event (pressed, released, typed,...). If the key event happened 
 * the HandlerFunction's process method is called.
 * 
 * @author ezander
 */
public interface HandlerFunction {

    public void process(EventDetails e);
    
}
