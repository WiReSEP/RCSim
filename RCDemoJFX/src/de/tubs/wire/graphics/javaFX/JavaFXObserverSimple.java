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
package de.tubs.wire.graphics.javaFX;

import javafx.geometry.Point3D;
import de.tubs.wire.graphics.ViewController;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import de.tubs.wire.graphics.camera.CameraFactory;
import de.tubs.wire.graphics.camera.CameraView;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.graphics.camera.Camera;

/**
 *
 * @author ezander
 */
public class JavaFXObserverSimple extends JavaFXObserverBase implements ViewController {

    Stage stage;
    Group root;
    final PerspectiveCamera fxCamera = new PerspectiveCamera(true);
    final Affine cameraTransform = new Affine();

    int camNum = 0;
    Camera<Point3D> camera;
    TrackHelperJFX helper = new TrackHelperJFX();

    public JavaFXObserverSimple(Stage primaryStage) {
        // Save the state and creat a new root group (for the scene graph)
        stage = primaryStage;
        root = new Group();

        // Create a new scene for the scene graph (root)
        Scene scene = new Scene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        scene.setFill(Color.AQUAMARINE);

        // For the FX Camera stuff we need to setup a group with a camera and 
        // put an affine transform into it (which will be later used for the 
        // updating the camera view).
        Group cameraGroup = new Group();
        cameraGroup.getChildren().add(fxCamera);
        cameraGroup.getTransforms().add(cameraTransform);
        root.getChildren().add(cameraGroup);
        
        // Set near and far clip and put camera into scene
        fxCamera.setNearClip(0.1);
        fxCamera.setFarClip(10000);
        scene.setCamera(fxCamera);
        
        // Set the scene into the primary stage
        primaryStage.setScene(scene);
    }

    public Camera getCamera() {
        return camera;
    }

    public int getCamNum() {
        return camNum;
    }

    public void setCamNum(int camNumNew) {
        int n = camList.size();
        camNum = ((camNumNew % n) + n) % n;
        if (track != null) {
            // Note: this MUST be done in two steps, otherwise a screen update 
            // could occur in between before the fxCamera is initialised
            Camera<Point3D> newCamera = CameraFactory.buildCamera(camList.get(camNum), helper);
            newCamera.init(track);
            camera = newCamera;
        }
    }

    @Override
    public void nextCam() {
        setCamNum(getCamNum() + 1);
    }

    @Override
    public void prevCam() {
        setCamNum(getCamNum() - 1);
    }

    @Override
    public void init(TrackInformation trackInfo) {
        this.trackInfo = trackInfo;
        this.track = trackInfo.getTrack();
        assert (track != null);

        world = createWorld();
        root.getChildren().clear();
        root.getChildren().add(world);

        setCamNum(camNum);
        setCamNum(-1);
    }

    @Override
    public void notify(double t, double[] y) {
        super.notify(t, y);

        double s = y[0];
        double dsdt = y[1];
        CameraView<Point3D> camView = camera.getView(s, dsdt);

        Transform newCameraTransform = ToolkitJFX.lookAt(camView.getEye(), camView.getTarget(), camView.getUp());
        cameraTransform.setToTransform(newCameraTransform);
    }

}
