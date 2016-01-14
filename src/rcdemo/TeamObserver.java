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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsContext3D;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Raster;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import rcdemo.graphics.java3d.Java3dObserverSimple;
import rcdemo.simulator.SimulationState;
import rcdemo.simulator.Simulator;

/**
 *
 * @author ezander
 */
public class TeamObserver extends Java3dObserverSimple {

    @Override
    public void init(SimulationState state) {
        super.init(state); //To change body of generated methods, choose Tools | Templates.
        //universe.getViewer().getView().addCanvas3D(offCanvas);

    }

    
    static BufferedImage createBufferedImage(Canvas3D canvas){
        GraphicsContext3D  ctx = canvas.getGraphicsContext3D();
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        BufferedImage bi = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
        ImageComponent2D im = new ImageComponent2D( ImageComponent.FORMAT_RGB, bi );
        Raster ras = new Raster( new Point3f( -1.0f, -1.0f, -1.0f ), Raster.RASTER_COLOR, 0, 0, w, h, im, null );
        ctx.readRaster( ras );
        return ras.getImage().getImage();
    }
    
    public TeamObserver() {
    }

    int imgNum = 0;
    void saveFile(BufferedImage img, String baseName, String ext) {
        imgNum++;
        String filename = String.format("%s-%03d.%s", baseName, imgNum, ext);
        File file = new File(filename);
        try {
            ImageIO.write(img, ext, file);
        } catch (IOException ex) {
            System.out.println("foo");
            Logger.getLogger(TeamObserver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void notify(double t, double[] y) {
        super.notify(t, y);

        // TODO: Send t (simulation time), s, and dsdt
        double s = y[0];
        double dsdt = y[1];
        System.out.format("Sim-State: %s %s %s\n", t, s, dsdt);

        // TODO: Send pos, speed and velocity
        Vector3d pos = helper.getPosition(track, s);
        Vector3d speed = helper.getSpeed(track, s, dsdt);
        double velocity = speed.length();
        System.out.format("Position: %s\n", pos);
        System.out.format("Speed: %s\n", speed);
        System.out.format("Velocity: %s\n", velocity);
        
        // TODO: Transfer this image
        BufferedImage img1 = createBufferedImage(canvas);
        
        // No real need to save the buffer here, only to show how/that it works
        // That was a real pain...
        saveFile(img1, "rc-image", "png");
        
        // Sleep for a while as the simulation is fast (better choose a larger delay later) 
        Simulator.sleep(0.5);
    }

}
