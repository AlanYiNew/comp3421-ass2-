package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class Camera {
	private float rotateX,rotateY,rotateZ;
	private float pos[] = null;
	private float target[] = null;
	private float focus[] = null;
	private float aspectRatio;
	public void setFocus(float x,float y, float z){
		focus = new float[]{x,y,z};
		pos = new float[]{x,y+1,z-1};
	}
	
	public float[] getPos() {
		return pos;
	}
	public void setPos(float[] pos) {
		this.pos = pos;
	}
	public float[] getTarget() {
		return target;
	}
	
	public void setTarget(float[] target) {
		this.target = target;
	}
	
	public void setAspect(float aspect) {
		this.aspectRatio = aspect;
	}
	
	public void setCamera(GL2 gl){
		GLU glu = new GLU();
		glu.gluLookAt(pos[0], pos[1], pos[2], focus[0], focus[1], focus[2], 0, 1, 0);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(90, aspectRatio, 1, 10);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		System.out.println(pos[0] + " " + pos[1] +" " +pos[2]);
		System.out.println(focus[0] + " " + focus[1] +" " +focus[2]);
	}
}
