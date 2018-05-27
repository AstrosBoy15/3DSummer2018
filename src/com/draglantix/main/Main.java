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
import com.draglantix.objConverter.ModelData;
import com.draglantix.objConverter.OBJFileLoader;
import com.draglantix.render.Loader;
import com.draglantix.render.MasterRenderer;
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
		Terrain terrain = new Terrain(-1, -1,loader,new ModelTexture(loader.loadTexture("snowTerrainTexture")));
		Terrain terrain2 = new Terrain(0, -1,loader,new ModelTexture(loader.loadTexture("terrainTexture")));

		//Models///
		
		ModelData treeData = OBJFileLoader.loadOBJ("tree");
		ModelData rock1Data = OBJFileLoader.loadOBJ("rock1");
		ModelData rock2Data = OBJFileLoader.loadOBJ("rock2");
		ModelData rock3Data = OBJFileLoader.loadOBJ("rock3");
		ModelData grassData = OBJFileLoader.loadOBJ("grass");	
		ModelData pineTreeData = OBJFileLoader.loadOBJ("pineTree");	
		ModelData fallTreeData = OBJFileLoader.loadOBJ("fallTree");	
		ModelData snowTreeData = OBJFileLoader.loadOBJ("snowTree");		
		ModelData snowRock1Data = OBJFileLoader.loadOBJ("snowRock1");
		ModelData snowRock2Data = OBJFileLoader.loadOBJ("snowRock2");
		ModelData snowRock3Data = OBJFileLoader.loadOBJ("snowRock3");
		ModelData snowPineTreeData = OBJFileLoader.loadOBJ("snowPineTree");
		
		RawModel treeModel = loader.loadToVAO(
			treeData.getVertices(), treeData.getTextureCoords(),
			treeData.getNormals(), treeData.getIndices());
		RawModel rock1Model = loader.loadToVAO(
			rock1Data.getVertices(), rock1Data.getTextureCoords(),
			rock1Data.getNormals(), rock1Data.getIndices());
		RawModel rock2Model = loader.loadToVAO(
			rock2Data.getVertices(), rock2Data.getTextureCoords(),
			rock2Data.getNormals(), rock2Data.getIndices());
		RawModel rock3Model = loader.loadToVAO(
			rock3Data.getVertices(), rock3Data.getTextureCoords(),
			rock3Data.getNormals(), rock3Data.getIndices());
		RawModel grassModel = loader.loadToVAO(
			grassData.getVertices(), grassData.getTextureCoords(),
			grassData.getNormals(), grassData.getIndices());
		RawModel pineTreeModel = loader.loadToVAO(
				pineTreeData.getVertices(), pineTreeData.getTextureCoords(),
				pineTreeData.getNormals(), pineTreeData.getIndices());
		RawModel fallTreeModel = loader.loadToVAO(
				fallTreeData.getVertices(), fallTreeData.getTextureCoords(),
				fallTreeData.getNormals(), fallTreeData.getIndices());
		//Snow Biome
		RawModel snowTreeModel = loader.loadToVAO(
				snowTreeData.getVertices(), snowTreeData.getTextureCoords(),
				snowTreeData.getNormals(), snowTreeData.getIndices());
		RawModel snowRock1Model = loader.loadToVAO(
				snowRock1Data.getVertices(), snowRock1Data.getTextureCoords(),
				snowRock1Data.getNormals(), snowRock1Data.getIndices());
		RawModel snowRock2Model = loader.loadToVAO(
				snowRock2Data.getVertices(), snowRock2Data.getTextureCoords(),
				snowRock2Data.getNormals(), snowRock2Data.getIndices());
		RawModel snowRock3Model = loader.loadToVAO(
				snowRock3Data.getVertices(), snowRock3Data.getTextureCoords(),
				snowRock3Data.getNormals(), snowRock3Data.getIndices());
		RawModel snowPineTreeModel = loader.loadToVAO(
				snowPineTreeData.getVertices(), snowPineTreeData.getTextureCoords(),
				snowPineTreeData.getNormals(), snowPineTreeData.getIndices());
		
		TexturedModel tree = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("treeTexture")));
		TexturedModel rock1 = new TexturedModel(rock1Model, new ModelTexture(loader.loadTexture("rockTexture")));
		TexturedModel rock2 = new TexturedModel(rock2Model, new ModelTexture(loader.loadTexture("rockTexture")));
		TexturedModel rock3 = new TexturedModel(rock3Model, new ModelTexture(loader.loadTexture("rockTexture")));
		TexturedModel grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("tallGrassTexture")));
		TexturedModel pineTree = new TexturedModel(pineTreeModel, new ModelTexture(loader.loadTexture("pineTreeTexture")));	
		TexturedModel fallTree = new TexturedModel(fallTreeModel, new ModelTexture(loader.loadTexture("fallTreeTexture")));	
		TexturedModel snowTree = new TexturedModel(snowTreeModel, new ModelTexture(loader.loadTexture("snowTreeTexture")));	
		TexturedModel snowRock1 = new TexturedModel(snowRock1Model, new ModelTexture(loader.loadTexture("rockTexture")));
		TexturedModel snowRock2 = new TexturedModel(snowRock2Model, new ModelTexture(loader.loadTexture("rockTexture")));
		TexturedModel snowRock3 = new TexturedModel(snowRock3Model, new ModelTexture(loader.loadTexture("rockTexture")));
		TexturedModel snowPineTree = new TexturedModel(snowPineTreeModel, new ModelTexture(loader.loadTexture("snowPineTreeTexture")));	
		
		List<Entity> entities = new ArrayList<Entity>();
		
		Random rand = new Random();
		
		int range = 800;
		float x, z;
		
		for(int i=0; i < 50; i++) {
			x = rand.nextFloat() * range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(tree, new Vector3f(x, 0, z), 0, 0, 0, 5));
			x = rand.nextFloat() * range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(rock1, new Vector3f(x, 0, z), 0, 0, 0, 1));
			x = rand.nextFloat() * range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(rock2, new Vector3f(x, 0, z), 0, 0, 0, 2));
			x = rand.nextFloat() * range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(rock3, new Vector3f(x, 0, z), 0, 0, 0, 3));
			x = rand.nextFloat() * range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(grass, new Vector3f(x, 0, z), 0, 0, 0, 5));
			x = rand.nextFloat() * range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(pineTree, new Vector3f(x, 0, z), 0, 0, 0, 5));
			x = rand.nextFloat() * range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(fallTree, new Vector3f(x, 0, z), 0, 0, 0, 5));
			
			//snow biome
			x = rand.nextFloat() * -range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(snowTree, new Vector3f(x, 0, z), 0, 0, 0, 5));
			x = rand.nextFloat() * -range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(snowRock1, new Vector3f(x, 0, z), 0, 0, 0, 1));
			x = rand.nextFloat() * -range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(snowRock2, new Vector3f(x, 0, z), 0, 0, 0, 2));
			x = rand.nextFloat() * -range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(snowRock3, new Vector3f(x, 0, z), 0, 0, 0, 3));
			x = rand.nextFloat() * -range;
			z = rand.nextFloat() * -range;
			entities.add(new Entity(snowPineTree, new Vector3f(x, 0, z), 0, 0, 0, 5));
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
				
				for(Entity e : entities) {
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