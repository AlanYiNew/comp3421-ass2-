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
public class Game extends JFrame implements GLEventListener, KeyListener {

	private Terrain myTerrain;
	private TextureData data;
	private double camPos[];
	private double camLookAt[];

	public Game(Terrain terrain) {
		super("Assignment 2");
		myTerrain = terrain;
		camPos = new double[] { 0, 2, 1 };
		camLookAt = new double[] { 0, 0, 0 };
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
	 * @param args
	 *            - The first argument is a level file in JSON format
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
		Terrain terrain = LevelIO.load(new File(args[0]));
		Game game = new Game(terrain);
		game.run();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GLU glu = new GLU();
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(camPos[0], camPos[1], camPos[2], camLookAt[0],
				camLookAt[1], camLookAt[2], 0, 1, 0);
		myTerrain.init(gl);
		myTerrain.draw(gl, data);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glDeleteBuffers(1, myTerrain.getBufferIds(), 0);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();
		
		//setting cammera
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU glu = new GLU();
		// glu.gluPerspective(20, (float)width/(float)height, 0.5, 12);
		gl.glOrtho(-5, 5, -5, 5, 0, -5);
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			camPos[1] += 0.1;
			camLookAt[1] += 0.1;
			break;
		case KeyEvent.VK_DOWN:
			camPos[1] -= 0.1;
			camLookAt[1] -= 0.1;
			break;
		case KeyEvent.VK_LEFT:
			camPos[0] -= 0.1;
			//camLookAt[0] -= 0.1;
			break;
		case KeyEvent.VK_RIGHT:
			camPos[0] += 0.1;
			//camLookAt[0] += 0.1;
			break;
		case KeyEvent.VK_A:
			camPos[2] += 0.1;
			//camLookAt[2] += 0.1;
			break;
		case KeyEvent.VK_B:
			camPos[2] -= 0.1;
			//camLookAt[2] -= 0.1;
			break;
		default:
			break;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
