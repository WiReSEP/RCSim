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
import java.util.HashMap;
import java.util.Map;
import javafx.event.EventHandler;

/**
 *
 * @author ezander
 */
public class KeyProcessor implements KeyListener, EventHandler<javafx.scene.input.KeyEvent> {


    static class KeyEventMap {

        Map<Character, HandlerFunction> charToFunction = new HashMap<>();
        Map<Integer, HandlerFunction> keycodeToFunction = new HashMap<>();

        void add(char c, HandlerFunction func) {
            if (func != null) {
                charToFunction.put(c, func);
            }
        }

        void add(int i, HandlerFunction func) {
            if (func != null) {
                keycodeToFunction.put(i, func);
            }
        }

        void processEvent(KeyEvent e) {
            HandlerFunction function = null;
            if (keycodeToFunction.containsKey(e.getKeyCode())) {
                function = keycodeToFunction.get(e.getKeyCode());
            } else if (charToFunction.containsKey(e.getKeyChar())) {
                function = charToFunction.get(e.getKeyChar());
            }
            if (function != null) {
                EventDetails details = new EventDetails();
                // maybe fill with details from e
                function.process(details);
            }
        }

        void processEvent(javafx.scene.input.KeyEvent t) {
            int code = t.getCode().ordinal();
            char ch = t.getCharacter().charAt(0);
            System.out.println(t.getCharacter());
            System.out.println(t.getCode());
            System.out.println(code);
            
            HandlerFunction function = null;
            if (keycodeToFunction.containsKey(code)) {
                function = keycodeToFunction.get(code);
            } else if (charToFunction.containsKey(ch)) {
                function = charToFunction.get(ch);
            }
            if (function != null) {
                EventDetails details = new EventDetails();
                // maybe fill with details from t
                function.process(details);
            }
        }
    }
    KeyProcessor.KeyEventMap typedKeyFunctions = new KeyProcessor.KeyEventMap();
    KeyProcessor.KeyEventMap pressedKeyFunctions = new KeyProcessor.KeyEventMap();
    KeyProcessor.KeyEventMap releasedKeyFunctions = new KeyProcessor.KeyEventMap();

    public KeyProcessor add(char c, HandlerFunction typedFunc) {
        typedKeyFunctions.add(c, typedFunc);
        return this;
    }

//    public KeyProcessor add(char c, HandlerFunction pressedFunc, HandlerFunction releasedFunc) {
//        return add(c, null, pressedFunc, releasedFunc);
//    }
//
//    public KeyProcessor add(char c, HandlerFunction typedFunc, HandlerFunction pressedFunc, HandlerFunction releasedFunc) {
//        typedKeyFunctions.add(c, typedFunc);
//        pressedKeyFunctions.add(c, pressedFunc);
//        releasedKeyFunctions.add(c, releasedFunc);
//        return this;
//    }

//    public KeyProcessor add(int i, HandlerFunction typedFunc) {
//        return add(i, typedFunc, null, null);
//    }

    public KeyProcessor add(int i, HandlerFunction pressedFunc, HandlerFunction releasedFunc) {
        pressedKeyFunctions.add(i, pressedFunc);
        releasedKeyFunctions.add(i, releasedFunc);
        return this;
    }

//    public KeyProcessor add(int i, HandlerFunction typedFunc, HandlerFunction pressedFunc, HandlerFunction releasedFunc) {
//        typedKeyFunctions.add(i, typedFunc);
//        pressedKeyFunctions.add(i, pressedFunc);
//        releasedKeyFunctions.add(i, releasedFunc);
//        return this;
//    }

    @Override
    public void keyTyped(KeyEvent e) {
        typedKeyFunctions.processEvent(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeyFunctions.processEvent(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        releasedKeyFunctions.processEvent(e);
    }

    @Override
    public void handle(javafx.scene.input.KeyEvent t) {
        typedKeyFunctions.processEvent(t);
    }
    
}
