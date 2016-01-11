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
package rcdemo.graphics.javaFX;

import rcdemo.graphics.ViewController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.media.j3d.Transform3D;
import rcdemo.graphics.camera.Camera;
import rcdemo.graphics.camera.CameraFactory;
import rcdemo.simulator.SimulationState;

/**
 *
 * @author ezander
 */
public class JavaFXObserverSimple extends JavaFXObserverBase implements ViewController {

    Stage stage;
    Group root;
    int camNum = 0;
    Camera camTransform;
    
    public JavaFXObserverSimple(Stage primaryStage) {
        stage = primaryStage;
        //buildScene();
        //buildCamera();
        //buildAxes();
        //buildMolecule();

        root = new Group();

        //Scene scene = new Scene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
        Scene scene = new Scene(root, 1024, 768, true, SceneAntialiasing.DISABLED);
        scene.setFill(Color.GREY);
        //handleKeyboard(scene, world);
        //handleMouse(scene, world);

        primaryStage.setTitle("Rollercoaster Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

        //scene.setCamera(camera);
    }

    public Camera getCamTransform() {
        return camTransform;
    }

    public int getCamNum() {
        return camNum;
    }

    public void setCamNum(int camNumNew) {
        int n = camList.size();
        camNum = ((camNumNew % n) + n) % n;
        if (track != null) {
            // Note: this MUST be done in two steps, otherwise a screen update 
            // could occur in between before the camera is initialised
            Camera camTransformNew = CameraFactory.buildCamera(camList.get(camNum));
            camTransformNew.init(track);
            camTransform = camTransformNew;
        }
    }

    public void nextCam() {
        setCamNum(getCamNum() + 1);
    }

    public void prevCam() {
        setCamNum(getCamNum() - 1);
    }

    @Override
    public void init(SimulationState state) {
        this.state = state;
        this.track = state.getTrack();
        assert (track != null);

        world = createWorld(state);
        root.getChildren().add(world);
        //stage.ad

//        camera = universe.getViewingPlatform().getViewPlatformTransform();
//
//        //
//        branchGroup = new BranchGroup();
//        branchGroup.addChild(world);
//        universe.addBranchGraph(branchGroup);
//        View view = canvas.getView();
//        view.setBackClipDistance(1000);
//        view.setSceneAntialiasingEnable(true);

        setCamNum(camNum);
    }

    public void notify(double t, double[] y) {
        super.notify(t, y);

        double s = y[0];
        double dsdt = y[1];
        Transform3D transform = camTransform.getTransform(track, s, dsdt);
        transform.invert();
//        camera.setTransform(transform);
    }

}
