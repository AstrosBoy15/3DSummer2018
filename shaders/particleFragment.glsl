#version 400 core

in vec2 textureCoords1;
in vec2 textureCoords2;
in float blend;

in vec3 color1;
in vec3 color2;
in float colorBlend;

out vec4 out_Color;

uniform sampler2D particleTexture;

void main(void) {

	vec4 texColor1 = texture(particleTexture, textureCoords1);
	vec4 texColor2 = texture(particleTexture, textureCoords2);

	out_Color = mix(texColor1, texColor2, blend);
	
	vec3 mixColor = mix(color1, color2, colorBlend);
	
	out_Color = vec4(out_Color.xyz * mixColor.xyz, out_Color.w);
}