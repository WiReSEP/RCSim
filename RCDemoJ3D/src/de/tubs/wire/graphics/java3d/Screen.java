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
package de.tubs.wire.graphics.java3d;

import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import javax.swing.SwingUtilities;

/**
 * Utility class that helps setting the display mode to fullscreen and back.
 * 
 * @author ezander
 */
public class Screen {

    /** DisplayMode that leaves the display mode unchanged */
    public static final DisplayMode MODE_DEFAULT = null;
    
    /** DisplayMode with 800 x 600 pixels */
    public static final DisplayMode MODE_800_600_16 = new DisplayMode(800, 600, 16, DisplayMode.REFRESH_RATE_UNKNOWN);
    
    /**
     * Constructor is private as this is a utility class.
     */
    private Screen() {
    }
    
    /**
     * Get the default screen device of the local graphics environment.
     * @return The default screen device.
     */
    protected static GraphicsDevice getScreenDevice() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return env.getDefaultScreenDevice();
    }

    /**
     * Sets the given window to fullscreen.
     * 
     * @param window The window to be made full screen.
     */
    public static void setFullScreen(Window window) {
        setFullScreen(window, null);
    }
    
    /**
     * Sets the given window to fullscreen and set the display mode.
     * 
     * @param window The window to be made full screen.
     * @param dm The new display mode.
     */
    public static void setFullScreen(Window window, DisplayMode dm) {
        GraphicsDevice gd = getScreenDevice();
        //window.setUndecorated(true); // May if it's a frame
        //window.setResizable(false);
        gd.setFullScreenWindow(window);
        if (dm != null && gd.isDisplayChangeSupported()) {
            gd.setDisplayMode(dm);
        }
    }

    /**
     * Get the current full screen window (if any).
     * 
     * @return The current full screen window.
     */
    public static Window getFullScreenWindow() {
        GraphicsDevice gd = getScreenDevice();
        return gd.getFullScreenWindow();
    }

    /**
     * Restores the screen to normal (not full screen).
     */
    public static void restoreScreen() {
        GraphicsDevice gd = getScreenDevice();
        Window window = gd.getFullScreenWindow();
        if (window != null) {
            //window.dispose();
        }
        gd.setFullScreenWindow(null);
    }

    /**
     * Toggles the fullscreen mode.
     * 
     * @param window To make fullscreen, if currently not fullscreen.
     */
    public static void toggleFullScreen(Window window) {
        GraphicsDevice gd = getScreenDevice();
        if (gd.getFullScreenWindow()==null) {
            setFullScreen(window);
        }
        else {
            restoreScreen();
        }
    }

    /**
     * Toggles the fullscreen mode of the AWT component.
     * 
     * @param component Component whose parent is to go fullscreen .
     */
    public static void toggleParentFullScreen(Component component) {
        toggleFullScreen(SwingUtilities.getWindowAncestor(component));
    }
}
