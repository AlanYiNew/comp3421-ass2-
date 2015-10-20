package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class Camera {

	private float pos[] = null;
	private float aspectRatio;
	
	private final float NEAR = 0.1f;
	private final float FAR = 10f;
	private final float fovy = 90;
	private float[] eye = new float[3];
	private float[] center = new float[3];
	private	float[] cameraUp = {0,1,0};
	public float[] getCameraUp() {
		return cameraUp;
	}

	public static enum cameraMode{FIRST,THIRD,FREE};
	private cameraMode mode;
	
	public Avatar avatar;
	public Terrain terrain;
	
	public Camera(Avatar ava, Terrain ter){
		avatar = ava;
		terrain = ter;
		mode = cameraMode.THIRD;
	}
	
	public float[] getPos() {
		return pos;
	}
	public void setPos(float[] pos) {
		this.pos = pos;
	}
	
	public void setAspect(float aspect) {
		this.aspectRatio = aspect;
	}
	
	public void setCamera(GL2 gl){
		if (mode == cameraMode.THIRD){
			pos = avatar.getPos();
			float[] target = avatar.getFront();
			
			eye[0] = (float) (pos[0] - target[0]);
			eye[1] = (float) (pos[1] + 0.5 + avatar.getEye());
			eye[2] = (float) (pos[2] - target[2]);
			
			center[0] = (float)(pos[0] + target[0]);
			center[2] = (float)(pos[2] + target[2]);
			center[1] = (float)(pos[1] + 0.5 - avatar.getEye());
			
		} else if(mode == cameraMode.FIRST){
			float[] target = avatar.getFront();
			eye = avatar.getPos();
			center = target;
		} else if(mode == cameraMode.FREE){
			
		}
		
		GLU glu = new GLU();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();	
		
		glu.gluLookAt(eye[0], eye[1], eye[2], center[0], center[1], center[2], 0, 1, 0);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(fovy, aspectRatio, NEAR, FAR);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	
	}

	public float[] getEye() {
		return eye;
	}

	public float[] getCenter() {
		return center;
	}

	public void changeMode() {
		if (mode == cameraMode.THIRD){
			mode = cameraMode.FIRST;
		}	else if (mode == cameraMode.FIRST){
			mode = cameraMode.FREE;
		}	else{
			mode = cameraMode.THIRD;
		}
		
	}

	public cameraMode getMode() {
		return mode;
	}
}
