package ass2.spec;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Avatar {
	private double[] pos;
	private double[] front;
	private double angle = 90;
	private double eye = 0.5;
	private Terrain terrain;

	private Light torch;
	private boolean onTorch = false;

	private static float POSITION_OFFSET = 0.2f;
	private static float ROTATE_MARGIN = 90;
	private static float AVATAR_RATIO = 0.3f;

	public Avatar(double x, double y, double z, Terrain terrain) {
		pos = new double[] { x, y, z };
		/*
		 * front = new double[] {x,y,z - 0.5}; side = new double[]{x-0.5,y,z};
		 */

		front = new double[3];

		front[0] = Math.cos(this.angle);
		front[2] = -Math.sin(this.angle);
		front[1] = y;

		torch = new Light();
		torch.setMode(Light.lightMode.TORCH);
		this.terrain = terrain;
	}

	public void toggleTorch() {
		onTorch = !onTorch;
	}

	public void draw(GL2 gl) {
		gl.glPushMatrix();
		{
			if (onTorch) {
				float _pos[] = new float[] { (float) (pos[0] + front[0]),
						(float) (pos[1] + 0.5), (float) (pos[2] + front[2]), 1 };
				torch.setLightPos(_pos);
				torch.setUpLight(gl);
				torch.draw(gl);
			}

			gl.glTranslated(pos[0], pos[1] + POSITION_OFFSET, pos[2]);
			gl.glRotated(Math.toDegrees(angle), 0, 1, 0);

			GLUT glut = new GLUT();
			gl.glFrontFace(GL2.GL_CW);
			glut.glutSolidTeapot(AVATAR_RATIO);
			gl.glFrontFace(GL2.GL_CCW);
		}
		gl.glPopMatrix();
	}

	public void vmove(double direction) {
		if (Math.abs(pos[0] + front[0] * direction) >= terrain.getOffset()[0]
				|| Math.abs(pos[2] + front[2] * direction) >= terrain
						.getOffset()[1])
			return;

		pos[0] += front[0] * direction;
		pos[2] += front[2] * direction;
		pos[1] = terrain.altitude(pos[0] + terrain.getOffset()[0], pos[2]
				+ terrain.getOffset()[1]);
	}

	public void xmove(double direction) {
		if (Math.abs(pos[0] + front[0] * direction) >= terrain.getOffset()[0]
				|| Math.abs(pos[2] + front[2] * direction) >= terrain
						.getOffset()[1])
			return;
		pos[0] += direction;
		pos[1] = terrain.altitude(pos[0] + terrain.getOffset()[0], pos[2]
				+ terrain.getOffset()[1]);
	}

	public void zmove(double direction) {
		if (Math.abs(pos[0] + front[0] * direction) >= terrain.getOffset()[0]
				|| Math.abs(pos[2] + front[2] * direction) >= terrain
						.getOffset()[1])
			return;
		pos[2] += direction;
		pos[1] = terrain.altitude(pos[0] + terrain.getOffset()[0], pos[2]
				+ terrain.getOffset()[1]);
	}

	public void rotate(double angle) {
		this.angle -= angle;
		front[0] = Math.cos(this.angle);
		front[2] = -Math.sin(this.angle);
	}

	public void look(double direction) {
		if (this.eye + direction > 1 || this.eye + direction < -1)
			return;

		this.eye += direction;
	}

	public float[] getPos() {
		float _pos[] = new float[] { (float) pos[0], (float) pos[1],
				(float) pos[2] };
		return _pos;
	}

	public float[] getFront() {
		float _pos[] = new float[] { (float) front[0], (float) front[1],
				(float) front[2] };
		return _pos;
	}
	
	public double getAngle(){
		return angle;
	}

	public double getEye() {
		return eye;
	}

	public void setUpMaterial(GL2 gl) {
		float matShine[] = { 70 };
		float lightAmb[] = { 0, 0, 0, 1 };
		float lightDiff[] = { 1.0f, 1.0f, 1.0f, 1 };
		float lightSpec[] = { 1.0f, 1.0f, 1.0f, 1 };

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, lightDiff, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, lightSpec, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, lightAmb, 0);
	}
}
