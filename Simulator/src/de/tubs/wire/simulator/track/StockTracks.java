/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tubs.wire.simulator.track;

/**
 * Enum that helps in loading tracks from the internal resources (JAR).
 * 
 * @see TrackInformation
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
    
    /**
     * Get the name of the internal resource.
     * 
     * @return The name of the internal resource.
     */
    public String getResourceName() {
        return resourceName;
    }
}
