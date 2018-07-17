package com.draglantix.tools;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class SelectionBuffers {

	 	public static final int RES_WIDTH = 260;
	    public static final int RES_HEIGHT = 200;
	    
	   
	    private int selectionBuffer;
	    private int selectionTexture;
	    private int selectionDepthBuffer;
	    
	    public SelectionBuffers() {//call when loading the game
	        initSelectionBuffer();
	    }
	 
	    public void cleanUp() {//call when closing the game
	        GL30.glDeleteFramebuffers(selectionBuffer);
	        GL11.glDeleteTextures(selectionTexture);
	        GL30.glDeleteFramebuffers(selectionDepthBuffer);
	    }
	 
	    public void bindSelectionBuffer() {//call before rendering to this FBO
	        bindFrameBuffer(selectionBuffer,RES_WIDTH,RES_HEIGHT);
	    }
	     
	    public void unbindCurrentFrameBuffer() {//call to switch to default frame buffer
	        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	        GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
	    }
	 
	    public int getSelectionTexture() {//get the resulting texture
	        return selectionTexture;
	    }
	     
	    private void initSelectionBuffer() {
	        selectionBuffer = createFrameBuffer();
	        selectionTexture = createTextureAttachment(RES_WIDTH,RES_HEIGHT);
	        selectionDepthBuffer = createDepthBufferAttachment(RES_WIDTH,RES_HEIGHT);
	        unbindCurrentFrameBuffer();
	    }
	     
	    private void bindFrameBuffer(int frameBuffer, int width, int height){
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
	        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
	        GL11.glViewport(0, 0, width, height);
	    }
	 
	    private int createFrameBuffer() {
	        int frameBuffer = GL30.glGenFramebuffers();
	        //generate name for frame buffer
	        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
	        //create the framebuffer
	        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
	        //indicate that we will always render to color attachment 0
	        return frameBuffer;
	    }
	 
	    private int createTextureAttachment( int width, int height) {
	        int texture = GL11.glGenTextures();
	        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
	        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,
	                0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
	        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
	                texture, 0);
	        return texture;
	    }
	    
	    private int createDepthBufferAttachment(int width, int height) {
	        int depthBuffer = GL30.glGenRenderbuffers();
	        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
	        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
	                height);
	        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
	                GL30.GL_RENDERBUFFER, depthBuffer);
	        return depthBuffer;
	    }
	
}
