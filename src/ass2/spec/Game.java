package ass2.spec;

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
public class Game extends JFrame implements GLEventListener{

    private Terrain myTerrain;
    private TextureData data;
    private Camera camera;
    public Game(Terrain terrain) {
    	super("Assignment 2");
        myTerrain = terrain;
        camera = new Camera();
        float x = 0;
        float z = 0;
        float offSet[] = terrain.getOffset();
        float y = (float)myTerrain.altitude(x+offSet[1], z+offSet[0]); 
        camera.setFocus(x,y,z);
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
		gl.glClearColor(0,0,0,1);
		
		//gluLookAt must be called under modelview matrix
		setUpMaterialt(gl);
		camera.setCamera(gl);
		myTerrain.init(gl);	
		myTerrain.draw(gl,data);	
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
	    setUpLight(gl);
	}

	private void setUpLight(GL2 gl) {
		float lightPos[] =  {0,3,-5,1};
		float lightAmb[] = {0,0,0,1};
		float lightDiff[] = {1.0f,1.0f,1.0f,1};
		float lightSpec[] = {1.0f,1.0f,1.0f,1};
		float gloAmb[] = {1.0f,0.0f,0.0f,1.0f};
		
		gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_POSITION,lightPos,0);
		gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_DIFFUSE,lightDiff,0);
		gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_SPECULAR,lightSpec,0);
		gl.glLightfv(GL2.GL_LIGHT0,GL2.GL_AMBIENT,lightAmb,0);
		
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT,gloAmb,0);
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE,GL2.GL_TRUE);
	}
	
	private void setUpMaterialt(GL2 gl) {
		float matShine[] =  {70};
		float lightAmb[] = {0,0,0,1};
		float lightDiff[] = {1.0f,1.0f,1.0f,1};
		float lightSpec[] = {1.0f,1.0f,1.0f,1};
		float gloAmb[] = {0.0f,0.0f,0.0f,1.0f};
		
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_SHININESS,matShine,0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_DIFFUSE,lightDiff,0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_SPECULAR,lightSpec,0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_AMBIENT,lightAmb,0);
	}
	
	

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		camera.setAspect(1.0f * width/height);
		/*gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrtho(-10, 10, -10, 10, 0, -10);*/
		
		/*gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();
		glu.gluPerspective(90, 1.0*width/height,1, 12);
		gl.glMatrixMode(GL2.GL_MODELVIEW);*/
		
		
	}
}
