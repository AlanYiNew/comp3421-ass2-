#version 130

out vec3 normal; 
out vec4 world; 
out vec2 texture;

void main (void) {	
    world = gl_ModelViewMatrix * gl_Vertex;
    normal = vec3(normalize(gl_NormalMatrix * normalize(gl_Normal)));
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
}
