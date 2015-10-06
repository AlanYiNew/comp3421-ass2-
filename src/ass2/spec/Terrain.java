package ass2.spec;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.TextureData;

/**
 * COMMENT: Comment HeightMap
 *
 * @author malcolmr
 */
public class Terrain {

	private Dimension mySize;
	private double[][] myAltitude;
	private List<Tree> myTrees;
	private List<Road> myRoads;
	private float[] mySunlight;
	private int[] bufferIds;
	private int numOfMesh;
	FloatBuffer posData;
	FloatBuffer normalData;
	FloatBuffer textureData;
	float terrainPositions[];
	float terrainNormals[];
	float textureCoordinates[];
	float widthoffset;
	float lengthoffset;
	MyTexture myTexture;

	/**
	 * Create a new terrain
	 *
	 * @param width
	 *            The number of vertices in the x-direction
	 * @param depth
	 *            The number of vertices in the z-direction
	 */
	public Terrain(int width, int depth) {
		mySize = new Dimension(width, depth);
		myAltitude = new double[width][depth];
		myTrees = new ArrayList<Tree>();
		myRoads = new ArrayList<Road>();
		mySunlight = new float[3];
		numOfMesh = (width - 1) * (depth - 1) * 2;
		bufferIds = new int[1];
		widthoffset = (width - 1)/2;
		lengthoffset = (depth - 1)/2;
	}

	public Terrain(Dimension size) {
		this(size.width, size.height);
	}

	public Dimension size() {
		return mySize;
	}

	public List<Tree> trees() {
		return myTrees;
	}

	public List<Road> roads() {
		return myRoads;
	}

	public float[] getSunlight() {
		return mySunlight;
	}

	/**
	 * Set the sunlight direction.
	 * 
	 * Note: the sun should be treated as a directional light, without a
	 * position
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void setSunlightDir(float dx, float dy, float dz) {
		mySunlight[0] = dx;
		mySunlight[1] = dy;
		mySunlight[2] = dz;
	}

	/**
	 * Resize the terrain, copying any old altitudes.
	 * 
	 * @param width
	 * @param height
	 */
	public void setSize(int width, int height) {
		mySize = new Dimension(width, height);
		double[][] oldAlt = myAltitude;
		myAltitude = new double[width][height];

		for (int i = 0; i < width && i < oldAlt.length; i++) {
			for (int j = 0; j < height && j < oldAlt[i].length; j++) {
				myAltitude[i][j] = oldAlt[i][j];
			}
		}
	}

	/**
	 * Get the altitude at a grid point
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public double getGridAltitude(int x, int z) {
		return myAltitude[x][z];
	}

	/**
	 * Set the altitude at a grid point
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public void setGridAltitude(int x, int z, double h) {
		myAltitude[x][z] = h;
	}

	/**
	 * Get the altitude at an arbitrary point. Non-integer points should be
	 * interpolated from neighbouring grid points
	 * 
	 * TO BE COMPLETED
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public double altitude(double x, double z) {
		double altitude = 0;
		int xl = (int) Math.floor(x);
		int xr = xl + 1;
		int zl = (int) Math.floor(z);
		int zr = zl + 1;

		double alr = myAltitude[xl][zr];
		double all = myAltitude[xl][zl];
		double a1 = (z - zr) / (zl - zr) * (all - alr) + alr;

		double arl = myAltitude[xr][zl];
		double arr = myAltitude[xr][zr];
		double a2 = (z - zr) / (zl - zr) * (arl - arr) + arr;

		altitude = (x - xr) / (xl - xr) * (a1 - a2) + a2;

		return altitude;
	}

	/**
	 * Add a tree at the specified (x,z) point. The tree's y coordinate is
	 * calculated from the altitude of the terrain at that point.
	 * 
	 * @param x
	 * @param z
	 */
	public void addTree(double x, double z) {
		double y = altitude(x, z);
		Tree tree = new Tree(x, y, z);
		myTrees.add(tree);
	}

	/**
	 * Add a road.
	 * 
	 * @param x
	 * @param z
	 */
	public void addRoad(double width, double[] spine) {
		Road road = new Road(width, spine);
		myRoads.add(road);
	}

	public void draw(GL2 gl, TextureData data) {

		// Bind the buffer we want to use
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferIds[0]);

		// Enable three vertex arrays: coordinates, normal and texture.

		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

		//specify the pointer to refer to the vertices data in GL_ARRAY_BUFFER
		gl.glVertexPointer(3, // 3 coordinates per vertex
				GL2.GL_FLOAT, // each co-ordinate is a float
				0, // There are no gaps in data between co-ordinates
				0); // Co-ordinates are at the start of the current array buffer
		
		//specify the pointer to refer to the normal data in GL_ARRAY_BUFFER
		gl.glNormalPointer(GL2.GL_FLOAT, 0, terrainPositions.length
				* Float.BYTES); // colors are found after the position
		
		//specify the pointer to refer to the texture data in GL_ARRAY_BUFFER
		gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0,
				(terrainPositions.length + terrainNormals.length) * Float.BYTES);

		// Draw triangles with each Mesh having 3 vertices
		gl.glDrawArrays(GL2.GL_TRIANGLES, 0, numOfMesh * 3);

		// Disable these.
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

		// Unbind the buffer.
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
	}

	// Must be called in the init state
	public void init(GL2 gl) {
		gl.glEnable(GL2.GL_TEXTURE_2D);

		// Texture setting
		myTexture = new MyTexture(gl, "grass.jpg", "jpg", true);
		
		//The third argument should be GL_MODULATE if light is needed in the scene
		gl.glTexEnvf(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE,
				GL2.GL_MODULATE);

		//Each mesh has three vertices and each vertex has 3 coordinates
		terrainPositions = new float[(int) (numOfMesh * 3 * 3)];
		terrainNormals = new float[(int) (numOfMesh * 3 * 3)];
		textureCoordinates = new float[(int) (numOfMesh * 3 * 2)];
		
		//index for setting up values in array
		int curr = 0;

		//generating texture data array
		for (int i = 0; i < numOfMesh * 3 * 2; i += 6) {
			textureCoordinates[i] = 0.0f;
			textureCoordinates[i + 1] = 0.0f;
			textureCoordinates[i + 2] = 0.0f;
			textureCoordinates[i + 3] = 1.0f;
			textureCoordinates[i + 4] = 1.0f;
			textureCoordinates[i + 5] = 1.0f;
		}

		for (int i = 0; i < (int) mySize.getWidth() - 1; i++) {
			for (int j = 0; j < (int) mySize.getHeight() - 1; j++) {

				// compute normal for top left triangle of the mesh
				float[] v1 = { 1,
						(float) (myAltitude[i + 1][j] - myAltitude[i][j]), 0 };
				float[] v2 = { 0,
						(float) (myAltitude[i][j + 1] - myAltitude[i][j]), 1 };
				float[] n = MathUtil.crossProduct(v1, v2);

				// top left triangle of the mesh
				terrainNormals[curr] = n[0];
				terrainPositions[curr++] = i - widthoffset;
				terrainNormals[curr] = n[1];
				terrainPositions[curr++] = (float) myAltitude[i][j];
				terrainNormals[curr] = n[2];
				terrainPositions[curr++] = j - lengthoffset;

				terrainNormals[curr] = n[0];
				terrainPositions[curr++] = i + 1 - widthoffset;
				terrainNormals[curr] = n[1];
				terrainPositions[curr++] = (float) myAltitude[i + 1][j];
				terrainNormals[curr] = n[2];
				terrainPositions[curr++] = j - lengthoffset;

				terrainNormals[curr] = n[0];
				terrainPositions[curr++] = i - widthoffset;
				terrainNormals[curr] = n[1];
				terrainPositions[curr++] = (float) myAltitude[i][j + 1];
				terrainNormals[curr] = n[2];
				terrainPositions[curr++] = j + 1 - lengthoffset;

				// normal for bottom right triangle of the mesh
				float[] v3 = { -1,
						(float) (myAltitude[i][j + 1] - myAltitude[i + 1][j]),
						1 };
				float[] v4 = {
						0,
						(float) (myAltitude[i + 1][j + 1] - myAltitude[i + 1][j]),
						1 };
				n = MathUtil.crossProduct(v3, v4);

				// bottom right triangle of the mesh
				terrainNormals[curr] = n[0];
				terrainPositions[curr++] = i + 1 - widthoffset;
				terrainNormals[curr] = n[1];
				terrainPositions[curr++] = (float) myAltitude[i + 1][j];
				terrainNormals[curr] = n[2];
				terrainPositions[curr++] = j - lengthoffset;

				terrainNormals[curr] = n[0];
				terrainPositions[curr++] = i - widthoffset;
				terrainNormals[curr] = n[1];
				terrainPositions[curr++] = (float) myAltitude[i][j + 1];
				terrainNormals[curr] = n[2];
				terrainPositions[curr++] = j + 1 - lengthoffset;

				terrainNormals[curr] = n[0];
				terrainPositions[curr++] = i + 1 - widthoffset;
				terrainNormals[curr] = n[1];
				terrainPositions[curr++] = (float) myAltitude[i + 1][j + 1];
				terrainNormals[curr] = n[2];
				terrainPositions[curr++] = j + 1 - lengthoffset;
			}
		}

		// Java container for the coordinates array
		posData = Buffers.newDirectFloatBuffer(terrainPositions);
		normalData = Buffers.newDirectFloatBuffer(terrainNormals);
		textureData = Buffers.newDirectFloatBuffer(textureCoordinates);

		// Generate 1 VBO buffer and get its ID
		gl.glGenBuffers(1, bufferIds, 0);

		// Binding array buffer to the valid ID generated before in the
		// constructor
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferIds[0]);

		// Seperate enough space in the Buffer to put in data later
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, (terrainPositions.length
				+ terrainNormals.length + textureCoordinates.length)
				* Float.BYTES, null, GL2.GL_STATIC_DRAW);
		
		//put in data in the order posData, normalData and textureData;
		//second argument is the offset from which the data begins
		//Third argument is the number of bytes of the data
		//Last argument is the Buffer object we refer to
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, terrainPositions.length
				* Float.BYTES, posData);
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, terrainPositions.length
				* Float.BYTES, terrainNormals.length * Float.BYTES, normalData);
		gl.glBufferSubData(
				GL2.GL_ARRAY_BUFFER,
				(terrainPositions.length + terrainNormals.length) * Float.BYTES,
				textureCoordinates.length * Float.BYTES, textureData);
	}

	public int[] getBufferIds() {
		return bufferIds;
	}
}
