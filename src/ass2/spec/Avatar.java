package ass2.spec;
import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;


public class Avatar {
	private double[] pos;
	private double[] front;
	private double[] side;
	private double angle = 90;
	private Terrain terrain;
	
	public Avatar(double x, double y, double z,Terrain terrain){
		pos = new double[] {x,y,z};
		front = new double[] {x,y,z - 0.5};
		side = new double[]{x-0.5,y,z};
		this.terrain = terrain;
	}
	
	public void draw(GL2 gl){
		gl.glPushMatrix();
		{
			gl.glTranslated(pos[0],pos[1]+ 0.4,pos[2]);
			gl.glRotated(Math.toDegrees(angle),0,1,0);
			GLUT glut = new GLUT();
			gl.glFrontFace(GL2.GL_CW);
			glut.glutSolidTeapot(0.5);
			gl.glFrontFace(GL2.GL_CCW);
		}
		gl.glPopMatrix();
	}
	
	public void vmove(double direction){
		if(Math.abs(pos[0] + front[0] * direction) >= terrain.getOffset()[0] || 
				Math.abs(pos[2] + front[2] * direction) >= terrain.getOffset()[1])
			return;
		
		pos[0] += front[0] * direction;
		pos[2] += front[2] * direction;
		pos[1] = terrain.altitude(pos[0]+terrain.getOffset()[0],pos[2]+terrain.getOffset()[1]);
	}
	
	public void hmove(double direction){
		if(Math.abs(pos[0] + side[0] * direction) >= terrain.getOffset()[0] || 
				Math.abs(pos[2] + side[2] * direction ) >=  terrain.getOffset()[1])
			return;
		
		
		pos[0] += side[0] * direction;
		pos[2] += side[2] * direction;
		pos[1] = terrain.altitude(pos[0]+terrain.getOffset()[0], pos[2]+terrain.getOffset()[1]);
	}
	
	public void rotate(double angle){
		this.angle -= angle;
		front[0] = Math.cos(this.angle);
		front[2] = -Math.sin(this.angle);
		side[0] = Math.cos(this.angle - 90);
		side[2] = - Math.sin(this.angle - 90);
	}
	
	public float[] getPos(){
		float _pos[] = new float[]{(float) pos[0],(float) pos[1],(float) pos[2]};	
		return _pos;
	}
	
	public float[] getFront(){
		float _pos[] = new float[]{(float) front[0],(float) front[1],(float) front[2]};	
		return _pos;
	}
	
	public float[] getSide(){
		float _pos[] = new float[]{(float) side[0],(float) side[1],(float) side[2]};	
		return _pos;
	}
	
	public void setUpMaterial(GL2 gl) {
		float matShine[] =  {70};
		float lightAmb[] = {0,0,0,1};
		float lightDiff[] = {1.0f,1.0f,1.0f,1};
		float lightSpec[] = {1.0f,1.0f,1.0f,1};
		
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_SHININESS,matShine,0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_DIFFUSE,lightDiff,0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_SPECULAR,lightSpec,0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK,GL2.GL_AMBIENT,lightAmb,0);
	}
}
