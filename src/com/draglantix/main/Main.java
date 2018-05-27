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
		Terrain terrain = new Terrain(-1, -1,loader,new ModelTexture(loader.loadTexture("terrainTexture")));
		Terrain terrain2 = new Terrain(0, -1,loader,new ModelTexture(loader.loadTexture("terrainTexture")));

		
		///////Models Init///////// 
		
		RawModel tallGrass = OBJLoader.loadOBJModel("grass", loader);		
		TexturedModel tallGrassModel = new TexturedModel(tallGrass,  
				new ModelTexture(loader.loadTexture("tallGrassTexture")));
		ModelTexture tallGrassTexture = tallGrassModel.getTexture();
		//tallGrassTexture.setShineDamper(10);
		//tallGrassTexture.setReflectivity(1);
		
		RawModel fallTree = OBJLoader.loadOBJModel("fallTree", loader);		
		TexturedModel fallTreeModel = new TexturedModel(fallTree,  
				new ModelTexture(loader.loadTexture("fallTreeTexture")));
		
		RawModel tree = OBJLoader.loadOBJModel("tree", loader);		
		TexturedModel treeModel = new TexturedModel(tree,  
				new ModelTexture(loader.loadTexture("treeTexture")));
		
		RawModel pineTree = OBJLoader.loadOBJModel("pineTree", loader);		
		TexturedModel pineTreeModel = new TexturedModel(pineTree,  
				new ModelTexture(loader.loadTexture("pineTreeTexture")));
		
		RawModel snowTree = OBJLoader.loadOBJModel("snowTree", loader);		
		TexturedModel snowTreeModel = new TexturedModel(snowTree,  
				new ModelTexture(loader.loadTexture("snowTreeTexture")));
		
		RawModel rock1 = OBJLoader.loadOBJModel("rock1", loader);		
		TexturedModel rock1Model = new TexturedModel(rock1,  
				new ModelTexture(loader.loadTexture("rockTexture")));
		
		RawModel rock2 = OBJLoader.loadOBJModel("rock2", loader);		
		TexturedModel rock2Model = new TexturedModel(rock2,  
				new ModelTexture(loader.loadTexture("rockTexture")));
		
		RawModel rock3 = OBJLoader.loadOBJModel("rock3", loader);		
		TexturedModel rock3Model = new TexturedModel(rock3,  
				new ModelTexture(loader.loadTexture("rockTexture")));
		
		RawModel snowRock1 = OBJLoader.loadOBJModel("snowRock1", loader);		
		TexturedModel snowRock1Model = new TexturedModel(snowRock1,  
				new ModelTexture(loader.loadTexture("rockTexture")));
		
		RawModel snowRock2 = OBJLoader.loadOBJModel("snowRock2", loader);		
		TexturedModel snowRock2Model = new TexturedModel(snowRock2,  
				new ModelTexture(loader.loadTexture("rockTexture")));
		
		RawModel snowRock3 = OBJLoader.loadOBJModel("snowRock3", loader);		
		TexturedModel snowRock3Model = new TexturedModel(snowRock3,  
				new ModelTexture(loader.loadTexture("rockTexture")));
		
		RawModel snowPineTree = OBJLoader.loadOBJModel("snowPineTree", loader);		
		TexturedModel snowPineTreeModel = new TexturedModel(snowPineTree,  
				new ModelTexture(loader.loadTexture("snowPineTreeTexture")));	
		
		List<Entity> grass = new ArrayList<Entity>();
		List<Entity> fallTrees = new ArrayList<Entity>();
		List<Entity> trees = new ArrayList<Entity>();
		List<Entity> pineTrees = new ArrayList<Entity>();
		List<Entity> snowTrees = new ArrayList<Entity>();
		List<Entity> rocks1 = new ArrayList<Entity>();
		List<Entity> rocks2 = new ArrayList<Entity>();
		List<Entity> rocks3 = new ArrayList<Entity>();
		List<Entity> snowRocks1 = new ArrayList<Entity>();
		List<Entity> snowRocks2 = new ArrayList<Entity>();
		List<Entity> snowRocks3 = new ArrayList<Entity>();
		List<Entity> snowPineTrees = new ArrayList<Entity>();
		
		Random rand = new Random();
		
		int range = 800;
		
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			grass.add(new Entity(tallGrassModel, new Vector3f(x, 0, z), 0, 0, 0, 3));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			fallTrees.add(new Entity(fallTreeModel, new Vector3f(x, 0, z), 0, 0, 0, 5));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			trees.add(new Entity(treeModel, new Vector3f(x, 0, z), 0, 0, 0, 5));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			pineTrees.add(new Entity(pineTreeModel, new Vector3f(x, 0, z), 0, 0, 0, 5));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			snowTrees.add(new Entity(snowTreeModel, new Vector3f(x, 0, z), 0, 0, 0, 5));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			rocks1.add(new Entity(rock1Model, new Vector3f(x, 0, z), 0, 0, 0, 2));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			rocks2.add(new Entity(rock2Model, new Vector3f(x, 0, z), 0, 0, 0, 1));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			rocks3.add(new Entity(rock3Model, new Vector3f(x, 0, z), 0, 0, 0, 3));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			snowRocks1.add(new Entity(snowRock1Model, new Vector3f(x, 0, z), 0, 0, 0, 2));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			snowRocks2.add(new Entity(snowRock2Model, new Vector3f(x, 0, z), 0, 0, 0, 1));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			snowRocks3.add(new Entity(snowRock3Model, new Vector3f(x, 0, z), 0, 0, 0, 3));
		}
		for(int i=0; i < 50; i++) {
			float x = rand.nextFloat() * range;
			float z = rand.nextFloat() * -range;
			snowPineTrees.add(new Entity(snowPineTreeModel, new Vector3f(x, 0, z), 0, 0, 0, 5));
		}
		
		///////////Game Loop///////////////////
		
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
				
				for(Entity e : grass) {
					renderer.processEntity(e);
				}
				for(Entity e : fallTrees) {
					renderer.processEntity(e);
				}
				for(Entity e : trees) {
					renderer.processEntity(e);
				}
				for(Entity e : pineTrees) {
					renderer.processEntity(e);
				}
				for(Entity e : snowTrees) {
					renderer.processEntity(e);
				}
				for(Entity e : rocks1) {
					renderer.processEntity(e);
				}
				for(Entity e : rocks2) {
					renderer.processEntity(e);
				}
				for(Entity e : rocks3) {
					renderer.processEntity(e);
				}
				for(Entity e : snowRocks1) {
					renderer.processEntity(e);
				}
				for(Entity e : snowRocks2) {
					renderer.processEntity(e);
				}
				for(Entity e : snowRocks3) {
					renderer.processEntity(e);
				}
				for(Entity e : snowPineTrees) {
					renderer.processEntity(e);
				}	
				
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