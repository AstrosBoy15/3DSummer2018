package com.draglantix.shaders;

public class ContrastShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/filter_contrastVertex.glsl";
	private static final String FRAGMENT_FILE = "shaders/filter_contrastFragment.glsl";
	
	public ContrastShader() {
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
