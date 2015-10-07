package ass2.spec;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Light {
	
	private float lightPos[] = { 0, 5, 0, 1 };
	private float lightAmb[] = { 0, 0, 0, 1 };
	private float lightDiff[] = { 1.0f, 1.0f, 1.0f, 1 };
	private float lightSpec[] = { 1.0f, 1.0f, 1.0f, 1 };
	private float gloAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };	
	
	public void setUpLight(GL2 gl) {

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiff, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb, 0);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, gloAmb, 0);
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
	}

	public void draw(GL2 gl) {
		gl.glPushMatrix();
		GLUT glut = new GLUT();
		gl.glTranslated(lightPos[0],lightPos[1],lightPos[2]);
		//gl.glTranslated(0,0,4);
		gl.glFrontFace(GL2.GL_CW);
		glut.glutSolidSphere(0.1,50,50);
		gl.glFrontFace(GL2.GL_CCW);
		
		gl.glPopMatrix();
	}

}
