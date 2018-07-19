package com.draglantix.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.draglantix.filters.Contrast;
import com.draglantix.filters.Bright;
import com.draglantix.filters.Combine;
import com.draglantix.filters.BlurHorizontal;
import com.draglantix.filters.Blur;
import com.draglantix.models.RawModel;
import com.draglantix.tools.Loader;
import com.draglantix.tools.Window;

public class PostProcessing {
	
	private final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private RawModel quad;
	private Contrast contrastChanger;
	private BlurHorizontal hBlur;
	private Blur vBlur;
	private Bright brightFilter;
	private Combine combineFilter;
	
	private boolean usesAlpha = false;

	public PostProcessing(Loader loader, boolean usesAlpha){
		quad = loader.loadToVAO(POSITIONS, 2);
		this.usesAlpha = usesAlpha;
		contrastChanger = new Contrast();
		hBlur = new BlurHorizontal(Window.getWidth(), Window.getHeight(), usesAlpha);
		vBlur = new Blur(Window.getWidth(), Window.getHeight(), usesAlpha);
		brightFilter = new Bright(Window.getWidth(), Window.getHeight(), usesAlpha);
		combineFilter = new Combine();
	}
	
	public void doPostProcessing(int colourTexture, int brightTexture){
		start();
		//brightFilter.render(colourTexture);
		hBlur.render(brightTexture);
		vBlur.render(hBlur.getOutputTexture());
		combineFilter.render(colourTexture, vBlur.getOutputTexture());
		end();
	}
	
	public void updateDimensions(int width, int height) {
		hBlur.updateDimensions(width, height);
		vBlur.updateDimensions(width, height);
		brightFilter.updateDimensions(width, height);
	}
	
	public void cleanUp(){
		contrastChanger.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		brightFilter.cleanUp();
		combineFilter.cleanUp();
	}
	
	private void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
