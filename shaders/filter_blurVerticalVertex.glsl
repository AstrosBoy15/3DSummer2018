#version 150

in vec2 position;

out vec2 blurTextureCoords[11];

uniform float targetHeight;

void main(void){

	gl_Position = vec4(position, 0.0, 1.0);
	vec2 centerTexCoords = position * 0.5 + 0.5;
	float pixelSize = 1.0 / targetHeight;
	
	for(int i = -11; i <= 11; i++) {
		blurTextureCoords[i+11] = centerTexCoords + vec2(0.0, pixelSize * i);
	}

}