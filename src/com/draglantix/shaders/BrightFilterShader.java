package com.draglantix.shaders;

public class BrightFilterShader extends ShaderProgram{
	
	private static final String VERTEX_FILE = "shaders/filter_simpleVertex.glsl";
	private static final String FRAGMENT_FILE = "shaders/filter_brightFragment.glsl";
	
	public BrightFilterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
