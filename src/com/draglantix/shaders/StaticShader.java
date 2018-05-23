package com.draglantix.shaders;

import org.joml.Matrix4f;

import com.draglantix.entities.Camera;
import com.draglantix.tools.Maths;

public class StaticShader extends ShaderProgram{

	private static final String VERTEX_FILE = "./shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "./shaders/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");	
		super.bindAttribute(1, "textureCoords");	
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	

	public void loadProjectionnMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}

}
