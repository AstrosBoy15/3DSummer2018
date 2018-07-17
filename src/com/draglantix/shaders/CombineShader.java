package com.draglantix.shaders;

public class CombineShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/filter_simpleVertex.glsl";
	private static final String FRAGMENT_FILE = "shaders/filter_combineFragment.glsl";
	
	private int location_colourTexture;
	private int location_highlightTexture;
	
	public CombineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_colourTexture = super.getUniformLocation("colourTexture");
		location_highlightTexture = super.getUniformLocation("highlightTexture");
	}
	
	public void connectTextureUnits(){
		super.loadInt(location_colourTexture, 0);
		super.loadInt(location_highlightTexture, 1);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
