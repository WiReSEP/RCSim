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
package de.tubs.wire.keyboard;

/**
 * This class is supposed to contain information about the event that occurred.
 *
 * Currently, contains nothing but could contain stuff like event time, input
 * device, originating window, etc in the future.
 *
 * @author ezander
 */
public class EventDetails {
    
    public final KeyProcessor.SimpleKeyEvent evt;

    // currently nothing, but maybe in the future
    public EventDetails(KeyProcessor.SimpleKeyEvent evt) {
        this.evt = evt;
    }

    @Override
    public String toString() {
        return getClass().getName() + "(" + evt.toString() + ")";
    }
    
}
