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
package de.tubs.wire.graphics.java3d;

import de.tubs.wire.graphics.ViewController;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import de.tubs.wire.graphics.camera.CameraFactory;
import de.tubs.wire.graphics.camera.CameraView;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.graphics.camera.Camera;
import de.tubs.wire.keyboard.AWTKeyProcessor;

/**
 *
 * @author ezander
 */
public class Java3dObserverSimple extends Java3dObserverBase implements ViewController {

    protected SimpleUniverse universe;
    protected Canvas3D canvas;
    protected TransformGroup glCamera;

    int camNum = -1;
    Camera<Vector3d> camera;
    protected AWTKeyProcessor keyprocessor;


    public Camera<Vector3d> getCamera() {
        return camera;
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
            // could occur in between before the glCamera is initialised
            Camera<Vector3d> newCamera = CameraFactory.buildCamera( camList.get(camNum), helper );
            newCamera.init(track);
            camera = newCamera;
        }
    }
    
    @Override
    public void nextCam() {
        setCamNum(getCamNum()+1);
    }
    @Override
    public void prevCam() {
        setCamNum(getCamNum()-1);
    }
    
    @Override
    public void init(TrackInformation trackInfo) {
        this.trackInfo = trackInfo;
        this.track = trackInfo.getTrack();
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
        if( keyprocessor!=null )
            keyprocessor.handleEvents(canvas);


        world = createWorld();
        glCamera = universe.getViewingPlatform().getViewPlatformTransform();

        //
        branchGroup = new BranchGroup();
        branchGroup.addChild(world);
        universe.addBranchGraph(branchGroup);
        View view = canvas.getView();
        view.setBackClipDistance(3000);
        view.setSceneAntialiasingEnable(true);
        
        setCamNum(camNum);
    }

    @Override
    public void notify(double t, double[] y) {
        if( !canvas.isOffScreen())
            canvas.stopRenderer();
        super.notify(t, y);
        
        double s = y[0];
        double dsdt = y[1];
        CameraView<Vector3d> camView = camera.getView(s, dsdt);
        Transform3D transform = new Transform3D();
        transform.lookAt(
                new Point3d(camView.getEye()), 
                new Point3d(camView.getTarget()), camView.getUp());
        transform.invert();
        glCamera.setTransform(transform);
        
        if( !canvas.isOffScreen())
            canvas.startRenderer();
    }

    public void setKeyProcessor(AWTKeyProcessor keyprocessor) {
        this.keyprocessor = keyprocessor;
    }


}
