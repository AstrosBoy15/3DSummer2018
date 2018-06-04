#version 400 core

in vec2 textureCoords;

out vec4 out_Color;

uniform vec3 entityID;

void main(void) {

	out_Color = vec4(entityID, 1.0);

}