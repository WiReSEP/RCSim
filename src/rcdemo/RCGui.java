package rcdemo;

import com.sun.j3d.utils.universe.SimpleUniverse;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.swing.JFileChooser;
import javax.swing.JPopupMenu;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import rcdemo.graphics.Java3dObserverBase;
import rcdemo.graphics.Java3dObserverMulti;
import rcdemo.graphics.Java3dObserverSimple;
import rcdemo.simulator.ODESimulator;
import rcdemo.simulator.SimulationState;
import rcdemo.simulator.Simulator;
import rcdemo.simulator.TextBasedObserver;
import rcdemo.simulator.TimeStepper;
import rcdemo.ui.KeyProcessor;
import rcdemo.ui.KeyEventFunction;

/**
 *
 * @author ezander
 */
public class RCGui extends javax.swing.JFrame {

    String lastPath;
    Java3dObserverBase java3dObserver;
    Simulator sim;
    Timer timer;

    /**
     * Creates new form RCGui
     */
    public RCGui() {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        initComponents();
        lastPath = System.getProperty("user.dir");
        //initSimple();
        initMulti();
    }

    
    
    void initMulti() {
        //setLayout(new BorderLayout());
        Dimension minimumSize = new Dimension(10, 10);
        
        //GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        //config.set
        
        
        GraphicsConfigTemplate3D gct3D= new GraphicsConfigTemplate3D();
        //gct3D.setSceneAntialiasing(GraphicsConfigTemplate3D.PREFERRED);
        GraphicsConfiguration config= java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().
                //getDefaultConfiguration();
                getBestConfiguration(gct3D);
        
        Canvas3D canvas1 = new Canvas3D(config);
        //canvas1.se
        canvas1.setDoubleBufferEnable(true);
        //getContentPane().add(canvas1, BorderLayout.CENTER);
        //getContentPane().add(canvas1);
        canvas1.setMinimumSize(minimumSize);
        jSplitPane1.setLeftComponent(canvas1);
        canvas1.requestFocus();

        Canvas3D canvas2 = new Canvas3D(config);
        canvas2.setDoubleBufferEnable(true);
        //getContentPane().add(canvas2, BorderLayout.SOUTH);
        //getContentPane().add(canvas2);
        canvas2.setMinimumSize(minimumSize);
        jSplitPane1.setRightComponent(canvas2);
        canvas2.requestFocus();
        
        setSize(160 * 6, 90 * 6);
        jSplitPane1.setDividerLocation(0.5);
        jSplitPane1.setOneTouchExpandable(true);
        
        
        Java3dObserverMulti observer = new Java3dObserverMulti();
        java3dObserver = observer;
        Java3dObserverMulti.MyView view1 = observer.addView(canvas1);
        Java3dObserverMulti.MyView view2 = observer.addView(canvas2);
        view1.setCamNum(-1);

        sim = new ODESimulator();
        sim.addObserver(observer);
        //sim.addObserver(new TextBasedObserver());

        TimeStepper stepper = sim.getStepper();
        canvas2.addKeyListener(
                new KeyProcessor()
                .add(KeyEvent.VK_SHIFT, e -> sim.getStepper().pause(), e -> sim.getStepper().resume())
                .add('+', e -> sim.getStepper().accelerate(1.4142))
                .add('-', e -> sim.getStepper().decelerate(1.4142))
                .add('p', e -> sim.getStepper().pause())
                .add('c', e -> sim.getStepper().resume())
                .add('q', e -> System.exit(0))
                .add(KeyEvent.VK_LEFT, null, e -> view1.prevCam())
                .add(KeyEvent.VK_RIGHT, null, e -> view1.nextCam())
                .add(KeyEvent.VK_UP, null, e -> view2.prevCam())
                .add(KeyEvent.VK_DOWN, null, e -> view2.nextCam())
        //.add('')
        );
        
    }
    
    void initSimple() {
        //setLayout(new BorderLayout());
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas = new Canvas3D(config);
        canvas.setDoubleBufferEnable(true);
        getContentPane().add(canvas, BorderLayout.CENTER);
        canvas.requestFocus();
        setSize(160 * 6, 90 * 6);

        Java3dObserverSimple observer = new Java3dObserverSimple();
        java3dObserver = observer;
        observer.setCanvas(canvas);

        sim = new ODESimulator();
        sim.addObserver(observer);

        TimeStepper stepper = sim.getStepper();
        canvas.addKeyListener(
                new KeyProcessor()
                .add(KeyEvent.VK_SHIFT, e -> sim.getStepper().pause(), e -> sim.getStepper().resume())
                .add('+', e -> sim.getStepper().accelerate(1.4142))
                .add('-', e -> sim.getStepper().decelerate(1.4142))
                .add('p', e -> sim.getStepper().pause())
                .add('c', e -> sim.getStepper().resume())
                .add('q', e -> System.exit(0))
                .add(KeyEvent.VK_LEFT, null, e -> observer.prevCam())
                .add(KeyEvent.VK_RIGHT, null, e -> observer.nextCam())
        //.add('')
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setResizeWeight(0.5);
        getContentPane().add(jSplitPane1);

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        openMenuItem.setMnemonic('o');
        openMenuItem.setText("Open");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openMenuItem);

        saveMenuItem.setMnemonic('s');
        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setMnemonic('a');
        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.setDisplayedMnemonicIndex(5);
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Edit");

        cutMenuItem.setMnemonic('t');
        cutMenuItem.setText("Cut");
        editMenu.add(cutMenuItem);

        copyMenuItem.setMnemonic('y');
        copyMenuItem.setText("Copy");
        editMenu.add(copyMenuItem);

        pasteMenuItem.setMnemonic('p');
        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setMnemonic('d');
        deleteMenuItem.setText("Delete");
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        contentsMenuItem.setMnemonic('c');
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void loadFile(File selected) {

        //c.addKeyListener(this);
        //timer = new Timer(100, this);
        //timer.start();
        //Panel p = new Panel();
        //p.add(go);
        //add("North", p);
        //go.addActionListener(this);
        //go.addKeyListener(this);
        // Create a simple scene and attach it to the virtual universe
        String filename = selected.getAbsolutePath();
        SimulationState state = SimulationState.readFromXML(filename);
        jSplitPane1.setDividerLocation(0.5);

        sim.setState(state);
        sim.init();
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(10, e -> sim.update());
        timer.start();
    }

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        JFileChooser chooser = new JFileChooser(lastPath);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "RollerCoaster files", "rct");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            lastPath = selectedFile.getAbsolutePath();
            loadFile(selectedFile);
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RCGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RCGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RCGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RCGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RCGui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    // End of variables declaration//GEN-END:variables

}
