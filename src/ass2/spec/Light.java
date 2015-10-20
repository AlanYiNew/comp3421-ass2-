package ass2.spec;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Light {
	private float radius = 12; 
	private long initialTime;
	private float lightPos[] = { -radius, 0, 0, 1 };
	private float lightAmb[] = { 0, 0, 0, 1 };
	private float lightDiff[] = { 1.0f, 0.85f, 0.7f, 1 };
	private float lightSpec[] = { 1.0f, 0.85f, 0.7f, 1 };
	private float gloAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };	
	
	private boolean MOVING_SUN = false;
	
	public static enum lightMode{SUN,TORCH};
	lightMode mode;
	
	public Light(){
		initialTime = System.currentTimeMillis();
	}
	
	public Light(Light.lightMode m){
		mode = m;
	}
	
	public void setMode(Light.lightMode m){
		mode = m;
	}
	
	public void setUpLight(GL2 gl) {
		
		if(mode == lightMode.SUN && MOVING_SUN){
			double currTime = System.currentTimeMillis();
			double difference = (currTime - initialTime)/8000;
			lightPos[0] = (float) (-radius*Math.cos(difference));
			lightPos[1] = (float) (radius*Math.sin(difference));
				if	((float)Math.abs( Math.sin(difference)) >= 0.7){
					lightDiff[2] = (float)Math.abs( Math.sin(difference));
					lightSpec[2] = (float)Math.abs( Math.sin(difference));
				}
				
				if	((float)Math.abs( Math.sin(difference)) >= 0.85){
					lightDiff[1] = (float)Math.abs( Math.sin(difference));
					lightSpec[1] = (float)Math.abs( Math.sin(difference));
				}
		}
				
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiff, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb, 0);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, gloAmb, 0);
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);		
	}
	
	public void setLightPos(float pos[]){
		lightPos = pos;
	}
	
	public void toggleSun(){
		MOVING_SUN = !MOVING_SUN;
		initialTime = System.currentTimeMillis();
	}

	public void draw(GL2 gl) {
		gl.glPushMatrix();
		GLUT glut = new GLUT();
		gl.glTranslated(lightPos[0],lightPos[1],lightPos[2]);

		gl.glFrontFace(GL2.GL_CW);
		glut.glutSolidSphere(0.1,50,50);
		gl.glFrontFace(GL2.GL_CCW);
		
		gl.glPopMatrix();
	}
}
