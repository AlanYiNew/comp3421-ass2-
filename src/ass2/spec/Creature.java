package ass2.spec;

import java.nio.FloatBuffer;

import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.gl2.GLUT;

public class Creature {
	private float[] creaturePosition;
	private float[] creatureNormal;
	private float[] creatureTexture;
	private int[] bufferIds = new int[1];
	private FloatBuffer posData;
	private FloatBuffer normalData;
	private FloatBuffer textureData;
	private final int FloatBYTE = 4;

	
	private int shaderprogram;
	
	private static final String VERTEX_SHADER = "vertex_shader.glsl";
    private static final String FRAGMENT_SHADER = "fragment_shader.glsl";
	
	public void init(GL2 gl) {
		creaturePosition = new float[] { 
				-0.5f, 0, 0.5f,// front
				0.5f, 0, 0.5f, 
				0.5f, 1, 0.5f, 
				-0.5f, 1, 0.5f,

				0.5f, 0, 0.5f,// right
				0.5f, 0, -0.5f, 
				0.5f, 1, -0.5f, 
				0.5f, 1, 0.5f,

				-0.5f, 1, 0.5f,// top
				0.5f, 1, 0.5f, 
				0.5f, 1, -0.5f, 
				-0.5f, 1, -0.5f,

				-0.5f, 0, 0.5f,// left
				-0.5f, 1, 0.5f, 
				-0.5f, 1, -0.5f, 
				-0.5f, 0, -0.5f,

				-0.5f, 0, -0.5f,// back
				-0.5f, 1, -0.5f,
				0.5f, 1, -0.5f, 
				0.5f, 0, -0.5f,

				-0.5f, 0, -0.5f,// bottom
				0.5f, 0, -0.5f, 
				0.5f, 0, 0.5f, 
				-0.5f, 0, 0.5f };
		
		creatureNormal = new float[] { 
				0, 0, 1f, 
				0, 0, 1f, 
				0, 0, 1f, 
				0, 0, 1f, 
				1f, 0, 0, 
				1f, 0, 0, 
				1f, 0, 0, 
				1f, 0, 0, 
				0, 1f, 0, 
				0, 1f, 0, 
				0, 1f, 0, 
				0, 1f, 0, 
				-1f, 0, 0,
				-1f, 0, 0,
				-1f, 0, 0,
				-1f, 0, 0,
				0, 0, -1f, 
				0, 0, -1f, 
				0, 0, -1f, 
				0, 0, -1f, 
				0, -1f, 0,
				0, -1f, 0,
				0, -1f, 0,
				0, -1f, 0
		};
		try{
			shaderprogram = Shader.initShaders(gl,VERTEX_SHADER,FRAGMENT_SHADER);
		}catch(Exception e){
		    e.printStackTrace();       	
		}

		creatureTexture = new float[] { 0, 0, 1, 0, 1, 1, 0, 1 };

		posData = Buffers.newDirectFloatBuffer(creaturePosition);
		normalData = Buffers.newDirectFloatBuffer(creatureNormal);
		textureData = Buffers.newDirectFloatBuffer(creatureTexture);

		// Binding array buffer to the valid ID generated before in the
		// constructor
		gl.glGenBuffers(1 ,bufferIds, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferIds[0]);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, (creaturePosition.length
				+ creatureNormal.length + creatureTexture.length)
				* FloatBYTE, null, GL2.GL_STATIC_DRAW);

		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, 0, creaturePosition.length
				* FloatBYTE, posData);

		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, creaturePosition.length * FloatBYTE,
				creatureNormal.length * FloatBYTE, normalData);
		
		gl.glBufferSubData(GL2.GL_ARRAY_BUFFER, (creaturePosition.length+creatureNormal.length) * FloatBYTE,
				creatureTexture.length * FloatBYTE, textureData);
	}

	public void draw(GL2 gl,int textureArray[]) {
		
		// Bind the buffer we want to use
				gl.glUseProgram(shaderprogram);
				gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, bufferIds[0]);

				// Enable three vertex arrays: coordinates, normal and texture.

				gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
				gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
				gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

				gl.glVertexPointer(3, // 3 coordinates per vertex
						GL2.GL_FLOAT, // each co-ordinate is a float
						0, // There are no gaps in data between co-ordinates
						0); // Co-ordinates are at the start of the current array buffer
				
				//specify the pointer to refer to the normal data in GL_ARRAY_BUFFER
				gl.glNormalPointer(GL2.GL_FLOAT, 0, creatureNormal.length
						* FloatBYTE); // colors are found after the position
				
				//specify the pointer to refer to the texture data in GL_ARRAY_BUFFER
				gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0,
						(creaturePosition.length + creatureNormal.length) * FloatBYTE);
				gl.glEnable(GL2.GL_TEXTURE_2D);
				gl.glBindTexture(GL2.GL_TEXTURE_2D, textureArray[1]);
				gl.glDrawArrays(GL2.GL_QUADS, 0, 4);
				gl.glBindTexture(GL2.GL_TEXTURE_2D, textureArray[0]);
				gl.glDisable(GL2.GL_TEXTURE_2D);
				gl.glDrawArrays(GL2.GL_QUADS, 4, 20);
				gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
				gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
				gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

				gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
				gl.glUseProgram(0);
				
	}
}
