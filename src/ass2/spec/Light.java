package ass2.spec;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Light {
	/*
	 * private float radius = 12;
	 */
	private float lightPos[] = null;
	private float lightAmb[] = { 0, 0, 0, 1 };
	private float lightDiff[] = { 1.0f, 0.85f, 0.7f, 1 };
	private float lightSpec[] = { 1.0f, 0.85f, 0.7f, 1 };
	private float gloAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };

	private boolean MOVING_SUN = false;
	private Terrain terrain = null;

	// r1 x,z axis, r2, x,y axis
	private float r1, r2, initAngle1, initAngle2;
	private long initialTime;

	public static enum lightMode {
		SUN, TORCH
	};

	lightMode mode;

	public Light(Terrain t) {
		terrain = t;
	}

	public Light(Light.lightMode m) {
		mode = m;
	}

	public void setMode(Light.lightMode m) {
		mode = m;
	}

	public void setUpLight(GL2 gl) {

		if (mode == lightMode.SUN && MOVING_SUN) {
			//long currTime = System.currentTimeMillis();
			//double difference = (currTime - initialTime) / 8000;
			double difference = 0.01; 
			initAngle1 += difference;
			initAngle2 += difference;

			lightPos[0] = (float) (-r1 * Math.cos(initAngle1) + terrain.X_OFFSET);
			lightPos[1] = (float) (-r2 * Math.sin(initAngle2));
			lightPos[2] = (float) (r1 * Math.sin(initAngle1)+ terrain.Z_OFFSET);
			if ((float) Math.abs(Math.sin(difference)) >= 0.7) {
				lightDiff[2] = (float) Math.abs(Math.sin(difference));
				lightSpec[2] = (float) Math.abs(Math.sin(difference));
			}

			if ((float) Math.abs(Math.sin(difference)) >= 0.85) {
				lightDiff[1] = (float) Math.abs(Math.sin(difference));
				lightSpec[1] = (float) Math.abs(Math.sin(difference));
			}
		}

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiff, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb, 0);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, gloAmb, 0);
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
	}

	public void setLightPos(float pos[]) {
		lightPos = pos;
		float mapPos[] = { lightPos[0] - terrain.X_OFFSET, lightPos[1],
				lightPos[2] - terrain.Z_OFFSET };
		
		float x1 = (float)Math.sqrt(mapPos[0]*mapPos[0] + mapPos[2] * mapPos[2]);
		initAngle1 = (float) Math.atan(mapPos[2] / mapPos[0]);
		initAngle2 = (float) Math.atan(lightPos[1] / x1);

		r1 = (float) Math.abs((mapPos[2] / Math.sin(initAngle1)));
		r2 = (float) Math.abs((lightPos[1] / Math.sin(initAngle2)));
		System.out.println(lightPos[0] + " " + lightPos[1] + " " + lightPos[2]);

	}

	public void toggleSun() {
		MOVING_SUN = !MOVING_SUN;
		initialTime = System.currentTimeMillis();
	}

	public void draw(GL2 gl) {
		gl.glPushMatrix();
		GLUT glut = new GLUT();
		gl.glTranslated(lightPos[0] - terrain.X_OFFSET, lightPos[1],
				lightPos[2] - terrain.Z_OFFSET);

		gl.glFrontFace(GL2.GL_CW);
		glut.glutSolidSphere(0.1, 50, 50);
		gl.glFrontFace(GL2.GL_CCW);

		gl.glPopMatrix();
	}
}
