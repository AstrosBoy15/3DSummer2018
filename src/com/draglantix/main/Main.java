package com.draglantix.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.draglantix.entities.Camera;
import com.draglantix.entities.Entity;
import com.draglantix.entities.Light;
import com.draglantix.entities.Player;
import com.draglantix.guis.GuiRenderer;
import com.draglantix.guis.GuiTexture;
import com.draglantix.models.RawModel;
import com.draglantix.models.TexturedModel;
import com.draglantix.objConverter.ModelData;
import com.draglantix.objConverter.OBJFileLoader;
import com.draglantix.render.Loader;
import com.draglantix.render.MasterRenderer;
import com.draglantix.render.Window;
import com.draglantix.terrains.Terrain;
import com.draglantix.textures.ModelTexture;
import com.draglantix.textures.TerrainTexture;
import com.draglantix.textures.TerrainTexturePack;
import com.draglantix.tools.Timer;

public class Main {
	
	private boolean pause = false;
	private Random rand = new Random();
	
	private Terrain currentTerrain;
	private Terrain terrains[][] = new Terrain[2][2];
	
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
		MasterRenderer renderer = new MasterRenderer();
	
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0, 10000, 1000), new Vector3f(10, 0, 0)));
		lights.add(new Light(new Vector3f(0, 1000, -1000), new Vector3f(0, 0, 10)));
		lights.add(new Light(new Vector3f(1000, 100, 0), new Vector3f(0, 10, 0)));
		lights.add(new Light(new Vector3f(-7000, 10000, 0), new Vector3f(1, 1, 1)));
		
		
		TerrainTexture backgroundTextureGrass = new TerrainTexture(loader.loadTexture("terrain/terrainTexture"));
		TerrainTexture backgroundTextureSnow = new TerrainTexture(loader.loadTexture("terrain/snowTerrainTexture"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("terrain/mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("terrain/pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("terrain/path"));
		
		TerrainTexturePack texturePackGrass = new TerrainTexturePack(backgroundTextureGrass, rTexture, gTexture, bTexture);
		TerrainTexturePack texturePackSnow = new TerrainTexturePack(backgroundTextureSnow, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("terrain/blendMap"));
		
		//Models///
		
		ModelData treeData = OBJFileLoader.loadOBJ("model/obj/tree");
		ModelData mushroomTestData = OBJFileLoader.loadOBJ("model/obj/mushroomTest");
		ModelData rock1Data = OBJFileLoader.loadOBJ("model/obj/rock1");
		ModelData rock2Data = OBJFileLoader.loadOBJ("model/obj/rock2");
		ModelData rock3Data = OBJFileLoader.loadOBJ("model/obj/rock3");
		ModelData grassData = OBJFileLoader.loadOBJ("model/obj/grass");	
		ModelData pineTreeData = OBJFileLoader.loadOBJ("model/obj/pineTree");	
		ModelData fallTreeData = OBJFileLoader.loadOBJ("model/obj/fallTree");	
		ModelData snowTreeData = OBJFileLoader.loadOBJ("model/obj/snowTree");		
		ModelData snowRock1Data = OBJFileLoader.loadOBJ("model/obj/snowRock1");
		ModelData snowRock2Data = OBJFileLoader.loadOBJ("model/obj/snowRock2");
		ModelData snowRock3Data = OBJFileLoader.loadOBJ("model/obj/snowRock3");
		ModelData snowPineTreeData = OBJFileLoader.loadOBJ("model/obj/snowPineTree");
		ModelData snowmanData = OBJFileLoader.loadOBJ("model/obj/snowman");
		
		RawModel treeModel = loader.loadToVAO(
			treeData.getVertices(), treeData.getTextureCoords(),
			treeData.getNormals(), treeData.getIndices());
		RawModel mushroomTestModel = loader.loadToVAO(
				mushroomTestData.getVertices(), mushroomTestData.getTextureCoords(),
				mushroomTestData.getNormals(), mushroomTestData.getIndices());
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
		RawModel snowmanModel = loader.loadToVAO(
				snowmanData.getVertices(), snowmanData.getTextureCoords(),
				snowmanData.getNormals(), snowmanData.getIndices());
		
		ModelTexture mushroomModelTex = new ModelTexture(loader.loadTexture("model/texture/mushroomTextureAtlas"));
		mushroomModelTex.setNumberOfRows(2);
		
		TexturedModel tree = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("model/texture/treeTexture")));
		TexturedModel mushroomTest = new TexturedModel(mushroomTestModel, mushroomModelTex);
		TexturedModel rock1 = new TexturedModel(rock1Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		TexturedModel rock2 = new TexturedModel(rock2Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		TexturedModel rock3 = new TexturedModel(rock3Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		TexturedModel grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("model/texture/tallGrassTexture")));
		TexturedModel pineTree = new TexturedModel(pineTreeModel, new ModelTexture(loader.loadTexture("model/texture/pineTreeTexture")));	
		TexturedModel fallTree = new TexturedModel(fallTreeModel, new ModelTexture(loader.loadTexture("model/texture/fallTreeTexture")));	
		TexturedModel snowTree = new TexturedModel(snowTreeModel, new ModelTexture(loader.loadTexture("model/texture/snowTreeTexture")));	
		TexturedModel snowRock1 = new TexturedModel(snowRock1Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		TexturedModel snowRock2 = new TexturedModel(snowRock2Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		TexturedModel snowRock3 = new TexturedModel(snowRock3Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		TexturedModel snowPineTree = new TexturedModel(snowPineTreeModel, new ModelTexture(loader.loadTexture("model/texture/snowPineTreeTexture")));
		TexturedModel snowman = new TexturedModel(snowmanModel, new ModelTexture(loader.loadTexture("model/texture/snowmanTexture")));		
		
		List<Entity> entities = new ArrayList<Entity>();
		
		for(int i = 0; i < terrains.length; i++) {
			for(int j = 0; j < terrains[i].length; j++) {
				if(j<terrains[i].length/2) {
					terrains[j][i] = new Terrain(-j, -i, loader, texturePackSnow, blendMap, "terrain/heightmap2");
					for(int w=0; w < 50; w++) {
						entities.add(new Entity(snowTree, generateEntityPos(terrains[j][i]), 0, 0, 0, 5));
						entities.add(new Entity(snowRock1, generateEntityPos(terrains[j][i]), 0, 0, 0, 1));
						entities.add(new Entity(snowRock2, generateEntityPos(terrains[j][i]), 0, 0, 0, 2));
						entities.add(new Entity(snowRock3, generateEntityPos(terrains[j][i]), 0, 0, 0, 3));
						entities.add(new Entity(snowPineTree, generateEntityPos(terrains[j][i]), 0, 0, 0, 5));
						entities.add(new Entity(snowman, generateEntityPos(terrains[j][i]), 0, 0, 0, 3));
					}
				}else {
					terrains[j][i] = new Terrain(-j, -i, loader, texturePackGrass, blendMap, "terrain/heightmap2");
					for(int w=0; w < 50; w++) {
						
						entities.add(new Entity(tree, generateEntityPos(terrains[j][i]), 0, 0, 0, 5));
						entities.add(new Entity(mushroomTest, rand.nextInt(4), generateEntityPos(terrains[j][i]), 0, 0, 0, .5f));
						entities.add(new Entity(rock1, generateEntityPos(terrains[j][i]), 0, 0, 0, 1));
						entities.add(new Entity(rock2, generateEntityPos(terrains[j][i]), 0, 0, 0, 2));
						entities.add(new Entity(rock3, generateEntityPos(terrains[j][i]), 0, 0, 0, 3));
						entities.add(new Entity(grass, generateEntityPos(terrains[j][i]), 0, 0, 0, 5));
						entities.add(new Entity(pineTree, generateEntityPos(terrains[j][i]), 0, 0, 0, 5));
						entities.add(new Entity(fallTree, generateEntityPos(terrains[j][i]), 0, 0, 0, 5));
					}
				}
			}
		}
		
		Player player = new Player(snowman, new Vector3f(0, 0, 0), 0, 180, 0, 3);
		Camera camera = new Camera(player);
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture dragon = new GuiTexture(loader.loadTexture("dragon"), new Vector2f(0.4f, 0.4f), 0.25f);
		guis.add(dragon);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		///////////Game Loop///////////////////
		
		while(!window.shouldClose()) {
			boolean can_render = false;
			
			double time_2 = Timer.getTime();
			double passed = time_2 - time;
			unprocessed += passed;
			frame_time += passed;
			
			time = time_2;
			while(unprocessed >= frame_cap) {
				if(Window.hasResized()){
					GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
					renderer.updateProjectionMatrix();
				}
				
				if(Window.getInput().isKeyPressed(GLFW.GLFW_KEY_P)) {
					pause = !pause;
				}
				
				if(Window.getInput().isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
					GLFW.glfwSetWindowShouldClose(Window.getWindow(), true);
				}
				unprocessed-=frame_cap;
				can_render = true;
				
				window.update();
				
				int currentX = (int) (1-player.getPosition().x/Terrain.SIZE);
				int currentZ = (int) (1-player.getPosition().z/Terrain.SIZE);
				
				if(currentZ < terrains.length) {
					if(currentX < terrains[currentZ].length) {
						currentTerrain = terrains[currentX][currentZ];
					}
				}
			
				if(frame_time>=1.0) {
					frame_time = 0;
				}
				
			}
			
			if(can_render) {/////////////RENDER HERE///////////
				
				if(!pause) {
					player.move(currentTerrain);
					camera.move();
				}
				renderer.processEntity(player);
				for(int i = 0; i < terrains.length; i++) {
					for(int j = 0; j < terrains[i].length; j++) {
						renderer.processTerrain(terrains[j][i]);
					}
				}
				for(Entity e : entities) {
					renderer.processEntity(e);
				}
				renderer.renderer(lights, camera);
		
				
				guiRenderer.render(guis);
				window.swapBuffers();
			}
		}
		
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		GLFW.glfwTerminate();
		
		////////////////////////////////////////////////////////
	}
	
	private Vector3f generateEntityPos(Terrain terrain){
		float x = (rand.nextFloat() * terrain.SIZE) + terrain.getX();
		float z = (rand.nextFloat() * terrain.SIZE) + terrain.getZ();
		float y = terrain.getHeightOfTerrain(x, z);
		return new Vector3f(x, y, z);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}