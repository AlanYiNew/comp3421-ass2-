package ass2.spec;

import java.util.Random;

import javax.media.opengl.GL2;
import static javax.media.opengl.GL.*;  // GL constants

public class Rain{
	private static final int MAX_PARTICLES = 50000; // max number of particles
	private Particle[] particles = new Particle[MAX_PARTICLES];
	// Global speed for all the particles
	private static float z = -40.0f; //zOffset
	private static float y = 5.0f;   //yOffset
	MyTexture myTexture;
	private Camera camera;
	private Terrain terrain;
	private boolean on;
	Rain(Camera camera,Terrain terrain){
		// Initialize the particles
		this.camera = camera;
		this.terrain = terrain;
		this.on = false;
		for (int i = 0; i < MAX_PARTICLES; i++) {
			particles[i] = new Particle(this);
		}
	}
	public void display(GL2 gl) {
		gl.glPushMatrix();
		 //gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
	     
		gl.glActiveTexture(gl.GL_TEXTURE1);
		myTexture = new MyTexture(gl, "rain.bmp", "bmp", false);
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_COMBINE);
		gl.glEnable(GL_TEXTURE_2D);
		gl.glEnable(GL_BLEND);
		gl.glDepthMask( false );
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		

		// Render the particles
		for (int i = 0; i < MAX_PARTICLES; i++) {
			if (particles[i].active){
				// Draw the particle using our RGB values, fade the particle based on it's life

				gl.glColor4f(0.5f,0.75f, 0.75f, 0.3f);
				gl.glBindTexture(GL2.GL_TEXTURE_2D, myTexture.getTextureId()); 
				gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
				float[] color = {0.5f,0.75f,0.75f,0.3f};

				gl.glBegin(GL_TRIANGLE_STRIP); // build quad from a triangle strip

				float px = particles[i].x;
				float py = particles[i].y;
				float pz = particles[i].z;
				float[] ps1 = {px + 0.0125f, py + 0.0125f, pz};
				float[] ps2 = {px - 0.0125f, py + 0.0125f, pz};
				float[] ps3 = {px + 0.0125f, py - 0.0125f, pz};
				float[] ps4 = {px - 0.0125f, py - 0.0125f, pz};
				float[] rainPos = {px,py,pz};
				float[] dc = MathUtil.sub(rainPos,camera.getEye());
				float[] ca = MathUtil.normalise( MathUtil.crossProduct(dc, camera.getCameraUp()));
				float[] cb = MathUtil.normalise(MathUtil.crossProduct(dc, ca));
				float[] p1 = MathUtil.sub(rainPos, MathUtil.scale(MathUtil.add(ca,cb),0.01f));
				float[] p2 = MathUtil.add(rainPos, MathUtil.scale(MathUtil.sub(ca,cb),0.01f));
				float[] p3 = MathUtil.add(rainPos, MathUtil.scale(MathUtil.add(ca,cb),0.01f));
				float[] p4 = MathUtil.sub(rainPos, MathUtil.scale(MathUtil.sub(ca,cb),0.01f));
				gl.glMultiTexCoord2d(GL_TEXTURE1,1, 1);
	            gl.glVertex3f(p1[0],p1[1],p1[2]);
	            gl.glMultiTexCoord2d(GL_TEXTURE1,0, 1);
	            gl.glVertex3f(p2[0],p2[1],p2[2]);
	            gl.glMultiTexCoord2d(GL_TEXTURE1,1, 0);
	            gl.glVertex3f(p4[0],p4[1],p4[2]);
	            gl.glMultiTexCoord2d(GL_TEXTURE1,0, 0);
	            gl.glVertex3f(p3[0],p3[1],p3[2]);
	            gl.glEnd();
	            particles[i].update(this);
			}
		}
		gl.glDisable(GL_TEXTURE_2D);
		gl.glPopMatrix();
		gl.glActiveTexture(gl.GL_TEXTURE0);
		gl.glDisable(GL_BLEND);
		gl.glDepthMask( true );
	}

	// Particle (inner class)
	class Particle {
		float x, y, z,drop;  // position
		boolean active;
		private Random rand = new Random();

		// Constructor
		public Particle(Rain rain) {			
			init();
			active = rain.on;
		}

		public void update(Rain rain) {
			if (y <= 0){
				if (rain.on){
					init();
				}else{
					active = !active;
				}
			}else{
				y -= drop;
			}
			
		}

		public void init() {
			x = ((float)(rand.nextInt(terrain.size().width*100)))/100-terrain.X_OFFSET;
			y = rand.nextFloat()+4;
			z = ((float)(rand.nextInt(terrain.size().height*100)))/100-terrain.Z_OFFSET;
			drop = rand.nextFloat();
		}
	}
	
	public void rainChange() {
		on = !on;
		for (int i = 0; i < MAX_PARTICLES; i++) {
			if (!particles[i].active){
				particles[i] = new Particle(this);
			}
		}
	}

}

