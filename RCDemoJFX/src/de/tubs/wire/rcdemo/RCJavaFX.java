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

import de.tubs.wire.graphics.DefaultKeyMapping;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import de.tubs.wire.graphics.javaFX.JavaFXObserverSimple;
import de.tubs.wire.simulator.TrackSimulator;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.simulator.track.StockTracks;

/**
 *
 * @author ezander
 */
public class RCJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        TrackInformation trackInfo = TrackInformation.readFromXML(StockTracks.TEST);

        JavaFXObserverSimple observerFX = new JavaFXObserverSimple(primaryStage);
        //observer3d.setCamNum(-1);

        TrackSimulator sim = new TrackSimulator();
        sim.addObserver(observerFX);
        //sim.addObserver( new TextBasedObserver<TrackInformation>());
        sim.setSimulationInfo(trackInfo);

        observerFX.init(sim.getSimulationInfo());
        sim.init();

        primaryStage.setTitle("Rollercoaster Simulator");
        primaryStage.show();

        Scene scene = primaryStage.getScene();
        
        KeyProcessorFX keyprocessor = new KeyProcessorFX();
        DefaultKeyMapping.setDefaultKeys(keyprocessor, sim, observerFX, false);
        keyprocessor.add('q', d->primaryStage.close(), "Quit the application.");
        keyprocessor.handleSceneEvents(scene);
        

        Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(200000));
            }

            @Override
            protected void interpolate(double frac) {
                sim.update();
            }

        };
        animation.play();

    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //System.setProperty("prism.dirtyopts", "false");
        launch(args);
    }
}
