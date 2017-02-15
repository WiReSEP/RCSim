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
    int camNum = 0;
    Camera<Point3D> camTransform;
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    Affine cameraTransform = new Affine();
    TrackHelperJFX helper = new TrackHelperJFX();

    public JavaFXObserverSimple(Stage primaryStage) {
        stage = primaryStage;
        root = new Group();

        Scene scene = new Scene(root, 1024, 768, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);
        scene.setFill(Color.AQUAMARINE);
        //handleKeyboard(scene, world);
        //handleMouse(scene, world);
        
        

        Group camGroup = new Group();
        camGroup.getChildren().add(camera);
        camGroup.getTransforms().add(cameraTransform);

        root.getChildren().add(camGroup);
        camera.setNearClip(0.1);
        camera.setFarClip(10000);
        scene.setCamera(camera);
        primaryStage.setScene(scene);
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
            Camera<Point3D> camTransformNew = CameraFactory.buildCamera(camList.get(camNum), helper);
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
    public void init(TrackInformation trackInfo) {
        this.trackInfo = trackInfo;
        this.track = trackInfo.getTrack();
        assert (track != null);

        world = createWorld(trackInfo);
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
        CameraView<Point3D> camView = camTransform.getView(s, dsdt);

        Transform tr = ToolkitJFX.lookAt(camView.getEye(), camView.getTarget(), camView.getUp());
        cameraTransform.setToTransform(tr);
    }

}
