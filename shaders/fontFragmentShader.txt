#version 400

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec3 fontColor;

void main(void){

	vec4 tex = texture(guiTexture,textureCoords);

	out_Color = vec4(fontColor.rgb, tex.a);

}