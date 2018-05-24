package com.draglantix.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		
		double frame_cap = 1.0/60.0;
		
		double frame_time = 0;
		int frames = 0;
		
		double time = Timer.getTime();
		double unprocessed = 0;
		
		RawModel model = OBJLoader.loadOBJModel("dragon", loader);
		
		TexturedModel dragonmodel = new TexturedModel(model,  
				new ModelTexture(loader.loadTexture("dragonTexture")));
		ModelTexture texture = dragonmodel.getTexture();
		texture.setShineDamper(10);
		texture.setReflectivity(1);
		
		Light light = new Light(new Vector3f(0, 0, -40), new Vector3f(1, 1, 1));
		
		List<Entity> dragons = new ArrayList<Entity>();
		Random random = new Random();
		
		for(int i = 0; i < 20; i++) {
			float x = random.nextFloat() * 100 - 50;
			float y = random.nextFloat() * 100 - 50;
			float z = random.nextFloat() * -300;
			dragons.add(new Entity(dragonmodel, new Vector3f(x, y , z), random.nextFloat() * 180f,
					random.nextFloat() * 180f, 0f, 1f));
		}
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		
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
					frames = 0;
				}
				
			}
			
			if(can_render) {
				
				for(Entity dragon : dragons) {
					dragon.increaseRotation(0, 1.5f, 0);
					renderer.processEntity(dragon);
				}
				camera.move();
				renderer.renderer(light, camera);
				window.swapBuffers();
				frames++;
			}
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		GLFW.glfwTerminate();
	}
	
	public static void main(String[] args) {
		new Main();
	}
}