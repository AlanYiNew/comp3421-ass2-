package ass2.spec;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

public class Camera {

	private float pos[] = null;
	private float aspectRatio;
	
	private final float AVATAR_FOVY = 90;
	private final float FREE_VIEW_FOVY = 90;
	
	private float near = 0.1f;
	private float far = 10f;
	private float fovy = 90;
	private float[] eye = new float[3];
	private float[] center = new float[3];
	private	float[] cameraUp = {0,1,0};

	private float _pos[];
	private float _front[];
	private float _look;
	private double _angle;

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
			center[1] = (float)(pos[1] + 0.5 - avatar.getEye());
			center[2] = (float)(pos[2] + target[2]);
			
			
		} else if(mode == cameraMode.FIRST){
			
			eye = avatar.getPos();
			eye[1] += 0.5;
			float[] front = avatar.getFront();
			center = new float[]{eye[0] + front[0],(float) (eye[1] + avatar.getEye()),eye[2] + front[2]};
			
		} else if(mode == cameraMode.FREE){
			
			eye = this._pos;
			float[] front = _front;
			
			if (Math.abs(eye[0]) >= terrain.getOffset()[0] || Math.abs(eye[2]) >= terrain.getOffset()[1])
				eye[1] = 0.5f;
			else
				eye[1] = (float) terrain.altitude(eye[0] + terrain.getOffset()[0], eye[2] + terrain.getOffset()[1]) + 0.5f;

			center = new float[]{eye[0] + front[0],(float) (eye[1]) + _look,eye[2] + front[2]};
			
		}
		
		GLU glu = new GLU();
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();	
		
		glu.gluLookAt(eye[0], eye[1], eye[2], center[0], center[1], center[2], 0, 1, 0);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(fovy, aspectRatio, near, far);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	
	}
	
	public float[] getEye() {
		return eye;
	}

	public float[] getCenter() {
		return center;
	}
	
	public float[] getCameraUp() {
		return cameraUp;
	}

	public void changeMode(){
		if (mode == cameraMode.THIRD){
			mode = cameraMode.FIRST;
			fovy = AVATAR_FOVY;
			near = 0.1f;
			far = 10;
			
		} else if (mode == cameraMode.FIRST){
			mode = cameraMode.FREE;
			fovy = FREE_VIEW_FOVY;
			near = 0.1f;
			far = 15;
			_pos = avatar.getPos();
			_front = avatar.getFront();
			_angle = avatar.getAngle();
			_look = 0;
			
		} else{
			mode = cameraMode.THIRD;
			fovy = AVATAR_FOVY;
			near = 0.1f;
			far = 10;
		}	
	}

	public cameraMode getMode() {
		return mode;
	}
	
	public void vmove(double direction){
		
		_pos[0] += _front[0] * direction;
		_pos[2] += _front[2] * direction;
		
	}
	
	public void rotate(double angle) {
		this._angle -= angle;
		_front[0] = (float) Math.cos(this._angle);
		_front[2] = (float) -Math.sin(this._angle);
	}
	
	public void look(double direction) {
		if (this._look + direction > 1 || this._look < -1)
			return;

		this._look += direction;
	}
}
