package ass2.spec;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Light {
	private final int day = 1;
	private final int night = 0;
	private int current;
	private float radius = 12; 
	private long initialTime;
	private float lightPos[] = { -radius, 0, 0, 0 };
	private float lightAmb[] = { 0, 0, 0, 1 };
	private float lightDiff[] = { 1.0f, 0.85f, 0.7f, 1 };
	private float lightSpec[] = { 1.0f, 0.85f, 0.7f, 1 };
	private float gloAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };	
	private Terrain myTerrain;
	private float r1,r2,apha,zeta;
	
	public static enum lightMode{SUN,TORCH};
	lightMode mode;
	
	public Light(Terrain t){
		initialTime = System.currentTimeMillis();
		current = day;
		mode = lightMode.SUN;
	}
	
	public Light(Light.lightMode m){
		mode = m;
	}
	
	public void setMode(Light.lightMode m){
		mode = m;
	}
	
	public void setUpLight(GL2 gl) {
		
		if(mode == lightMode.SUN){
			double currTime = System.currentTimeMillis();
			double difference = (currTime - initialTime)/8000;
			lightPos[0] = (float) (Math.cos(difference + apha)*Math.cos(zeta)); 
			lightPos[2] = (float) (Math.cos(difference + apha)*Math.sin(zeta)); 
			lightPos[1] = (float) (r2 * Math.sin(zeta));
				if	((float)Math.abs( Math.sin(difference)) >= 0.7){
					lightDiff[2] = (float)Math.abs( Math.sin(difference));
					lightSpec[2] = (float)Math.abs( Math.sin(difference));
				}
				
				if	((float)Math.abs( Math.sin(difference)) >= 0.85){
					lightDiff[1] = (float)Math.abs( Math.sin(difference));
					lightSpec[1] = (float)Math.abs( Math.sin(difference));
				}
		}else if(mode == lightMode.TORCH){
			
		}
				
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, lightDiff, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightSpec, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmb, 0);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, gloAmb, 0);
		gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);		
	}
	
	public void setLightPos(float pos[]){
		lightPos = new float[]{pos[0] - myTerrain.X_OFFSET,pos[1],pos[2] - myTerrain.Z_OFFSET};
		r1 = (float) Math.sqrt(lightPos[0]*lightPos[0]+lightPos[2]*lightPos[2]);
		apha = - (float) Math.atan(pos[1]/r1);
		zeta = (float) Math.atan(pos[2]/pos[0]);
		r2 = (float) Math.sqrt(r1*r1 + pos[1] * pos[1]);
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

	public void toggleSun(){
		
	}
	
	private void setinitialLight(float pos[]){
		
	}
	
	private void setDayStartTime(){
		lightAmb = new float[]{ 0, 0, 0, 1 };
		lightDiff = new float[]{ 1.0f, 0.85f, 0.7f, 1 };
		lightSpec = new float[]{ 1.0f, 0.85f, 0.7f, 1 };	
	}
	
	private void setDayEndTime(){
		lightAmb = new float[]{ 0, 0, 0, 1 };
		lightDiff = new float[]{ 0, 0, 0, 1 };
		lightSpec = new float[]{ 0, 0, 0, 1 };	
	}
}