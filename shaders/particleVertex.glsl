#version 400 core

in vec2 position;

in mat4 viewMatrix;
in mat4 modelMatrix;
in vec4 texOffsets;
in float blendFactor;
in float colorBlendFactor;
in vec2 atlasOffset;
in vec3 currentColor;
in vec3 nextColor;

out vec2 textureCoords1;
out vec2 textureCoords2;
out float blend;
out float colorBlend;
out vec3 color1;
out vec3 color2;

uniform mat4 projectionMatrix;
uniform float numberOfRows;

void main(void) {
	
	vec2 textureCoords = position + vec2(0.5, 0.5);
	textureCoords.y = 1.0 - textureCoords.y;
	textureCoords /= numberOfRows;
	
	vec2 offset1 = texOffsets.xy;
	vec2 offset2 = texOffsets.zw;
	
	offset1.x += atlasOffset.x;
	offset1.y += atlasOffset.y;
	offset2.x += atlasOffset.x;
	offset2.y += atlasOffset.y;
	
	textureCoords1 = (textureCoords + offset1)/2;
	textureCoords2 = (textureCoords + offset2)/2;
	blend = blendFactor;
	colorBlend = colorBlendFactor;	
	
	color1 = currentColor;
	color2 = nextColor;

	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 0.0, 1.0);
 
}