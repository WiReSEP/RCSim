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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author ezander
 */
public class AWTKeyProcessor extends KeyProcessor implements KeyListener //, EventHandler<javafx.scene.input.SimpleKeyEvent> {
{

    void processAWTEvent(KeyEvent e) {
        final char keyChar = e.getKeyChar();
        final int keyCode = e.getKeyCode();
        final int eventType = e.getID(); // 
        
        SimpleKeyEvent evt = new SimpleKeyEvent(keyChar, keyCode, eventType);
        super.processKeyEvent(evt);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        processAWTEvent(e);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        processAWTEvent(e);
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        processAWTEvent(e);
    }

}
