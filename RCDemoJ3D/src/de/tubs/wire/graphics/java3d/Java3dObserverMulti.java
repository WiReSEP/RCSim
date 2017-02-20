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
import javax.media.j3d.Transform3D;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;


/**
 *
 * @author ezander
 */
public class Java3dObserverMulti extends Java3dObserverBase {
    
    VirtualUniverse universe;
    List<ViewInfo> views = new ArrayList<>();

    public class ViewInfo implements ViewController {

        Canvas3D canvas;
        TransformGroup camera;
        int camNum = 0;
        Camera camTransform;
        BranchGroup viewBranch;
        
        public ViewInfo(Canvas3D canvas) {
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
        
        @Override
        public void nextCam() {
            setCamNum(getCamNum() + 1);
        }
        
        @Override
        public void prevCam() {
            setCamNum(getCamNum() - 1);
        }
    }
    
    public ViewInfo addView(Canvas3D canvas) {
        assert universe == null;
        
        ViewInfo viewinfo = new ViewInfo(canvas);
        views.add(viewinfo);

        viewinfo.viewBranch = new BranchGroup();
        viewinfo.camera = new TransformGroup();
        viewinfo.camera.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        viewinfo.viewBranch.addChild(viewinfo.camera);
        ViewPlatform viewPlatform = new ViewPlatform();
        viewinfo.camera.addChild(viewPlatform);
        
        
        View view = new View();
        view.addCanvas3D(canvas);
        //View xview = canvas.getView();
        view.setPhysicalBody(new PhysicalBody());
        view.setPhysicalEnvironment(new PhysicalEnvironment());
        view.attachViewPlatform(viewPlatform);
        view.setBackClipDistance(1000);
        view.setSceneAntialiasingEnable(true);
        //assert canvas.getSceneAntialiasingAvailable();
        // setCamNum(camNum);
        return viewinfo;
    }
    
    @Override
    public void init(TrackInformation trackInfo) {
        this.trackInfo = trackInfo;
        this.track = trackInfo.getTrack();
        assert (track != null);

        //
        world = createWorld();
        branchGroup = new BranchGroup();
        branchGroup.addChild(world);

        // Create the universe and add the group of objects
        if (universe == null) {
            universe = new VirtualUniverse();
            Locale locale = new Locale(universe);
            locale.addBranchGraph(branchGroup);
            for (ViewInfo view : views) {
                locale.addBranchGraph(view.viewBranch);
                view.setCamNum(view.getCamNum());
            }
        }
    }
    
    @Override
    public void notify(double t, double[] y) {
        for (ViewInfo view : views) {
            view.canvas.stopRenderer();
        }
        super.notify(t, y);
        double s = y[0];
        double dsdt = y[1];

        for (ViewInfo view : views) {
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
