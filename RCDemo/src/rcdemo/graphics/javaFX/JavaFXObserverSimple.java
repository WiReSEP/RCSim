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

import javafx.geometry.Point3D;
import rcdemo.graphics.ViewController;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;
import rcdemo.graphics.camera.CameraTransform;
import rcdemo.graphics.camera.CameraFactory;
import rcdemo.graphics.camera.CameraView;
import de.tubs.wire.simulator.track.TrackInformation;

/**
 *
 * @author ezander
 */
public class JavaFXObserverSimple extends JavaFXObserverBase implements ViewController {

    Stage stage;
    Group root;
    int camNum = 0;
    CameraTransform<Point3D> camTransform;
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

    public CameraTransform getCamTransform() {
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
            CameraTransform<Point3D> camTransformNew = CameraFactory.buildCamera(camList.get(camNum), helper);
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
    public void init(TrackInformation state) {
        this.state = state;
        this.track = state.getTrack();
        assert (track != null);

        world = createWorld(state);
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
        CameraView<Point3D> camView = camTransform.getTransform(s, dsdt);

        Transform tr = ToolkitJFX.lookAt(camView.getEye(), camView.getTarget(), camView.getUp());
        cameraTransform.setToTransform(tr);
    }

}
