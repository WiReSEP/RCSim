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


import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Primitive;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.PointLight;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Alpha;
import javax.media.j3d.RotationInterpolator;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3f;
import javax.vecmath.Point3d;
import javax.vecmath.Color3f;
import java.awt.Frame;
import java.awt.Label;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Node;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.ode.nonstiff.HighamHall54Integrator;
import rcdemo.math.StateIntegrator;
import rcdemo.physics.CombinedForceModel;
import rcdemo.physics.ConstantForceModel;
import rcdemo.physics.FrictionForceModel;
import rcdemo.track.TrackODE;

/**
 *
 * @author ezander
 */
public class RC3d {
    public static void run() {
        run3();
    }
    
    public static void run5() {
      //Construct the objects that will be displayed in
      // the scene

      //Create and set properties for the large yellow
      // sphere.
      //Begin by describing the appearance of the surface
      // of the large sphere.  Make the color of the large
      // sphere yellow.
      Material yellowSphMaterial = new Material();
      yellowSphMaterial.setDiffuseColor(1.0f,1.0f,0.0f);
      Appearance yellowSphAppearance = new Appearance();
      yellowSphAppearance.setMaterial(yellowSphMaterial);

      //Now instantiate the large yellow sphere with 9
      // divisions.  Set the radius to 0.5. The reason for
      // setting GENERATE_NORMALS is unclear at this time.
      Sphere yellowSph = new Sphere(
                               0.5f,
                               Primitive.GENERATE_NORMALS,
                               9,
                               yellowSphAppearance);

      //Now create a small white sphere with 50 divisions.
      Material whiteSphMaterial = new Material();
      whiteSphMaterial.setDiffuseColor(1.0f,1.0f,1.0f);
      Appearance whiteSphAppearance = new Appearance();
      whiteSphAppearance.setMaterial(whiteSphMaterial);
      Sphere whiteSph = new Sphere(
                               0.10f,
                               Primitive.GENERATE_NORMALS,
                               50,
                               whiteSphAppearance);

      //Translate the location of the white sphere to make
      // it closer to the viewer than the yellow sphere at
      // the origin. 
      Transform3D whiteTransform = new Transform3D();
      //The following is a modification to the original
      // virtual universe that causes the white sphere to
      // be in the horizontal plane.  This causes the
      // white sphere to later be animated so as to appear
      // to be in a synchronous orbit around the yellow
      // sphere.
      whiteTransform.setTranslation(
                          new Vector3f(-0.5f,-0.0f,0.5f));
      TransformGroup whiteTransformGroup = 
                                     new TransformGroup();
      whiteTransformGroup.setTransform(whiteTransform);
      whiteTransformGroup.addChild(whiteSph);


      //Now create a small green sphere located up to the
      // right and behind the yellow sphere.
      Material greenSphMaterial = new Material();
      greenSphMaterial.setDiffuseColor(0.0f,1.0f,0.0f);
      Appearance greenSphAppearance = new Appearance();
      greenSphAppearance.setMaterial(greenSphMaterial);
      Sphere greenSph = new Sphere(
                               0.10f,
                               Primitive.GENERATE_NORMALS,
                               50,
                               greenSphAppearance);
      Transform3D greenTransform = new Transform3D();
      greenTransform.setTranslation(
                           new Vector3f(0.5f,0.5f,-0.5f));
      TransformGroup greenTransformGroup = 
                                     new TransformGroup();
      greenTransformGroup.setTransform(greenTransform);
      greenTransformGroup.addChild(greenSph);


      //Add a white point light, in front of, to the
      // right of, and above the yellow sphere.
      Color3f pointLightColor =
                              new Color3f(1.0f,1.0f,1.0f);
      Point3f pointLightPosition =
                              new Point3f(1.0f,1.0f,2.0f);
      Point3f pointLightAttenuation =
                              new Point3f(1.0f,0.0f,0.0f);

      PointLight pointLight = new PointLight(
                                   pointLightColor,
                                   pointLightPosition,
                                   pointLightAttenuation);

      //Create a BoundingSphere object and use it to
      // determine which objects to light.  Also use it
      // later to determine which objects to animate.
      BoundingSphere boundingSphere = 
         new BoundingSphere(new Point3d(0.0,0.0,0.0),1.0);
      pointLight.setInfluencingBounds(boundingSphere);


      //Create an empty Java 3D universe and associate it
      // with the Canvas3D object in the CENTER of the
      // frame.
      SimpleUniverse simpleUniverse = 
                             new SimpleUniverse();

      //Create and populate a BranchGroup object.
      BranchGroup branchGroup = new BranchGroup();
      //Add objects to the branchGroup. Note that the
      // yellow and white spheres are no longer added to
      // the branchGroup object, but rather are later
      // added to a group that causes them to be animated.
      branchGroup.addChild(greenTransformGroup);
      //If you disable the following statement and enable
      // a statement later that adds the pointLight to
      // the rotationXform, you will see a very different
      // effect.  This will cause the light source to
      // rotate with the yellow sphere causing the shadows
      // to move across the spheres.
      branchGroup.addChild(pointLight);


      //THE CODE THAT IMPLEMENTS THE ANIMATION BEGINS HERE

      //Create a transform group that will be populated
      // with the yellow sphere and the white sphere
      // transform group. Objects or groups of objects
      // belonging to this group will be animated.
      TransformGroup rotationXform = new TransformGroup();
      rotationXform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

      //Create an Alpha object that will be used to cause
      // the objects in the rotationXform group to
      // complete one animation cycle in 20 seconds.
      Alpha rotationAlpha = new Alpha(1,20000);

      //Create an Interpolator object that will cause the
      // objects in the rotationXform group to rotate 360
      // degrees about the vertical axis in the time
      // specified for one cycle by the rotationAlpha
      // object.
      RotationInterpolator rotator =
                           new RotationInterpolator(
                             rotationAlpha,rotationXform);
      
      //Specify a region in 3D space containing the
      // objects or groups of objects that will be
      // animated.
      rotator.setSchedulingBounds(boundingSphere);

      //Add the objects to the group that controls
      // the animation.
      rotationXform.addChild(rotator);
      //rotationXform.addChild(yellowSph);
      //rotationXform.addChild(whiteTransformGroup);
      
      
      
        String filename = "tracks/colossos.rct";
        SimulationState state = SimulationState.readFromXML(filename);
        double alpha = 0.003;
        for(int s=0; s<40; s++) {
            double x[] = state.track.getx(s).mapSubtract(200).mapMultiply(alpha).toArray();
            Sphere sphere = new Sphere(0.05f);
            Vector3f vector = new Vector3f((float)x[0], (float)x[1], (float)x[2]);
            rotationXform.addChild(transform(sphere, vector));
        }
      
      
      
      
      //Disable the earlier statement that adds the
      // pointLight to the branchGroup and enable the
      // following statement to get a very different
      // effect
      //rotationXform.addChild(pointLight);

      //Add the group that will be animated to the main
      // branch of the scene graph.
      branchGroup.addChild(rotationXform);


      //THE CODE THAT IMPLEMENTS THE ANIMATION ENDS HERE


      //Specify the apparent location of the viewer's eye.
      simpleUniverse.getViewingPlatform().
                             setNominalViewingTransform();

      //Populate the universe by adding the branch group
      // that contains the objects.
      simpleUniverse.addBranchGraph(branchGroup);
    }
    
    public static void run4() {
        Java3D005 thisObj = new Java3D005();
    }
    
    public static Node transform(Node node, Vector3f vector) {
        Transform3D transform = new Transform3D();
        transform.setTranslation(vector);

        TransformGroup tg = new TransformGroup();
        tg.setTransform(transform);
        tg.addChild(node);
        return tg;
    }

    public static void run3() {

        SimpleUniverse universe = new SimpleUniverse();


      BranchGroup branchGroup = new BranchGroup();
        
      //THE CODE THAT IMPLEMENTS THE ANIMATION BEGINS HERE

      //Create a transform group that will be populated
      // with the yellow sphere and the white sphere
      // transform group. Objects or groups of objects
      // belonging to this group will be animated.
      TransformGroup rotationXform = new TransformGroup();
      TransformGroup group = rotationXform;
      rotationXform.setCapability(
                    TransformGroup.ALLOW_TRANSFORM_WRITE);

        String filename = "tracks/colossos.rct";
        SimulationState state = SimulationState.readFromXML(filename);
        double alpha = 0.003;
        for(double s=0; s<40; s+=0.1) {
            double x[] = state.track.getx(s).mapSubtract(200).mapMultiply(alpha).toArray();
            Sphere sphere = new Sphere(0.05f);
            Vector3f vector = new Vector3f((float)x[0], (float)x[1], (float)x[2]);
            group.addChild(transform(sphere, vector));
        }
      

      branchGroup.addChild(rotationXform);


      //THE CODE THAT IMPLEMENTS THE ANIMATION ENDS HERE
        
        
        TransformGroup tg = new TransformGroup();
        {
        double x[] = state.track.getx(0).mapSubtract(200).mapMultiply(alpha).toArray();
        Transform3D transform = new Transform3D();
        System.out.println(x);
        Node node = new ColorCube(0.05);
        Vector3f vector = new Vector3f((float)x[0], (float)x[1], (float)x[2]);
        transform.setTranslation(vector);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setTransform(transform);
        tg.addChild(node);
        group.addChild(tg);
        }

        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Color3f light1Color = new Color3f(.1f, 1.4f, .1f); // green light
        Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        group.addChild(light1);

        universe.getViewingPlatform().setNominalViewingTransform();

        
        
        // add the group of objects to the Universe
        universe.addBranchGraph(branchGroup);

        for(double s=0; s<400; s+=0.1) {
            System.out.println(s);
            double x[] = state.track.getx(s).mapSubtract(200).mapMultiply(alpha).toArray();
            Vector3f vector = new Vector3f((float)x[0], (float)x[1], (float)x[2]);
            Transform3D transform = new Transform3D();
            transform.setTranslation(vector);
            tg.setTransform(transform);

            try {
                Thread.sleep(10);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void run2() {
        SimpleUniverse universe = new SimpleUniverse();

        BranchGroup group = new BranchGroup();

        group.addChild(new ColorCube(0.3));

        String filename = "tracks/colossos.rct";
        SimulationState state = SimulationState.readFromXML(filename);
        TrackODE ode2 = new TrackODE(
                state.track,
                new CombinedForceModel()
                .add(ConstantForceModel.createGravityForceModel(1, 9.81), 1)
                .add(new FrictionForceModel(), 0.01));
        ArrayRealVector y = new ArrayRealVector(new double[]{0, 1});

        StateIntegrator.setDefaultIntegrator(new HighamHall54Integrator(1e-6, 1, 1e-8, 1e-8));
        StateIntegrator stateInt = new StateIntegrator(ode2, y);
        double t1 = 200, dt = 0.5;
        while (true) {
            double t = stateInt.getT();
            y = stateInt.getY();
            if (t >= t1 - 1e-7) {
                break;
            }
            stateInt.integrateTo(t + dt);
        }

        for (float x = -1.0f; x <= 1.0f; x = x + 0.1f) {
            Sphere sphere = new Sphere(0.05f);
            TransformGroup tg = new TransformGroup();
            Transform3D transform = new Transform3D();
            Vector3f vector = new Vector3f(x, .0f, .0f);
            transform.setTranslation(vector);
            tg.setTransform(transform);
            tg.addChild(sphere);
            group.addChild(tg);
        }

        universe.getViewingPlatform().setNominalViewingTransform();

        universe.addBranchGraph(group);
    }
}



/*File Java3D005.java
Copyright 2007, R.G.Baldwin

The purpose of this program is to create a simple
animation program that will animate the virtual universe
that was developed in Java3D004.

The location of the white sphere was modified relative to
the original virtual universe.  The modification causes
the white sphere to be in the horizontal plane. This
causes the white sphere to later be animated so as to
appear to be in a synchronous orbit around the yellow
sphere.

The universe was animated by causing the yellow sphere to
slowly rotate on its vertical axis through one complete
revolution.  The yellow sphere carries the small white
sphere along with it just as though the white sphere is
orbiting the yellow sphee in a synchronous circular orbit.

The small green sphere does not rotate with the yellow
sphere, but maintains its original position.

In addition, comments are provided to show how to cause
the light source to also rotate around the vertical axis
in 3D space in synchronism with the rotation of the yellow
sphere.  This produces a very different effect causing 
shadows to move across the spheres.

Tested using Java SE 6, and Java 3D 1.5.0 running under
Windows XP.
*********************************************************/


//This is the top-level driver class for this program.
// This program could be written without the use of this
// driver class.  However, I decided to keep it intact
// for future expansions that require a user input GUI.
class Java3D005 extends Frame{
  public static void main(String[] args){
    Java3D005 thisObj = new Java3D005();
  }//end main
  //----------------------------------------------------//
  
  public Java3D005(){//constructor
    setTitle("Copyright 2007, R.G.Baldwin");
    add(new Label("You can build a GUI here."));
    setBounds(236,0,235,75);
    setVisible(true);
    
    //Instantiate the object in which the Java 3D
    // universe will be displayed.
    TheScene theScene = new TheScene();

    //This window listener is used to terminate the
    // program when the user clicks the X button.
    addWindowListener(
      new WindowAdapter(){
        public void windowClosing(WindowEvent e){
          System.exit(0);
        }//end windowClosing
      }//end new WindowAdapter
    );//end addWindowListener

  }//end constructor
  //----------------------------------------------------//

  //This is an inner class, from which the object in which
  // the Java 3D universe will be displayed is
  // instantiated.
  class TheScene extends Frame{

    TheScene(){//constructor

      //Create a Canvas3D object to be used for rendering
      // the Java 3D universe. Place it in the CENTER of
      // the Frame.
      Canvas3D canvas3D = new Canvas3D(
              SimpleUniverse.getPreferredConfiguration());
      add(BorderLayout.CENTER,canvas3D);

      //Construct the objects that will be displayed in
      // the scene

      //Create and set properties for the large yellow
      // sphere.
      //Begin by describing the appearance of the surface
      // of the large sphere.  Make the color of the large
      // sphere yellow.
      Material yellowSphMaterial = new Material();
      yellowSphMaterial.setDiffuseColor(1.0f,1.0f,0.0f);
      Appearance yellowSphAppearance = new Appearance();
      yellowSphAppearance.setMaterial(yellowSphMaterial);

      //Now instantiate the large yellow sphere with 9
      // divisions.  Set the radius to 0.5. The reason for
      // setting GENERATE_NORMALS is unclear at this time.
      Sphere yellowSph = new Sphere(
                               0.5f,
                               Primitive.GENERATE_NORMALS,
                               9,
                               yellowSphAppearance);

      //Now create a small white sphere with 50 divisions.
      Material whiteSphMaterial = new Material();
      whiteSphMaterial.setDiffuseColor(1.0f,1.0f,1.0f);
      Appearance whiteSphAppearance = new Appearance();
      whiteSphAppearance.setMaterial(whiteSphMaterial);
      Sphere whiteSph = new Sphere(
                               0.10f,
                               Primitive.GENERATE_NORMALS,
                               50,
                               whiteSphAppearance);

      //Translate the location of the white sphere to make
      // it closer to the viewer than the yellow sphere at
      // the origin. 
      Transform3D whiteTransform = new Transform3D();
      //The following is a modification to the original
      // virtual universe that causes the white sphere to
      // be in the horizontal plane.  This causes the
      // white sphere to later be animated so as to appear
      // to be in a synchronous orbit around the yellow
      // sphere.
      whiteTransform.setTranslation(
                          new Vector3f(-0.5f,-0.0f,0.5f));
      TransformGroup whiteTransformGroup = 
                                     new TransformGroup();
      whiteTransformGroup.setTransform(whiteTransform);
      whiteTransformGroup.addChild(whiteSph);


      //Now create a small green sphere located up to the
      // right and behind the yellow sphere.
      Material greenSphMaterial = new Material();
      greenSphMaterial.setDiffuseColor(0.0f,1.0f,0.0f);
      Appearance greenSphAppearance = new Appearance();
      greenSphAppearance.setMaterial(greenSphMaterial);
      Sphere greenSph = new Sphere(
                               0.10f,
                               Primitive.GENERATE_NORMALS,
                               50,
                               greenSphAppearance);
      Transform3D greenTransform = new Transform3D();
      greenTransform.setTranslation(
                           new Vector3f(0.5f,0.5f,-0.5f));
      TransformGroup greenTransformGroup = 
                                     new TransformGroup();
      greenTransformGroup.setTransform(greenTransform);
      greenTransformGroup.addChild(greenSph);


      //Add a white point light, in front of, to the
      // right of, and above the yellow sphere.
      Color3f pointLightColor =
                              new Color3f(1.0f,1.0f,1.0f);
      Point3f pointLightPosition =
                              new Point3f(1.0f,1.0f,2.0f);
      Point3f pointLightAttenuation =
                              new Point3f(1.0f,0.0f,0.0f);

      PointLight pointLight = new PointLight(
                                   pointLightColor,
                                   pointLightPosition,
                                   pointLightAttenuation);

      //Create a BoundingSphere object and use it to
      // determine which objects to light.  Also use it
      // later to determine which objects to animate.
      BoundingSphere boundingSphere = 
         new BoundingSphere(new Point3d(0.0,0.0,0.0),1.0);
      pointLight.setInfluencingBounds(boundingSphere);


      //Create an empty Java 3D universe and associate it
      // with the Canvas3D object in the CENTER of the
      // frame.
      SimpleUniverse simpleUniverse = 
                             new SimpleUniverse(canvas3D);

      //Create and populate a BranchGroup object.
      BranchGroup branchGroup = new BranchGroup();
      //Add objects to the branchGroup. Note that the
      // yellow and white spheres are no longer added to
      // the branchGroup object, but rather are later
      // added to a group that causes them to be animated.
      branchGroup.addChild(greenTransformGroup);
      //If you disable the following statement and enable
      // a statement later that adds the pointLight to
      // the rotationXform, you will see a very different
      // effect.  This will cause the light source to
      // rotate with the yellow sphere causing the shadows
      // to move across the spheres.
      branchGroup.addChild(pointLight);


      //THE CODE THAT IMPLEMENTS THE ANIMATION BEGINS HERE

      //Create a transform group that will be populated
      // with the yellow sphere and the white sphere
      // transform group. Objects or groups of objects
      // belonging to this group will be animated.
      TransformGroup rotationXform = new TransformGroup();
      rotationXform.setCapability(
                    TransformGroup.ALLOW_TRANSFORM_WRITE);

      //Create an Alpha object that will be used to cause
      // the objects in the rotationXform group to
      // complete one animation cycle in 20 seconds.
      Alpha rotationAlpha = new Alpha(1,20000);

      //Create an Interpolator object that will cause the
      // objects in the rotationXform group to rotate 360
      // degrees about the vertical axis in the time
      // specified for one cycle by the rotationAlpha
      // object.
      RotationInterpolator rotator =
                           new RotationInterpolator(
                             rotationAlpha,rotationXform);
      
      //Specify a region in 3D space containing the
      // objects or groups of objects that will be
      // animated.
      rotator.setSchedulingBounds(boundingSphere);

      //Add the objects to the group that controls
      // the animation.
      rotationXform.addChild(rotator);
      rotationXform.addChild(yellowSph);
      rotationXform.addChild(whiteTransformGroup);
      //Disable the earlier statement that adds the
      // pointLight to the branchGroup and enable the
      // following statement to get a very different
      // effect
      //rotationXform.addChild(pointLight);

      //Add the group that will be animated to the main
      // branch of the scene graph.
      branchGroup.addChild(rotationXform);


      //THE CODE THAT IMPLEMENTS THE ANIMATION ENDS HERE


      //Specify the apparent location of the viewer's eye.
      simpleUniverse.getViewingPlatform().
                             setNominalViewingTransform();

      //Populate the universe by adding the branch group
      // that contains the objects.
      simpleUniverse.addBranchGraph(branchGroup);

      //Do the normal GUI stuff.
      setTitle("Copyright 2007, R.G.Baldwin");
      setBounds(0,0,235,235);
      setVisible(true);

      //This listener is used to terminate the program
      // when the user clicks the X-button on the Frame.
      addWindowListener(
        new WindowAdapter(){
          public void windowClosing(WindowEvent e){
            System.exit(0);
          }//end windowClosing
        }//end new WindowAdapter
      );//end addWindowListener
      
    }//end constructor
    //--------------------------------------------------//
    
  }//end inner class TheScene

}//