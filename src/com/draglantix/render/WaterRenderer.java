package com.draglantix.render;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.draglantix.entities.Camera;
import com.draglantix.entities.Light;
import com.draglantix.models.RawModel;
import com.draglantix.shaders.WaterShader;
import com.draglantix.tools.Fbo;
import com.draglantix.tools.Loader;
import com.draglantix.tools.Maths;
import com.draglantix.tools.Timer;
import com.draglantix.water.WaterTile;

public class WaterRenderer {

	private static final String DUDV_MAP = "water/waterDUDV";
	private static final String NORMAL_MAP = "water/normalMap";
	private static final float WAVE_SPEED = 0.05f;
	
	private RawModel quad;
	private WaterShader shader;
	private Fbo waterReflection, waterRefraction;
	
	private float moveFactor = 0;
	
	private int dudvTexture;
	private int normalMap;

	private Timer timer;
	
	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix, Fbo waterReflection, Fbo waterRefraction) {
		this.shader = shader;
		this.waterReflection = waterReflection;
		this.waterRefraction = waterRefraction;
		dudvTexture = loader.loadTexture(DUDV_MAP);
		normalMap = loader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
		timer = new Timer();
	}

	public void updateProjectionMatrix(Matrix4f projectionMatrix) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(List<WaterTile> water, Camera camera, Light sun, float near, float far) {
		prepareRender(camera, sun, near, far);	
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), new Vector3f(0, 0, 0),
					WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera, Light sun, float near, float far){
		
		double passed = timer.getDelta();
		
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadPlaneValues(near, far);
		moveFactor += WAVE_SPEED * (float) passed;
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(sun);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterReflection.getColourTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterRefraction.getColourTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dudvTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalMap);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, waterRefraction.getDepthTexture());
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind(){
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		// Just x and z vertex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
