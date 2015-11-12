//shader with texture
#version 130

in vec3 normal;
in vec4 world;
in vec2 texture;
uniform int light;
uniform sampler2D color;

void main (void) {	
    vec4 ambient, globalAmbient;
    
	ambient =  gl_LightSource[light].ambient * gl_FrontMaterial.ambient;
	globalAmbient = gl_LightModel.ambient * gl_FrontMaterial.ambient;

	vec3 n, lightDir; 
	vec4 diffuse;
	float NdotL;
	
	n = normalize(normal);
	
	lightDir = normalize(vec3(gl_LightSource[light].position - world));
    NdotL = max(dot(n, lightDir), 0.0); 
    diffuse = NdotL * gl_FrontMaterial.diffuse * gl_LightSource[light].diffuse; 

    vec4 specular = vec4(0.0,0.0,0.0,1);
    float NdotHV;
    float NdotR;
    vec3 dirToView = normalize(vec3(-world));
    
    vec3 R = normalize(reflect(-lightDir,n)); 
    vec3 H =  normalize(lightDir+dirToView); 
    
	if (NdotL > 0.0) {
		NdotR = max(dot(R,dirToView ),0.0);
		specular = gl_FrontMaterial.specular * gl_LightSource[light].specular * pow(NdotR,gl_FrontMaterial.shininess);
	    
	}
	
	specular = clamp(specular,0,1);
    gl_FragColor = texture2D(color,texture) * (gl_FrontMaterial.emission + globalAmbient + ambient + diffuse) + specular;
	
}
