package com.draglantix.skybox;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.draglantix.entities.Camera;
import com.draglantix.models.RawModel;
import com.draglantix.render.Loader;
import com.draglantix.shaders.SkyboxShader;
import com.draglantix.tools.Timer;

public class SkyboxRenderer {

private static final float SIZE = 500f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = {"right", "left", "top", "bottom", "back", "front"};
	private static String[] NIGHT_TEXTURE_FILES = {"nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"};
	
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	
	private static float time;
	private static float time24;
	
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		time = (float) Timer.getTimeSec();
		time24 = (float) Timer.getTimeMin();
	}
	
	public void render(Camera camera, float r, float g, float b) {
		float time_2 = (float) Timer.getTimeSec();
		float passed = time_2 - time;
		
		time = time_2;
		
		shader.start();
		shader.loadViewMatrix(camera, passed);
		shader.loadFogColor(r, g, b);
		GL30.glBindVertexArray(cube.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		bindTextures();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void bindTextures(){
		float time_2 = (float) Timer.getTimeMin();
		float passed = time_2 - time24;
		
		if(passed>=24) {
			time24 = time_2;
		}
		
		//Clock
		//System.out.println("Time of Day: " + (int)passed + ":" + (int) (60 * (passed % 1)) );
		
		int texture1;
		int texture2;
		float blendFactor;		
		if(passed >= 0 && passed < 5){
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = (passed - 0)/(5 - 0);
		}else if(passed >= 5 && passed < 8){
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = (passed - 5)/(8 - 5);
		}else if(passed >= 8 && passed < 21){
			texture1 = texture;
			texture2 = texture;
			blendFactor = (passed - 8)/(21- 8);
		}else{
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = (passed - 21)/(24 - 21);
		}

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
	
	
}
