package com.draglantix.font;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.draglantix.models.RawModel;
import com.draglantix.render.Loader;
import com.draglantix.tools.Maths;

public class FontRenderer {
	
	private final RawModel quad;
	private FontShader shader;
	
	public FontRenderer(Loader loader) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(positions, 2);
		shader = new FontShader();
	}
	
	public void render(List<FontTexture> msgs) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(FontTexture msg: msgs) {
			shader.loadFontColor(msg.getColor());
			shader.loadNumberOfRows(msg.getRows());
			for(int i = 0; i < msg.getTextureIndex().length; i++) {
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, msg.getTexture());
				Vector2f finalPos = new Vector2f(msg.getPosition().x + i*.05f, msg.getPosition().y);
				Matrix4f matrix = Maths.createTransformationMatrix(finalPos, msg.getScale()/10);
				shader.loadOffset(msg.getTextureXOffset(i), msg.getTextureYOffset(i));
				shader.loadTransformation(matrix);
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
	
}
