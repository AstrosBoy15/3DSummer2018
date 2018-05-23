package com.draglantix.main;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.draglantix.entities.Camera;
import com.draglantix.entities.Entity;
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
		
		StaticShader shader = new StaticShader();
		
		Renderer renderer = new Renderer(shader);
	
		double frame_cap = 1.0/60.0;
		
		double frame_time = 0;
		int frames = 0;
		
		double time = Timer.getTime();
		double unprocessed = 0;
		

		float[] vertices = {			
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,0.5f,-0.5f,		
				
				-0.5f,0.5f,0.5f,	
				-0.5f,-0.5f,0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				0.5f,0.5f,-0.5f,	
				0.5f,-0.5f,-0.5f,	
				0.5f,-0.5f,0.5f,	
				0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,-0.5f,	
				-0.5f,-0.5f,-0.5f,	
				-0.5f,-0.5f,0.5f,	
				-0.5f,0.5f,0.5f,
				
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f
				
		};
		
		float[] textureCoords = {
				
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,			
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0,
				0,0,
				0,1,
				1,1,
				1,0

				
		};
		
		int[] indices = {
				0,1,3,	
				3,1,2,	
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,	
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22

		};
		
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		TexturedModel staticModel = new TexturedModel(model,  
				new ModelTexture(loader.loadTexture("Dragon")));
		
		Entity entity = new Entity(staticModel, new Vector3f(0, 0, -5), 0, 0, 0, 1);
		
		Camera camera = new Camera();
		
		while(!window.shouldClose()) {
			boolean can_render = false;
			
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			
			time = time_2;while(unprocessed >= frame_cap) {
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
				entity.increaseRotation(1, 1, 0);
				renderer.prepare();
				shader.start();
				shader.loadViewMatrix(camera);
				camera.move();
				renderer.render(entity, shader);
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