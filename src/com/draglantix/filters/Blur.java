package com.draglantix.filters;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.draglantix.render.ImageRenderer;
import com.draglantix.shaders.VerticalBlurShader;

public class Blur {
	
	private ImageRenderer renderer;
	private VerticalBlurShader shader;
	
	private boolean usesAlpha = false;
	
	public Blur(int targetFboWidth, int targetFboHeight, boolean usesAlpha){
		shader = new VerticalBlurShader();
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight, usesAlpha);
		this.usesAlpha = usesAlpha;
		shader.start();
		shader.loadTargetHeight(targetFboHeight);
		shader.stop();
	}

	
	public void render(int texture){
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void updateDimensions(int targetFboWidth, int targetFboHeight) {
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight, usesAlpha);
		shader.start();
		shader.loadTargetHeight(targetFboHeight);
		shader.stop();
	}
	
	public int getOutputTexture(){
		return renderer.getOutputTexture();
	}
	
	public void cleanUp(){
		renderer.cleanUp();
		shader.cleanUp();
	}
}
