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
package de.tubs.wire.rcdemo;

import de.tubs.wire.keyboard.KeyProcessor;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author ezander
 */
public class KeyProcessorFX extends KeyProcessor implements EventHandler<KeyEvent> {


    public static int keycodeToInt(KeyCode code) {
        switch( code ){
            case LEFT: return AWTKeyEvent.VK_LEFT;
            case RIGHT: return AWTKeyEvent.VK_RIGHT;
            case SPACE: return AWTKeyEvent.VK_SPACE;
            case UNDEFINED: return AWTKeyEvent.VK_UNDEFINED;
            default:
                System.err.println("Unknown key code: " + code);
                return AWTKeyEvent.VK_UNDEFINED;
        }
    }
    
    @Override
    public void handle(javafx.scene.input.KeyEvent fxevt) {
        int eventType = 0;
        long when = 0;
        int modifiers = 0;
        int keyCode = keycodeToInt(fxevt.getCode());
        char keyChar = fxevt.getCharacter().charAt(0); 
        //int keyLocation = AWTKeyEvent.KEY_LOCATION_STANDARD; //?? cant distiguish lctrl and rctrl
        
        if(fxevt.getEventType().equals(KeyEvent.KEY_TYPED)) eventType = AWTKeyEvent.KEY_TYPED;
        if(fxevt.getEventType().equals(KeyEvent.KEY_PRESSED)) eventType = AWTKeyEvent.KEY_PRESSED;
        if(fxevt.getEventType().equals(KeyEvent.KEY_RELEASED)) eventType = AWTKeyEvent.KEY_RELEASED;
                
        if( fxevt.isAltDown() ) modifiers |= AWTKeyEvent.ALT_DOWN_MASK | AWTKeyEvent.ALT_MASK;
        if( fxevt.isControlDown() ) modifiers |= AWTKeyEvent.CTRL_DOWN_MASK | AWTKeyEvent.CTRL_MASK;
        // Shift, Meta, Shortcut?
        
        SimpleKeyEvent evt = new SimpleKeyEvent(keyChar, keyCode, eventType);
        super.processKeyEvent(evt);
    }
    

    public void handleSceneEvents(Scene scene){
        scene.setOnKeyTyped(this);
        scene.setOnKeyPressed(this);
        scene.setOnKeyReleased(this);
    }
    
}
