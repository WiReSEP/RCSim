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
package rcdemo.graphics.java3d;

import com.sun.j3d.utils.geometry.ColorCube;
import rcdemo.graphics.ViewController;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import rcdemo.graphics.camera.CameraTransform;
import rcdemo.graphics.camera.CameraFactory;
import rcdemo.graphics.camera.CameraView;
import rcdemo.simulator.SimulationState;

/**
 *
 * @author ezander
 */
public class Java3dObserverSimple extends Java3dObserverBase implements ViewController {

    SimpleUniverse universe;
    Canvas3D canvas;
    TransformGroup camera;

    int camNum = -1;
    CameraTransform<Vector3d> camTransform;


    public CameraTransform<Vector3d> getCamTransform() {
        return camTransform;
    }

    public void setCanvas(Canvas3D canvas) {
        assert this.canvas == null;
        this.canvas = canvas;
    }

    public Canvas3D getCanvas() {
        return canvas;
    }
    
    public int getCamNum() {
        return camNum;
    }

    public void setCamNum(int camNumNew) {
        int n = camList.size();
        camNum = ((camNumNew % n) + n) % n;
        if( track!= null ){
            // Note: this MUST be done in two steps, otherwise a screen update 
            // could occur in between before the camera is initialised
            CameraTransform<Vector3d> camTransformNew = CameraFactory.buildCamera( camList.get(camNum), helper );
            camTransformNew.init(track);
            camTransform = camTransformNew;
        }
    }
    
    public void nextCam() {
        setCamNum(getCamNum()+1);
    }
    public void prevCam() {
        setCamNum(getCamNum()-1);
    }
    
    @Override
    public void init(SimulationState state) {
        this.state = state;
        this.track = state.getTrack();
        assert (track != null);
       
        // Create the universe and add the group of objects
        if (universe != null) {
            universe.cleanup();
        }
        universe = new SimpleUniverse(canvas);
        
        if (canvas == null) {
            canvas = universe.getCanvas();
            canvas.setDoubleBufferEnable(true);
            SwingUtilities.windowForComponent(canvas).setSize(160 * 6, 90 * 6);
        }

        world = createWorld(state);
        
        camera = universe.getViewingPlatform().getViewPlatformTransform();

        //
        branchGroup = new BranchGroup();
        branchGroup.addChild(world);
        universe.addBranchGraph(branchGroup);
        View view = canvas.getView();
        view.setBackClipDistance(1000);
        view.setSceneAntialiasingEnable(true);
        
        setCamNum(camNum);
    }

    public void notify(double t, double[] y) {
        canvas.stopRenderer();
        super.notify(t, y);
        
        double s = y[0];
        double dsdt = y[1];
        CameraView<Vector3d> camView = camTransform.getTransform(s, dsdt);
        Transform3D transform = new Transform3D();
        transform.lookAt(
                new Point3d(camView.getEye()), 
                new Point3d(camView.getTarget()), camView.getUp());
        transform.invert();
        camera.setTransform(transform);
        canvas.startRenderer();
    }


}
