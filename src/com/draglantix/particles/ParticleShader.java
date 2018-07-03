package com.draglantix.particles;

import org.joml.Matrix4f;

import com.draglantix.shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/particleVShader.txt";
	private static final String FRAGMENT_FILE = "shaders/particleFShader.txt";

	private int location_numberOfRows;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "viewMatrix");
		bindAttribute(5, "modelMatrix");
		bindAttribute(9, "texOffsets");
		bindAttribute(10, "blendFactor");
		bindAttribute(11, "atlasOffset");
		bindAttribute(12, "currentColor");
		bindAttribute(13, "nextColor");
	}

	@Override
	protected void getAllUniformLocations() {
		location_numberOfRows = getUniformLocation("numberOfRows");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
	}

	public void loadNumberOfRows(float numRows) {
		super.loadFloat(location_numberOfRows, numRows);
	}
	
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}

}
