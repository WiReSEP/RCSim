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
import java.util.ArrayList;
import java.util.List;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import de.tubs.wire.graphics.camera.CameraFactory;
import de.tubs.wire.simulator.track.TrackInformation;
import de.tubs.wire.graphics.camera.Camera;
import de.tubs.wire.graphics.camera.CameraView;
import de.tubs.wire.keyboard.AWTKeyProcessor;
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


/**
 *
 * @author ezander
 */
public class Java3dObserverMulti extends Java3dObserverBase {
    
    VirtualUniverse universe;
    List<MyView> views = new ArrayList<>();

    public class MyView implements ViewController {

        Canvas3D canvas;
        TransformGroup camera;
        int camNum = 0;
        Camera camTransform;
        BranchGroup viewBranch;
        
        public MyView(Canvas3D canvas) {
            this.canvas = canvas;
        }
        
        public Camera getCamTransform() {
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
            if (track != null) {
                camTransform = CameraFactory.buildCamera(camList.get(camNum), helper);
                camTransform.init(track);
            }
        }
        
        public void nextCam() {
            setCamNum(getCamNum() + 1);
        }
        
        public void prevCam() {
            setCamNum(getCamNum() - 1);
        }
    }
    
    public MyView addView(Canvas3D canvas) {
        assert universe == null;
        
        MyView view = new MyView(canvas);
        views.add(view);

        view.viewBranch = new BranchGroup();
        view.camera = new TransformGroup();
        view.camera.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        view.viewBranch.addChild(view.camera);
        ViewPlatform viewPlatform = new ViewPlatform();
        view.camera.addChild(viewPlatform);
        
        
        View xview = new View();
        xview.addCanvas3D(canvas);
        //View xview = canvas.getView();
        xview.setPhysicalBody(new PhysicalBody());
        xview.setPhysicalEnvironment(new PhysicalEnvironment());
        xview.attachViewPlatform(viewPlatform);
        xview.setBackClipDistance(1000);
        xview.setSceneAntialiasingEnable(true);
        //assert canvas.getSceneAntialiasingAvailable();
        
        
//        camera = universe.getViewingPlatform().getViewPlatformTransform();
//
//        View view = canvas.getView();
//        view.setBackClipDistance(1000);
//        
        // setCamNum(camNum);
        return view;
    }
    
    @Override
    public void init(TrackInformation trackInfo) {
        this.trackInfo = trackInfo;
        this.track = trackInfo.getTrack();
        assert (track != null);

        //
        world = createWorld(trackInfo);
        branchGroup = new BranchGroup();
        branchGroup.addChild(world);

        //if (1+1==2)            return; 
        // Create the universe and add the group of objects
        if (universe == null) {
            universe = new VirtualUniverse();
            Locale locale = new Locale(universe);
            locale.addBranchGraph(branchGroup);
            for (MyView view : views) {
                locale.addBranchGraph(view.viewBranch);
                view.setCamNum(view.getCamNum());
            }
        }
    }
    
    public void notify(double t, double[] y) {
        for (MyView view : views) {
            view.canvas.stopRenderer();
        }
        super.notify(t, y);
        double s = y[0];
        double dsdt = y[1];
        
        for (MyView view : views) {
            //assert false;
            //Transform3D transform = view.getCamTransform().getView(s, dsdt);
            
            //transform.invert();
            //view.camera.setTransform(transform);
            
            
        CameraView<Vector3d> camView = view.camTransform.getView(s, dsdt);
        Transform3D transform = new Transform3D();
        transform.lookAt(
                new Point3d(camView.getEye()), 
                new Point3d(camView.getTarget()), camView.getUp());
        transform.invert();
        view.camera.setTransform(transform);
            view.canvas.startRenderer();
            
        }
    }
    
}
