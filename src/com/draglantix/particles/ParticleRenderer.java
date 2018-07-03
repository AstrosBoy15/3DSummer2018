package com.draglantix.particles;

import java.nio.FloatBuffer;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import com.draglantix.entities.Camera;
import com.draglantix.models.RawModel;
import com.draglantix.render.Loader;
import com.draglantix.tools.Maths;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	public static final int MAX_INSTANCES = 1000;
	private static final int INSTANCE_DATA_LENGTH = 46;
	
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
	
	private RawModel quad;
	private static ParticleShader shader;
	
	private Loader loader;
	private int vbo;
	private int pointer = 0;
	
	private float theta, angle;
	private Matrix4f transformationMatrix, viewMatrix;		
	private Vector3f position = new Vector3f();
	
	protected ParticleRenderer(Loader loader, ParticleShader shader, Matrix4f projectionMatrix){
		this.loader = loader;
		this.vbo = loader.createEmptyVbo(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		quad = loader.loadToVAO(VERTICES, 2);
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0);
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4);
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8);
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12);
		
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 5, 4, INSTANCE_DATA_LENGTH, 16);
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 6, 4, INSTANCE_DATA_LENGTH, 20);
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 7, 4, INSTANCE_DATA_LENGTH, 24);
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 8, 4, INSTANCE_DATA_LENGTH, 28);
		
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 9, 4, INSTANCE_DATA_LENGTH, 32);
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 10, 1, INSTANCE_DATA_LENGTH, 36);
		
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 11, 2, INSTANCE_DATA_LENGTH, 37);
		
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 12, 3, INSTANCE_DATA_LENGTH, 39);
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 13, 3, INSTANCE_DATA_LENGTH, 42);
		loader.addInstancedAttribute(quad.getVaoID(), vbo, 14, 1, INSTANCE_DATA_LENGTH, 45);
		
		ParticleRenderer.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public static void updateProjectionMatrix(Matrix4f projectionMatrix) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(List<Particle> particles, ParticleTexture texture, Camera camera) {
		prepare();
		bindTexture(texture);
		pointer = 0;
		float[] vboData = new float[particles.size() * INSTANCE_DATA_LENGTH];
		for(Particle p : particles) {
			update(p, camera, vboData);
			updateTexCoordInfo(p, vboData);
			updateColor(p, vboData);
		}
		loader.updateVbo(vbo, vboData, buffer);
		GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVaoID(), particles.size());
		finishRendering();
		
	}
	
	private void updateTexCoordInfo(Particle particle, float[] data) {
		data[pointer++] = particle.getTexOffset1().x;
		data[pointer++] = particle.getTexOffset1().y;
		data[pointer++] = particle.getTexOffset2().x;
		data[pointer++] = particle.getTexOffset2().y;
		data[pointer++] = particle.getBlend();
		data[pointer++] = particle.getAtlasOffset().x;
		data[pointer++] = particle.getAtlasOffset().y;
	}
	
	private void updateColor(Particle p, float[] data) {
		data[pointer++] = p.getCurrentColor().x;
		data[pointer++] = p.getCurrentColor().y;
		data[pointer++] = p.getCurrentColor().z;
		data[pointer++] = p.getNextColor().x;
		data[pointer++] = p.getNextColor().y;
		data[pointer++] = p.getNextColor().z;
		data[pointer++] = p.getColorBlend();
	}
	
	private void bindTexture(ParticleTexture texture) {
		if(texture.isAdditiveBlend()) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		}else {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		shader.loadNumberOfRows(texture.getNumberOfRows());
	}
	
	private void prepare(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glEnableVertexAttribArray(5);
		GL20.glEnableVertexAttribArray(6);
		GL20.glEnableVertexAttribArray(7);
		GL20.glEnableVertexAttribArray(8);
		GL20.glEnableVertexAttribArray(9);
		GL20.glEnableVertexAttribArray(10);
		GL20.glEnableVertexAttribArray(11);
		GL20.glEnableVertexAttribArray(12);
		GL20.glEnableVertexAttribArray(13);
		GL20.glEnableVertexAttribArray(14);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
	}
	
	private void finishRendering(){
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL20.glDisableVertexAttribArray(5);
		GL20.glDisableVertexAttribArray(6);
		GL20.glDisableVertexAttribArray(7);
		GL20.glDisableVertexAttribArray(8);
		GL20.glDisableVertexAttribArray(9);
		GL20.glDisableVertexAttribArray(10);
		GL20.glDisableVertexAttribArray(11);
		GL20.glDisableVertexAttribArray(12);
		GL20.glDisableVertexAttribArray(13);
		GL20.glDisableVertexAttribArray(14);
		GL30.glBindVertexArray(0);
	}
	
	private void update(Particle p, Camera camera, float[] vboData){
		theta = camera.getTheta()-180;
		if(theta>360) {
			theta-=360;
		}else if(theta<-360) {
			theta+=360;
		}

		angle = -camera.getPitch()/60;
		
		p.setRotation(new Vector3f(0, theta, 0));
			
		position.x = p.getPosition().x;
		position.y = p.getPosition().y;
		position.z = p.getPosition().z;
		
		transformationMatrix = Maths.createTransformationMatrix(position, 
				p.getRotation().x, p.getRotation().y, p.getRotation().z, p.getScale());
		
		transformationMatrix.rotateX(angle);
		
		viewMatrix = Maths.createViewMatrix(camera);
	
		storeMatrixData(viewMatrix, vboData);
		storeMatrixData(transformationMatrix, vboData);
	}
	
	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		  vboData[pointer++] = matrix.m00();
		  vboData[pointer++] = matrix.m01();
		  vboData[pointer++] = matrix.m02();
		  vboData[pointer++] = matrix.m03();
		  vboData[pointer++] = matrix.m10();
		  vboData[pointer++] = matrix.m11();
		  vboData[pointer++] = matrix.m12();
		  vboData[pointer++] = matrix.m13();
		  vboData[pointer++] = matrix.m20();
		  vboData[pointer++] = matrix.m21();
		  vboData[pointer++] = matrix.m22();
		  vboData[pointer++] = matrix.m23();
		  vboData[pointer++] = matrix.m30();
		  vboData[pointer++] = matrix.m31();
		  vboData[pointer++] = matrix.m32();
		  vboData[pointer++] = matrix.m33();
	}
	
	

}
