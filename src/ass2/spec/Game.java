package ass2.spec;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;



/**
 * COMMENT: Comment Game 
 *
 * @author malcolmr
 */
public class Game extends JFrame implements GLEventListener, KeyListener{

    private Terrain myTerrain;
    private TextureData data;
    private Camera camera;
    private Avatar teapot;
    public Game(Terrain terrain) {
    	super("Assignment 2");
    	myTerrain = terrain;
    	
        float x = 0;
        float z = 0;
        float offSet[] = terrain.getOffset();
        float y = (float)myTerrain.altitude(x+offSet[1], z+offSet[0]); 

        teapot = new Avatar(0,myTerrain.altitude(offSet[1],offSet[0]),0,myTerrain);
        camera = new Camera(teapot,myTerrain);

    }
    
    /** 
     * Run the game.
     *
     */
    public void run() {
    	  GLProfile glp = GLProfile.getDefault();
    	  File file = new File("grass.jpg");
    	  try {
			data = TextureIO.newTextureData(glp, file, false, "jpg");
    	  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	  }
          GLCapabilities caps = new GLCapabilities(glp);
          GLJPanel panel = new GLJPanel();
          panel.addGLEventListener(this);
          panel.addKeyListener(this);
 
          // Add an animator to call 'display' at 60fps        
          FPSAnimator animator = new FPSAnimator(60);
          animator.add(panel);
          animator.start();

          getContentPane().add(panel);
          setSize(800, 600);        
          setVisible(true);
          setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    /**
     * Load a level file and display it.
     * 
     * @param args - The first argument is a level file in JSON format
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        Terrain terrain = LevelIO.load(new File(args[0]));
        Game game = new Game(terrain);
        game.run();
    }

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		//gluLookAt must be called under modelview matrix

		myTerrain.init(gl);	
		camera.setCamera(gl);
		setUpLight(gl);
		myTerrain.draw(gl,data);	
		teapot.draw(gl);
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
        gl.glDeleteBuffers(1,myTerrain.getBufferIds(),0);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL2.GL_LIGHTING);
	    gl.glEnable(GL2.GL_LIGHT0);
	    gl.glEnable(GL2.GL_DEPTH_TEST);
	    /*gl.glEnable(GL2.GL_CULL_FACE);
	    gl.glCullFace(GL2.GL_BACK);*/
	}

	private void setUpLight(GL2 gl) {
		//float lightPos[] =  {camera.getPos()[0],camera.getPos()[1],camera.getPos()[2],1};
		float lightPos[] = {0,10,0,1};
		float lightAmb[] = {0,0,0,1};
		float lightDiff[] = {1.0f,1.0f,1.0f,1};
		float lightSpec[] = {1.0f,1.0f,1.0f,1};
		float gloAmb[] = {0.0f,0.0f,0.0f,1.0f};
		
		gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_POSITION,lightPos,0);
		gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_DIFFUSE,lightDiff,0);
		gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_SPECULAR,lightSpec,0);
		gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_AMBIENT,lightAmb,0);
		
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT,gloAmb,0);
	    gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE,GL2.GL_TRUE);
	}
	

	
	

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		camera.setAspect(1.0f * width/height);	
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			teapot.vmove(0.1);
			break;
		case KeyEvent.VK_DOWN:
			teapot.vmove(-0.1);
			break;
		case KeyEvent.VK_LEFT:
			teapot.rotate(-0.1);
			break;
		case KeyEvent.VK_RIGHT:
			teapot.rotate(0.1);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
