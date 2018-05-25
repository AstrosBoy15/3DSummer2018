package com.draglantix.main;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.draglantix.entities.Camera;
import com.draglantix.entities.Entity;
import com.draglantix.entities.Light;
import com.draglantix.models.RawModel;
import com.draglantix.models.TexturedModel;
import com.draglantix.render.Loader;
import com.draglantix.render.MasterRenderer;
import com.draglantix.render.OBJLoader;
import com.draglantix.render.Window;
import com.draglantix.terrains.Terrain;
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
		
		double frame_cap = 1.0/60.0;
		double frame_time = 0;
		double time = Timer.getTime();
		double unprocessed = 0;
		
		Loader loader = new Loader();
		Camera camera = new Camera();
		MasterRenderer renderer = new MasterRenderer();
	
		Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));
		Terrain terrain = new Terrain(-1, -1,loader,new ModelTexture(loader.loadTexture("grass")));
		Terrain terrain2 = new Terrain(0, -1,loader,new ModelTexture(loader.loadTexture("grass")));

		
		RawModel model = OBJLoader.loadOBJModel("dragon", loader);		
		TexturedModel dragonmodel = new TexturedModel(model,  
				new ModelTexture(loader.loadTexture("dragonTexture")));
		ModelTexture texture = dragonmodel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		List<Entity> dragons = new ArrayList<Entity>();
		
		while(!window.shouldClose()) {
			boolean can_render = false;
			
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			
			time = time_2;
			while(unprocessed >= frame_cap) {
				if(window.hasResized()){
					GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
				}
				
				unprocessed-=frame_cap;
				can_render = true;
				
				window.update();
				if(frame_time>=1.0) {
					frame_time = 0;
				}
				
			}
			
			if(can_render) {/////////////RENDER HERE///////////
				
				camera.move();
				
				renderer.processTerrain(terrain);
				renderer.processTerrain(terrain2);
				
				renderer.renderer(light, camera);
				window.swapBuffers();
			}
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		GLFW.glfwTerminate();
		
		////////////////////////////////////////////////////////
	}
	
	public static void main(String[] args) {
		new Main();
	}
}