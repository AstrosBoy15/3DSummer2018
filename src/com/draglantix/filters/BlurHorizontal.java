package com.draglantix.filters;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.draglantix.render.ImageRenderer;
import com.draglantix.shaders.HorizontalBlurShader;

public class BlurHorizontal {
	
	private ImageRenderer renderer;
	private HorizontalBlurShader shader;
	
	private boolean usesAlpha = false;
	
	public BlurHorizontal(int targetFboWidth, int targetFboHeight, boolean usesAlpha){
		shader = new HorizontalBlurShader();
		this.usesAlpha = usesAlpha;
		shader.start();
		shader.loadTargetWidth(targetFboWidth);
		shader.stop();
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight, usesAlpha);
	}
	
	public void render(int texture){
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public int getOutputTexture(){
		return renderer.getOutputTexture();
	}
	
	public void updateDimensions(int targetFboWidth, int targetFboHeight) {
		shader.start();
		shader.loadTargetWidth(targetFboWidth);
		shader.stop();
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight, usesAlpha);
	}
	
	public void cleanUp(){
		renderer.cleanUp();
		shader.cleanUp();
	}

}
