package com.draglantix.render;

import org.lwjgl.opengl.GL11;

import com.draglantix.postProcessing.ProcessingFrameBuffers;

public class ImageRenderer {

	private ProcessingFrameBuffers fbo;

	public ImageRenderer(int width, int height) {
		this.fbo = new ProcessingFrameBuffers(width, height, ProcessingFrameBuffers.NONE);
	}

	public ImageRenderer() {}

	public void renderQuad() {
		if (fbo != null) {
			fbo.bindFrameBuffer();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
	}

	public int getOutputTexture() {
		return fbo.getColourTexture();
	}

	public void cleanUp() {
		if (fbo != null) {
			fbo.cleanUp();
		}
	}

}
