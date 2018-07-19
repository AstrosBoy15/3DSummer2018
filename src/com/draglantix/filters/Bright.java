package com.draglantix.filters;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.draglantix.render.ImageRenderer;
import com.draglantix.shaders.BrightFilterShader;

public class Bright {

	private ImageRenderer renderer;
	private BrightFilterShader shader;
	
	private boolean usesAlpha = false;
	
	public Bright(int width, int height, boolean usesAlpha){
		shader = new BrightFilterShader();
		this.usesAlpha = usesAlpha;
		renderer = new ImageRenderer(width, height, usesAlpha);
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
	
	public void updateDimensions(int width, int height) {
		renderer = new ImageRenderer(width, height, usesAlpha);
	}
	
	public void cleanUp(){
		renderer.cleanUp();
		shader.cleanUp();
	}
	
}
