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
package rcdemo;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import rcdemo.graphics.java3d.Java3dObserverSimple;
import rcdemo.graphics.javaFX.JavaFXObserverSimple;
import rcdemo.simulator.ODESimulator;
import rcdemo.simulator.SimulationState;
import rcdemo.simulator.Simulator;
import rcdemo.ui.DefaultKeyListener;

/**
 *
 * @author ezander
 */
public class RCJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //String filename = "tracks/colossos.rct";
        String filename = "tracks/foo.rct";
        SimulationState state = SimulationState.readFromXML(filename);

        JavaFXObserverSimple observer3d = new JavaFXObserverSimple(primaryStage);
        //observer3d.setCamNum(-1);

        Simulator sim = new ODESimulator();
        sim.addObserver(observer3d);
        sim.addObserver(new Java3dObserverSimple());
        //sim.addObserver( new TextBasedObserver());
        sim.setState(state);

        observer3d.init(sim.getState());
        sim.init();

        primaryStage.setTitle("Rollercoaster Simulator");
        primaryStage.show();

        Scene scene = primaryStage.getScene();
        //scene.setOnKeyPressed(
        scene.setOnKeyTyped(
                DefaultKeyListener.getDefaultKeyListener(sim, observer3d));
//        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                System.out.println(event);
//            }
//        });

        Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(200000));
            }

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
