package com.draglantix.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class EntitySelector {

	Fbo selectionBuffers;
	
	public EntitySelector(Fbo selectionBuffers) {
		this.selectionBuffers = selectionBuffers;
	}
	
	public Vector3f getEntityAtMousePos() {
	
		int texture = selectionBuffers.getColourTexture();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		
		int format = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT);
		int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
		
		double scaleX = (double) (width)/Window.getWidth();
		double scaleY = (double) (height)/Window.getHeight();
		
		double mouseX = (Window.getMousePos().x * scaleX);
		double mouseY = -(Window.getMousePos().y * scaleY) + selectionBuffers.getHeight();
		
		if(mouseX > width) {
			mouseX = width;
		}
		if(mouseX < 1) {
			mouseX = 1;
		}
		
		if(mouseY > height) {
			mouseY = height;
		}
		if(mouseY < 1) {
			mouseY = 1;
		}
		
		mouseX = mouseX -1;
		mouseY = mouseY -1;
		
		int channels = 4;
		if (format == GL11.GL_RGB)
		    channels = 3;

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * channels);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, format, GL11.GL_UNSIGNED_BYTE, buffer);

		for (int x = 0; x < width; x++) {
		    for (int y = 0; y < height; y++) {
		        int i = (x + y * width) * channels;

		        int r = buffer.get(i) & 0xFF;
		        int g = buffer.get(i + 1) & 0xFF;
		        int b = buffer.get(i + 2) & 0xFF;
		        int a = 255;
		        if (channels == 4)
		            a = buffer.get(i + 3) & 0xFF;

		        image.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
		    }
		}
		
		buffer.flip();
		
		Color color = new Color(image.getRGB((int)mouseX, (int)mouseY));
		
		Vector3f ID = new Vector3f((color.getRed()), (color.getGreen()), (color.getBlue()));
		
		return ID;
		
	}

}
