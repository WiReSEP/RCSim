/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tubs.wire.simulator.track;

/**
 *
 * @author ezander
 */
public enum StockTracks {
    
    COLOSSOS ("/tracks/colossos.rct"),
    BIGLOOP ("/tracks/bigloop.rct"),
    TEST ("/tracks/foo.rct");
    
    private final String resourceName;

    private StockTracks(String resourceName) {
        this.resourceName = resourceName;
    }   
    
    public String getResourceName() {
        return resourceName;
    }
}
