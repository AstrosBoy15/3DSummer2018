package com.draglantix.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.draglantix.entities.Camera;
import com.draglantix.tools.Maths;

public class SelectionShader extends ShaderProgram {

	private final static String VERTEX_FILE = "shaders/selectionVertexShader.txt";
	private final static String FRAGMENT_FILE = "shaders/selectionFragmentShader.txt";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_entityID;
	
	public SelectionShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_entityID = getUniformLocation("entityID");
	}
	
	public void loadEntityID(Vector3f color) {
		loadVector(location_entityID, color);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
