package ass2.spec;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

class CrossSection{
	ArrayList<double[]> vertices = null;
	
	public CrossSection(){
		vertices = new ArrayList<double[]>();
	}
	
	public void add(double[] p){
		vertices.add(p);
	}
	
	public double[] get(int index){
		return vertices.get(index);
	}
	
	public int size(){
		return vertices.size();
	}
	
	public double[] getNormal(){
		double[] n = new double[3];
		
		int size = vertices.size();
		for(int i = 0; i < size; i++){
			double p0[] = vertices.get(i);
			double p1[] = vertices.get((i+1)%size);
			
			n[0] += (p0[1] - p1[1]) * (p0[2] + p1[2]);
			n[1] += (p0[2] - p1[2]) * (p0[0] + p1[0]);
			n[2] += (p0[0] - p1[0]) * (p0[1] + p1[1]);
		}
		
		return n;
	}
}

/**
 * COMMENT: Comment Road 
 *
 * @author malcolmr
 */
public class Road {

    private List<Double> myPoints;
    private double myWidth;
    private Terrain terrain;
    ArrayList<CrossSection> crossSecVertices = null;
    ArrayList<CrossSection> meshes = null;
    private double[][] crossSec = null;
    private ArrayList<double[]> spine = null;
    public int SLICES = 50;
    
    /** 
     * Create a new road starting at the specified point
     */
    public Road(double width, double x0, double y0) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        myPoints.add(x0);
        myPoints.add(y0);   
    }

    public void setTerrain(Terrain t)
    {
    	terrain = t;
    }
    
    public void initExtrusion(){
        crossSecVertices = new ArrayList<CrossSection>();
        spine = new ArrayList<double[]>();        
        crossSec = new double[][]{
        	{-myWidth/2,0,0,1},
        	{myWidth/2,0,0,1}
        };
        this.createSpine();
        this.createCrossSectionOnSpine();
    }
    /**
     * Create a new road with the specified spine 
     *
     * @param width
     * @param spine
     */
    public Road(double width, double[] spine) {
        myWidth = width;
        myPoints = new ArrayList<Double>();
        for (int i = 0; i < spine.length; i++) {
            myPoints.add(spine[i]);
        }
    }

    /**
     * The width of the road.
     * 
     * @return
     */
    public double width() {
        return myWidth;
    }
    
    public double[] multiply(double[][] matrix, double[] vertex){
    	double m[] = new double[vertex.length];
    	for(int i = 0; i < matrix.length; i++){
    		for(int j = 0;  j < matrix[i].length; j++){
    			m[i] += matrix[i][j] * vertex[j];
    		}
    	}
    	return m;
    }
    
    public void createCrossSectionOnSpine(){
    	double pCurr[] = new double[]{spine.get(0)[0]-terrain.X_OFFSET,
    			terrain.altitude(spine.get(0)[0],spine.get(0)[1])+0.2,
    			spine.get(0)[1]-terrain.Z_OFFSET};
    	
    	double pNext[] = new double[]{spine.get(1)[0] - terrain.X_OFFSET,
    			terrain.altitude(spine.get(1)[0], spine.get(1)[1])+0.2,
    			spine.get(1)[1] - terrain.Z_OFFSET};
    	
    	double pPrev[] = pCurr;
    	
    	crossSecVertices.add(getTransPoint(pCurr,pNext,pPrev));
    	
    	for(int i = 1; i < spine.size()-1; i++){
    		pPrev = pCurr;
    		pCurr = pNext;
    		pNext = new double[]{spine.get(i+1)[0]-terrain.X_OFFSET,
    				terrain.altitude(spine.get(i+1)[0],spine.get(i+1)[1])+0.2,
    				spine.get(i+1)[1]-terrain.Z_OFFSET};
    		crossSecVertices.add(getTransPoint(pCurr,pNext,pPrev));	
    	}
    	
    	pPrev = pCurr;
		pCurr = pNext;
		crossSecVertices.add(getTransPoint(pCurr,pNext,pPrev));	
    }
    
    public void createSpine(){
    	double inc = (double)(size())/SLICES;

    	for(int i = 0; i < SLICES; i++){
	    	double[] p = point(i* inc);
	    	spine.add(p);		
    	}
    	
    }
    
    public CrossSection getTransPoint(double[] pCurr,double[] pNext,double[] pPrev){
    	double m[][] = new double[4][4];
    	
    	// phi
    	m[0][3] = pCurr[0];
    	m[1][3] = pCurr[1];
    	m[2][3] = pCurr[2];
    	m[3][3] = 1;
    	
    	// k
    	m[0][2] = pNext[0] - pPrev[0];
    	m[1][2] = pNext[1] - pPrev[1];
    	m[2][2] = pNext[2] - pPrev[2];
    	m[3][2] = 0;
    	
    	// normalize
    	double n = Math.sqrt(m[0][2] * m[0][2] + m[1][2] * m[1][2] + m[2][2] * m[2][2]);
    	m[0][2] /= n;
    	m[1][2] /= n;
    	m[2][2] /= n;
    	
    	// get perpendicular
    	m[0][1] = -m[1][2];
    	m[1][1] = m[0][2];
    	m[2][1] = 0;
    	m[3][1] = 0;
    	
    	// i 
    	m[0][0] = m[1][2] * m[2][1] - m[1][1] * m[2][2];
    	m[1][0] = m[2][2] * m[0][1] - m[2][1] * m[0][2];
    	m[2][0] = m[0][2] * m[1][1] - m[0][1] * m[1][2];
    	m[3][0] = 0;
    	
    	CrossSection cs = new CrossSection();
    	
    	
    	for(int i = 0; i < crossSec.length; i++){
    		double[] p = multiply(m,crossSec[i]);
    		System.out.println(p[0] + " " + p[1] + " " + p[2]);
    		cs.add(p);
    	}
    	
    	return cs;
    }

    /**
     * Add a new segment of road, beginning at the last point added and ending at (x3, y3).
     * (x1, y1) and (x2, y2) are interpolated as bezier control points.
     * 
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     */
    public void addSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        myPoints.add(x1);
        myPoints.add(y1);
        myPoints.add(x2);
        myPoints.add(y2);
        myPoints.add(x3);
        myPoints.add(y3);        
    }
    
    /**
     * Get the number of segments in the curve
     * 
     * @return
     */
    public int size() {
        return myPoints.size() / 6;
    }

    /**
     * Get the specified control point.
     * 
     * @param i
     * @return
     */
    public double[] controlPoint(int i) {
        double[] p = new double[2];
        p[0] = myPoints.get(i*2);
        p[1] = myPoints.get(i*2+1);
        return p;
    }
    
    /**
     * Get a point on the spine. The parameter t may vary from 0 to size().
     * Points on the kth segment take have parameters in the range (k, k+1).
     * 
     * @param t
     * @return
     */
    public double[] point(double t) {
        int i = (int)Math.floor(t);
        t = t - i;
        
        i *= 6;
        
        double x0 = myPoints.get(i++);
        double y0 = myPoints.get(i++);
        double x1 = myPoints.get(i++);
        double y1 = myPoints.get(i++);
        double x2 = myPoints.get(i++);
        double y2 = myPoints.get(i++);
        double x3 = myPoints.get(i++);
        double y3 = myPoints.get(i++);
        
        double[] p = new double[2];

        p[0] = b(0, t) * x0 + b(1, t) * x1 + b(2, t) * x2 + b(3, t) * x3;
        p[1] = b(0, t) * y0 + b(1, t) * y1 + b(2, t) * y2 + b(3, t) * y3;        
        
        return p;
    }
    
    /**
     * Calculate the Bezier coefficients
     * 
     * @param i
     * @param t
     * @return
     */
    private double b(int i, double t) {
        
        switch(i) {
        
        case 0:
            return (1-t) * (1-t) * (1-t);

        case 1:
            return 3 * (1-t) * (1-t) * t;
            
        case 2:
            return 3 * (1-t) * t * t;

        case 3:
            return t * t * t;
        }
        
        // this should never happen
        throw new IllegalArgumentException("" + i);
    }

    public void draw(GL2 gl){
    	
    	gl.glPushMatrix();
 //   		gl.glPolygonOffset(-1,-1);
    		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
    		for(int i = 0; i < crossSecVertices.size()-1; i++){
    			CrossSection ms0 = crossSecVertices.get(i);
    			CrossSection ms1 = crossSecVertices.get(i+1);
    			
    			float[] l0 = {(float) (ms0.get(0)[0] - ms0.get(1)[0]),(float) (ms0.get(0)[1] - ms0.get(1)[1]),(float) (ms0.get(0)[2] - ms0.get(1)[2])};
    			float[] l1 = {(float) (ms1.get(1)[0] - ms0.get(1)[0]),(float) (ms1.get(1)[1] - ms0.get(1)[1]),(float) (ms1.get(1)[2] - ms0.get(1)[2])};
    			
    			float[] cp = MathUtil.crossProduct(l1, l0);
    			cp = MathUtil.normalise(cp);
    			
    			gl.glNormal3d(cp[0], cp[1], cp[2]);
    			gl.glBegin(GL2.GL_QUADS);
    			{
    				gl.glVertex3d(ms0.get(0)[0], ms0.get(0)[1], ms0.get(0)[2]);
    				gl.glVertex3d(ms0.get(1)[0], ms0.get(1)[1], ms0.get(1)[2]);
    				gl.glVertex3d(ms1.get(1)[0], ms1.get(1)[1], ms1.get(1)[2]);
    				gl.glVertex3d(ms1.get(0)[0], ms1.get(0)[1], ms1.get(0)[2]);
    			}
    			gl.glEnd();
    		}
    	
    		/*gl.glBegin(GL2.GL_QUAD_STRIP);
    		{
    			for(CrossSection ms : crossSecVertices){
    				gl.glVertex3d(ms.get(0)[0], ms.get(0)[1], ms.get(0)[2]);
    				gl.glVertex3d(ms.get(1)[0], ms.get(1)[1], ms.get(1)[2]);
    		}
    		*/
    		
    		gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
    		gl.glBegin(GL2.GL_LINE_STRIP);
    		{
    			for(double[] p : spine){
    				gl.glVertex3d(p[0]-terrain.X_OFFSET,terrain.altitude(p[0], p[1]),p[1]-terrain.Z_OFFSET);
    			}
    		}
    		gl.glEnd();
  //  		gl.glPolygonOffset(0,0);
    	gl.glPopMatrix();  	
    }
    
}