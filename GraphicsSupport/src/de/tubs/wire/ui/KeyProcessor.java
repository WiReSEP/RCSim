/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tubs.wire.ui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ezander
 */
public class KeyProcessor {

    public static class SimpleKeyEvent {

        private final char keyChar;
        private final int keyCode;
        private final int eventType;

        public SimpleKeyEvent(char keyChar, int keyCode, int eventType) {
            this.keyChar = keyChar;
            this.keyCode = keyCode;
            this.eventType = eventType;
        }

        /**
         * @return the keyChar
         */
        public char getKeyChar() {
            return keyChar;
        }

        /**
         * @return the keyCode
         */
        public int getKeyCode() {
            return keyCode;
        }

        /**
         * @return the eventType
         */
        public int getEventType() {
            return eventType;
        }

        @Override
        public String toString() {
            return String.format("%s(char=%c,code=%d,type=%d)",
                    getClass().getSimpleName(), keyChar, keyCode, eventType);
        }

    }

    protected class AWTKeyEvent extends java.awt.event.KeyEvent {

        public AWTKeyEvent(Component source, int id, long when, int modifiers, int keyCode, char keyChar, int keyLocation) {
            super(null, id, when, modifiers, keyCode, keyChar, keyLocation);
        }
    }
    
    public static class KeyEventMap {

        Map<Character, HandlerFunction> charToFunctionMap = new HashMap<>();
        Map<Integer, HandlerFunction> keycodeToFunctionMap = new HashMap<>();

        void add(char c, HandlerFunction func) {
            if (func != null) {
                charToFunctionMap.put(c, func);
            }
        }

        void add(int i, HandlerFunction func) {
            if (func != null) {
                keycodeToFunctionMap.put(i, func);
            }
        }

        void processEvent(SimpleKeyEvent e) {
            HandlerFunction function = null;
            if (keycodeToFunctionMap.containsKey(e.getKeyCode())) {
                function = keycodeToFunctionMap.get(e.getKeyCode());
            } else if (charToFunctionMap.containsKey(e.getKeyChar())) {
                function = charToFunctionMap.get(e.getKeyChar());
            }
            if (function != null) {
                EventDetails details = new EventDetails(e);
                // maybe fill with details from e
                function.process(details);
            }
        }

    }

    private KeyEventMap typedKeyFunctions = new KeyProcessor.KeyEventMap();
    private KeyEventMap pressedKeyFunctions = new KeyProcessor.KeyEventMap();
    private KeyEventMap releasedKeyFunctions = new KeyProcessor.KeyEventMap();

    public void add(char c, HandlerFunction typedFunc) {
        typedKeyFunctions.add(c, typedFunc);
    }

    public void add(int i, HandlerFunction pressedFunc, HandlerFunction releasedFunc) {
        pressedKeyFunctions.add(i, pressedFunc);
        releasedKeyFunctions.add(i, releasedFunc);
    }
    
    public void processKeyEvent(SimpleKeyEvent evt) {
        System.err.println("de.tubs.wire.ui.KeyProcessor.processKeyEvent(evt)\n  evt=" + evt);
        
        switch(evt.getEventType()) {
            case AWTKeyEvent.KEY_PRESSED:
                typedKeyFunctions.processEvent(evt);
                break;
            case AWTKeyEvent.KEY_RELEASED:
                releasedKeyFunctions.processEvent(evt);
                break;
            case AWTKeyEvent.KEY_TYPED:
                typedKeyFunctions.processEvent(evt);
                break;
            default:
                throw new IllegalArgumentException("Illegal event type constant in SimpleKeyEvent: " + evt);
        }
    }
    
    public void showHelp() {
        System.err.println("Dies ist die Hilfe!!");
    }


}
