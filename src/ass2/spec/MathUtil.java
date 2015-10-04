package ass2.spec;

public class MathUtil {
	public static double[] crossProduct(double[] v1, double[] v2){		
		double[] val = new double[3];
		val[0] = v1[1] * v2[2] - v1[2] * v2[1];
		val[1] = v1[2] * v2[0] - v1[0] * v2[2];
		val[2] = v1[0] * v2[1] - v1[1] * v2[0];
		return val;		
	}
	
	public static double[] normalise(double[] v){
		double length = Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
		double val[] = {v[0]/length,v[1]/length,v[2]/length};
		return val;
	}
}
