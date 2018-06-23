package com.draglantix.particles;

import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.draglantix.entities.Camera;
import com.draglantix.models.RawModel;
import com.draglantix.render.Loader;
import com.draglantix.tools.Maths;

public class ParticleRenderer {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	
	private RawModel quad;
	private ParticleShader shader;
	
	protected ParticleRenderer(Loader loader, ParticleShader shader, Matrix4f projectionMatrix){
		quad = loader.loadToVAO(VERTICES, 2);
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void updateProjectionMatrix(Matrix4f projectionMatrix) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<ParticleTexture, List<Particle>> particles, Camera camera) {
		prepare();
		for(ParticleTexture texture : particles.keySet()) {
			if(texture.isAdditiveBlend()) {
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
			}else {
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
			for(Particle p : particles.get(texture)) {
				loadModelMatrix(p, camera);
				shader.loadTextureCoordInfo(p.getTexOffset1(), p.getTexOffset2(), texture.getNumberOfRows(), p.getBlend());
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		finishRendering();
		
	}
	
	private void prepare(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);
	}
	
	private void finishRendering(){
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Particle p, Camera camera){
		float theta = camera.getTheta()-180;
		if(theta>360) {
			theta-=360;
		}else if(theta<-360) {
			theta+=360;
		}

		float angle = -camera.getPitch()/60;
		
		Matrix4f transformationMatrix;		
		
		p.setRotation(new Vector3f(0, theta, 0));
			
		transformationMatrix = Maths.createTransformationMatrix(
				new Vector3f(p.getPosition().x, p.getPosition().y, p.getPosition().z), 
				p.getRotation().x, p.getRotation().y, p.getRotation().z, p.getScale());
		
		transformationMatrix.rotateX(angle);
		
		shader.loadModelMatrix(transformationMatrix);
		shader.loadViewMatrix(camera);
	}
	
	

}
