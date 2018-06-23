package com.draglantix.particles;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.draglantix.entities.Camera;
import com.draglantix.shaders.ShaderProgram;
import com.draglantix.tools.Maths;

public class ParticleShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/particleVShader.txt";
	private static final String FRAGMENT_FILE = "shaders/particleFShader.txt";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_texOffset1;
	private int location_texOffset2;
	private int location_texCoordInfo;
	
	public ParticleShader() {
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
		location_texOffset1 = getUniformLocation("texOffset1");
		location_texOffset2 = getUniformLocation("texOffset2");
		location_texCoordInfo = getUniformLocation("texCoordInfo");
	}

	public void loadTextureCoordInfo(Vector2f offset1, Vector2f offset2, float numRows, float blend) {
		super.load2DVector(location_texOffset1, offset1);
		super.load2DVector(location_texOffset2, offset2);
		super.load2DVector(location_texCoordInfo, new Vector2f(numRows, blend));
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
