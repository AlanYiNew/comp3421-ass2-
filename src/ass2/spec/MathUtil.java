package ass2.spec;

public class MathUtil {
	public static float[] crossProduct(float[] v1, float[] v2){		
		float[] val = new float[3];
		val[0] = v1[1] * v2[2] - v1[2] * v2[1];
		val[1] = v1[2] * v2[0] - v1[0] * v2[2];
		val[2] = v1[0] * v2[1] - v1[1] * v2[0];
		return val;		
	}
}
