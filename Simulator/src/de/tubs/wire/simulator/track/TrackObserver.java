/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tubs.wire.simulator.track;

import de.tubs.wire.simulator.Observer;

/**
 *
 * @author ezander
 */
public abstract class TrackObserver implements Observer<TrackInformation> {
    
    protected TrackInformation trackInfo;

    @Override
    public void init(TrackInformation trackInfo) {
        this.trackInfo = trackInfo;
    }
    
}
