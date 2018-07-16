package com.draglantix.postProcessing;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.draglantix.bloom.BrightFilter;
import com.draglantix.bloom.CombineFilter;
import com.draglantix.gaussianBlur.HorizontalBlur;
import com.draglantix.gaussianBlur.VerticalBlur;
import com.draglantix.models.RawModel;
import com.draglantix.render.Loader;
import com.draglantix.render.Window;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;
	private static ContrastChanger contrastChanger;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;

	public static void init(Loader loader){
		quad = loader.loadToVAO(POSITIONS, 2);
		contrastChanger = new ContrastChanger();
		hBlur = new HorizontalBlur(Window.getWidth(), Window.getHeight());
		vBlur = new VerticalBlur(Window.getWidth(), Window.getHeight());
		brightFilter = new BrightFilter(Window.getWidth(), Window.getHeight());
		combineFilter = new CombineFilter();
	}
	
	public static void doPostProcessing(int colourTexture, int brightTexture){
		start();
		//brightFilter.render(colourTexture);
		hBlur.render(brightTexture);
		vBlur.render(hBlur.getOutputTexture());
		combineFilter.render(colourTexture, vBlur.getOutputTexture());
		end();
	}
	
	public static void updateDimensions(int width, int height) {
		hBlur.updateDimensions(width, height);
		vBlur.updateDimensions(width, height);
		brightFilter.updateDimensions(width, height);
	}
	
	public static void cleanUp(){
		contrastChanger.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		brightFilter.cleanUp();
		combineFilter.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
