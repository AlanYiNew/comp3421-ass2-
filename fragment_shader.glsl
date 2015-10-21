#version 130



in vec3 N;
in vec4 v;

void main (void) {	
   vec4 ambient, globalAmbient;
    
	ambient =  gl_LightSource[0].ambient * gl_FrontMaterial.ambient;
	globalAmbient = gl_LightModel.ambient * gl_FrontMaterial.ambient;

	vec3 normal, lightDir; 
	vec4 diffuse;
	float NdotL;
	
	normal = normalize(N);
	
	
	lightDir = normalize(vec3(gl_LightSource[0].position - v));
    NdotL = max(dot(normal, lightDir), 0.0); 
    diffuse = NdotL * gl_FrontMaterial.diffuse * gl_LightSource[0].diffuse; 

    vec4 specular = vec4(0.0,0.0,0.0,1);
    float NdotHV;
    float NdotR;
    vec3 dirToView = normalize(vec3(-v));
    
    vec3 R = normalize(reflect(-lightDir,normal)); 
    vec3 H =  normalize(lightDir+dirToView); 
   
    /* compute the specular term if NdotL is  larger than zero */
    
	if (NdotL > 0.0) {
		NdotR = max(dot(R,dirToView ),0.0);
		specular = gl_FrontMaterial.specular * gl_LightSource[0].specular * pow(NdotHV,gl_FrontMaterial.shininess);
	    
	}
	
	specular = clamp(specular,0,1);
    gl_FragColor = gl_FrontMaterial.emission + globalAmbient + ambient + diffuse + specular;
	
}
