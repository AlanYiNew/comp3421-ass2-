package ass2.spec;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Torch {
	private float lightPos[] = null;
	private float lightDir[] = null;
	private float lightAmb[] = { 0.5f, 0.5f, 0.5f, 1 };
	private float lightDiff[] = { 1.0f, 1f, 1f, 1 };
	private float lightSpec[] = { 1.0f, 1f, 1f, 1 };
	private float gloAmb[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	private float spotAngle = 45;
	private float spotExponent = 1;
	private Terrain myTerrain;
	
	public final double AVATAR_DISPLACEMENT = 0.5;

	public Torch(Terrain t) {
		myTerrain = t;
	}
	
	public void setPos(double pos[], double front[]){
		lightPos = new float[]{(float) (pos[0]),(float) (pos[1] + this.AVATAR_DISPLACEMENT),(float) (pos[2]),1};
		lightDir = new float[]{(float) (front[0]), (float)(-AVATAR_DISPLACEMENT),(float) (front[2]),0};		
	}
	
	public void setLight(GL2 gl){		
       	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos,0); 
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmb,0);
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDiff,0);
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, lightSpec,0);
    	gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, gloAmb,0); 
    	

    	gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_CUTOFF, spotAngle);
    	gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION, lightDir,0);    
    	gl.glLightf(GL2.GL_LIGHT1, GL2.GL_SPOT_EXPONENT, spotExponent);
    	gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
	}
	
	public void draw(GL2 gl){
        GLUT glut = new GLUT();
        
    	gl.glPushMatrix();{
    		gl.glDisable(GL2.GL_LIGHTING);
    		gl.glRotated(-90, 0.0, 1.0, 0.0);
    		gl.glTranslated(0,0,-2.5);
    		gl.glColor3f(1.0f, 1.0f, 1.0f);       		
    		glut.glutWireCone(3.0 * Math.tan( spotAngle/180.0 * Math.PI ), 2.0, 20, 20);
    		gl.glEnable(GL2.GL_LIGHTING);
    	}gl.glPopMatrix();
	}
}
