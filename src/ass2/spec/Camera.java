package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class Camera {

	private float pos[] = null;
	private float target[] = null;
	private float focus[] = null;
	private float aspectRatio;
	
	private final float NEAR = 0.1f;
	private final float FAR = 10f;
	private final float fovy = 90;
	
	
	public Avatar avatar;
	public Terrain terrain;
	
	public Camera(Avatar ava, Terrain ter){
		avatar = ava;
		terrain = ter;
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
		
		pos = avatar.getPos();
		target = avatar.getFront();
		
		float eyeX = (float) (pos[0] - target[0]);
		float eyeY = (float) (pos[1] + avatar.getEye());
		float eyeZ = (float) (pos[2] - target[2]);
		
		float centerX = (float)(pos[0] + target[0]);
		float centerZ = (float)(pos[2] + target[2]);
		float centerY = (float)(pos[1] - avatar.getEye());
		
		GLU glu = new GLU();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();	
		glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0, 1, 0);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(fovy, aspectRatio, NEAR, FAR);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}
}
