/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tubs.wire.ui;

import java.awt.Component;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
    
    public static class KeyEventMap<T> {

        Map<T, HandlerFunction> keyToFunctionMap = new HashMap<>();

        void add(T key, HandlerFunction func) {
            if (func != null) {
                keyToFunctionMap.put(key, func);
            }
        }

        void processEvent(SimpleKeyEvent e, T key) {
            HandlerFunction function = keyToFunctionMap.get(key);
            if( function!=null ) {
                EventDetails details = new EventDetails(e);
                function.process(details);
            }
        }

    }

    private KeyEventMap<Character> typedKeyFunctions = new KeyProcessor.KeyEventMap<>();
    private KeyEventMap<Integer> pressedKeyFunctions = new KeyProcessor.KeyEventMap<>();
    private KeyEventMap<Integer> releasedKeyFunctions = new KeyProcessor.KeyEventMap<>();
    
    private Map<Character, String> typedKeyDescriptions = new TreeMap<>();
    private Map<Integer, String> pressedKeyDescriptions = new TreeMap<>();

    public void add(char c, HandlerFunction typedFunc) {
        add(c, typedFunc, (String)null);
    }

    public void add(char keyChar, HandlerFunction typedFunc, String description) {
        typedKeyFunctions.add(keyChar, typedFunc);
        if( description!=null ) {
            typedKeyDescriptions.put(keyChar, description);
        }   
    }

    public void add(int keyCode, HandlerFunction pressedFunc, HandlerFunction releasedFunc) {
        add(keyCode, pressedFunc, releasedFunc, (String)null);
    }
    
    public void add(int keyCode, HandlerFunction pressedFunc, HandlerFunction releasedFunc, String description) {
        pressedKeyFunctions.add(keyCode, pressedFunc);
        releasedKeyFunctions.add(keyCode, releasedFunc);
        if( description!=null ) {
            pressedKeyDescriptions.put(keyCode, description);
        }   
    }
    
    public void processKeyEvent(SimpleKeyEvent evt) {
        //System.err.println("de.tubs.wire.ui.KeyProcessor.processKeyEvent(evt)\n  evt=" + evt);
        
        switch(evt.getEventType()) {
            case AWTKeyEvent.KEY_TYPED:
                typedKeyFunctions.processEvent(evt, evt.getKeyChar());
                break;
            case AWTKeyEvent.KEY_PRESSED:
                pressedKeyFunctions.processEvent(evt, evt.getKeyCode());
                break;
            case AWTKeyEvent.KEY_RELEASED:
                releasedKeyFunctions.processEvent(evt, evt.getKeyCode());
                break;
            default:
                throw new IllegalArgumentException("Illegal event type constant in SimpleKeyEvent: " + evt);
        }
    }
    
    public void showHelp() {

        PrintStream out = System.err;
        out.println("Keyboard help");
        out.println("=============");
        typedKeyDescriptions.forEach((k,v)->out.println(k + " -> " + v));
        pressedKeyDescriptions.forEach((k,v)->out.println(AWTKeyEvent.getKeyText(k) + " -> " + v));
    }


}
