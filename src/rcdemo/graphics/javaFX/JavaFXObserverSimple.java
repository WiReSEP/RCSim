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

import javafx.animation.Animation;
import javafx.animation.Transition;
import rcdemo.graphics.ViewController;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.MatrixType;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.media.j3d.Transform3D;
import javax.vecmath.Matrix4d;
import rcdemo.graphics.camera.CameraTransform;
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
    CameraTransform camTransform;
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    Affine cameraTransform = new Affine();

    public JavaFXObserverSimple(Stage primaryStage) {
        stage = primaryStage;
        //buildScene();
        //buildCamera();
        //buildAxes();
        //buildMolecule();

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
        //camera.setTranslateZ(-450); //cameraDistance);
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
            CameraTransform camTransformNew = CameraFactory.buildCamera(camList.get(camNum));
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
        setCamNum(5);
    }

    @Override
    public void notify(double t, double[] y) {
        super.notify(t, y);

        double s = y[0];
        double dsdt = y[1];
        Transform3D transform = camTransform.getTransform(track, s, dsdt);
        transform.invert();

        Transform tr;
        double d[] = new double[16];
        transform.get(d);
        double dd[] = track.getx(s).toArray();
        System.out.println(s);
        cameraTransform.setToTransform(d, MatrixType.MT_3D_4x4, 0);
    }

}
