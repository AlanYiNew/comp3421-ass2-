package ass2.spec;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

/**
 * COMMENT: Comment Tree
 *
 * @author malcolmr
 */
public class Tree {

	private double[] myPos;
	private double[] initialDirection;
	private double factor = 0.3;
	private String pattern;
	private String[] generatedPattern;
	private double treeHeight = 1;
	private Terrain terrain;

	public Tree(double x, double y, double z) {
		myPos = new double[3];
		myPos[0] = x;
		myPos[1] = y;
		myPos[2] = z;
		initialDirection = new double[] { 0, 1, 0 };// up;
	}

	public double[] getPosition() {
		return myPos;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public void draw(GL2 gl, int iteration) {
		generatePattern(iteration);
		//setUpMaterial(gl);
		String extendedPattern = generatedPattern[iteration - 1];
		gl.glPushMatrix();	
		gl.glTranslated(myPos[0]-terrain.X_OFFSET,myPos[1],myPos[2]-terrain.Z_OFFSET);
		
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_AMBIENT, 1);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_DIFFUSE, 1);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SPECULAR, 1);
		gl.glPushMatrix();
		
		GLUT glut = new GLUT();
		gl.glRotated(-90,1,0,0);
		gl.glFrontFace(GL2.GL_CW);
		glut.glutSolidCylinder(0.1, treeHeight, 16, 16);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glPopMatrix();
		
		gl.glTranslated(0,treeHeight*0.8,0);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		
		for (int i = 0; i < extendedPattern.length(); i++) {
			switch (extendedPattern.charAt(i)) {
			case '[':
				gl.glPushMatrix();
				break;
			case ']':
				gl.glPopMatrix();
				break;
			case 'a':
				gl.glTranslated(0, factor, 0);
				drawBallAt(gl, new double[] { 0, 0, 0 });
				break;
			case 'l':
				gl.glRotated(30, 0, 0, 1);
				break;
			case 'r':
				gl.glRotated(-30, 0, 0, 1);
				break;
			case 'f':
				gl.glRotated(30, 1, 0, 0);
				break;
			case 'b':
				gl.glRotated(-30, 1, 0, 0);
				break;
			}
		}
		gl.glDisable(GL2.GL_TEXTURE_2D);
		gl.glPopMatrix();
	}

	private void generatePattern(int iteration) {
		generatedPattern = new String[20];
		generatedPattern[0] = pattern;
		for (int j = 1; j <= iteration; j++) {
			generatedPattern[j] = new String("");
			for (int i = 0; i < generatedPattern[j-1].length(); i++) {
				switch (generatedPattern[j-1].charAt(i)) {
				case 'l':
					generatedPattern[j]=generatedPattern[j].concat("l");
					break;
				case 'f':
					generatedPattern[j]=generatedPattern[j].concat("f");
					break;
				case 'b':
					generatedPattern[j]=generatedPattern[j].concat("b");
					break;
				case 'r':
					generatedPattern[j]=generatedPattern[j].concat("r");
					break;
				case '[':
					generatedPattern[j]=generatedPattern[j].concat("[");
					break;
				case ']':
					generatedPattern[j]=generatedPattern[j].concat("]");
					break;
				case 'a':
					generatedPattern[j] = generatedPattern[j].concat("a[la][ra][ba][fa]");
					break;			
				default:
					break;
				}
			}
		}
	}

	private void drawBallAt(GL2 gl, double[] pos) {
		gl.glPushMatrix();
		gl.glTranslated(pos[0], pos[1], pos[2]);
		GLUT glut = new GLUT();
		gl.glFrontFace(GL2.GL_CW);
		glut.glutSolidSphere(0.1, 5, 5);
		gl.glFrontFace(GL2.GL_CCW);
		gl.glPopMatrix();
	}
	
	private void setUpMaterial(GL2 gl) {
		float matShine[] = { 70 };
		float lightAmb[] = { 0, 0, 0, 1 };
		float lightDiff[] = { 1.0f, 1.0f, 1.0f, 1 };
		float lightSpec[] = { 1.0f, 1.0f, 1.0f, 1 };

		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, matShine, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, lightDiff, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, lightSpec, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, lightAmb, 0);
	}

	public void addTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
}
