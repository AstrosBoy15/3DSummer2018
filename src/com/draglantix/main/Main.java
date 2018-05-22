package com.draglantix.main;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.draglantix.models.RawModel;
import com.draglantix.models.TexturedModel;
import com.draglantix.render.Loader;
import com.draglantix.render.Renderer;
import com.draglantix.render.Window;
import com.draglantix.shaders.StaticShader;
import com.draglantix.textures.ModelTexture;
import com.draglantix.tools.Timer;

public class Main {
	
	public Main() {
		Window.setCallbacks();
	
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Failed to initalize GLFW!");
		}
		
		Window window = new Window();
		window.createWindow("DRAGLANTIX SUMMER 2018!");
		
		GL.createCapabilities();
		
		Loader loader = new Loader();
		Renderer renderer = new Renderer();
		
		StaticShader shader = new StaticShader();
	
		double frame_cap = 1.0/60.0;
		
		double frame_time = 0;
		int frames = 0;
		
		double time = Timer.getTime();
		double unprocessed = 0;
		
		while(!window.shouldClose()) {
			boolean can_render = false;
			
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			
			time = time_2;
			
			float[] vertices = {
					-.5f, .5f, 0f,
					-.5f, -.5f, 0f,
					.5f, -.5f, 0f,
					.5f, .5f, 0f
			};
			
			int[] indices = {
				0, 1, 3,
				3, 1, 2
			};
			
			RawModel model = loader.loadToVAO(vertices, indices);
			ModelTexture texture = new ModelTexture(loader.loadTexture("Dragon"));
			TexturedModel texturedModel = new TexturedModel(model, texture);
			
			while(unprocessed >= frame_cap) {
				if(window.hasResized()){
					GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
				}
				
				unprocessed-=frame_cap;
				can_render = true;
				
				window.update();
				if(frame_time>=1.0) {
					frame_time = 0;
					frames = 0;
				}
				
			}
			
			if(can_render) {
				renderer.prepare();
				shader.start();
				renderer.render(texturedModel);
				shader.stop();
				window.swapBuffers();
				frames++;
			}
		}
		
		shader.cleanUp();
		loader.cleanUp();
		GLFW.glfwTerminate();
	}
	
	public static void main(String[] args) {
		new Main();
	}
}