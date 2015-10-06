package ass2.spec;

public class Camera {
	private float rotateX,rotateY,rotateZ;
	private float pos[] = null;
	private float target[] = null;
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
	
	
}
