package com.draglantix.main;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.draglantix.entities.Entity;
import com.draglantix.entities.Light;
import com.draglantix.entities.Player;
import com.draglantix.font.FontTexture;
import com.draglantix.guis.GuiTexture;
import com.draglantix.models.RawModel;
import com.draglantix.models.TexturedModel;
import com.draglantix.objConverter.ModelData;
import com.draglantix.objConverter.OBJFileLoader;
import com.draglantix.render.Loader;
import com.draglantix.terrains.Terrain;
import com.draglantix.textures.ModelTexture;
import com.draglantix.textures.TerrainTexture;
import com.draglantix.textures.TerrainTexturePack;
import com.draglantix.textures.Texture;
import com.draglantix.tools.EntitySelector;
import com.draglantix.tools.SelectionBuffers;
import com.draglantix.water.WaterFrameBuffers;
import com.draglantix.water.WaterTile;

public class Assets {

	public Loader loader = new Loader();
	
	public WaterFrameBuffers waterBuffers;
	public SelectionBuffers selectionBuffers;
	public EntitySelector entitySelector;
	
	public Terrain terrains[][];
	public Terrain currentTerrain;
	
	public Entity currentSelection = null;
	
	public List<WaterTile> waters;
	public List<GuiTexture> guis;
	public List<FontTexture> fonts;
	public List<Entity> entities;
	public List<Light> lights;
	
	public WaterTile water;
	
	public TerrainTexture blendMap,backgroundTextureGrass,
	backgroundTextureSnow,rTexture,gTexture,bTexture;
	
	public TerrainTexturePack texturePackGrass,texturePackSnow;
	
	public ModelData treeData,mushroomTestData,rock1Data,rock2Data,rock3Data,
	grassData,pineTreeData,fallTreeData,snowTreeData,snowRock1Data,
	snowRock2Data,snowRock3Data,snowPineTreeData,snowmanData,lampData;
	
	public RawModel treeModel,mushroomTestModel,rock1Model,rock2Model,
	rock3Model,grassModel,pineTreeModel,fallTreeModel,snowTreeModel,
	snowRock1Model,snowRock2Model,snowRock3Model,snowPineTreeModel,
	snowmanModel,lampModel;
	
	public ModelTexture mushroomModelTex,lampModelTex;

	public TexturedModel tree,mushroomTest,rock1,rock2,rock3,grass,
	pineTree,fallTree,snowTree,snowRock1,snowRock2,snowRock3,
	snowPineTree,snowman, lamp;
	
	public Entity LampEntity;
	
	public Light sun;
	public Light light;
	
	public Player player;
	
	public Assets() {
		
		System.out.println("Loading...");
		
		loader = new Loader();
		
		waterBuffers = new WaterFrameBuffers();
		selectionBuffers = new SelectionBuffers();
		entitySelector = new EntitySelector(selectionBuffers);
		
		terrains = new Terrain[2][2];
		currentTerrain = terrains[0][0];
		
		waters = new ArrayList<WaterTile>();
		guis = new ArrayList<GuiTexture>();
		fonts = new ArrayList<FontTexture>();
		entities = new ArrayList<Entity>();
		lights = new ArrayList<Light>();
		
		fonts.add(new FontTexture(loader.loadTexture("font/glyphSheet"), "res/font/fnt.txt", "The quick brown fox jumps over the lazy dog", new Vector2f(0, 0), 0.5f, 7, 20, new Vector3f(255, 0, 0)));
		
		//guis.add(new GuiTexture(loader.loadTexture("dragon"), new Vector2f(.5f, .5f), 0.3f));
		
		water = new WaterTile(-400, 400, 0);
		waters.add(water);
		
		blendMap = new TerrainTexture(loader.loadTexture("terrain/blendMap"));
		backgroundTextureGrass = new TerrainTexture(loader.loadTexture("terrain/terrainTexture"));
		backgroundTextureSnow = new TerrainTexture(loader.loadTexture("terrain/snowTerrainTexture"));
		rTexture = new TerrainTexture(loader.loadTexture("terrain/mud"));
		gTexture = new TerrainTexture(loader.loadTexture("terrain/pinkFlowers"));
		bTexture = new TerrainTexture(loader.loadTexture("terrain/path"));
		
		texturePackGrass = new TerrainTexturePack(backgroundTextureGrass, rTexture, gTexture, bTexture);
		texturePackSnow = new TerrainTexturePack(backgroundTextureSnow, rTexture, gTexture, bTexture);
		
		treeData = OBJFileLoader.loadOBJ("model/obj/tree");
		mushroomTestData = OBJFileLoader.loadOBJ("model/obj/mushroomTest");
		rock1Data = OBJFileLoader.loadOBJ("model/obj/rock1");
		rock2Data = OBJFileLoader.loadOBJ("model/obj/rock2");
		rock3Data = OBJFileLoader.loadOBJ("model/obj/rock3");
		grassData = OBJFileLoader.loadOBJ("model/obj/grass");	
		pineTreeData = OBJFileLoader.loadOBJ("model/obj/pineTree");	
		fallTreeData = OBJFileLoader.loadOBJ("model/obj/fallTree");	
		snowTreeData = OBJFileLoader.loadOBJ("model/obj/snowTree");		
		snowRock1Data = OBJFileLoader.loadOBJ("model/obj/snowRock1");
		snowRock2Data = OBJFileLoader.loadOBJ("model/obj/snowRock2");
		snowRock3Data = OBJFileLoader.loadOBJ("model/obj/snowRock3");
		snowPineTreeData = OBJFileLoader.loadOBJ("model/obj/snowPineTree");
		snowmanData = OBJFileLoader.loadOBJ("model/obj/snowman");
		lampData = OBJFileLoader.loadOBJ("model/obj/lamp");
		
		treeModel = loader.loadToVAO(
			treeData.getVertices(), treeData.getTextureCoords(),
			treeData.getNormals(), treeData.getIndices());
		mushroomTestModel = loader.loadToVAO(
				mushroomTestData.getVertices(), mushroomTestData.getTextureCoords(),
				mushroomTestData.getNormals(), mushroomTestData.getIndices());
		rock1Model = loader.loadToVAO(
			rock1Data.getVertices(), rock1Data.getTextureCoords(),
			rock1Data.getNormals(), rock1Data.getIndices());
		rock2Model = loader.loadToVAO(
			rock2Data.getVertices(), rock2Data.getTextureCoords(),
			rock2Data.getNormals(), rock2Data.getIndices());
		rock3Model = loader.loadToVAO(
			rock3Data.getVertices(), rock3Data.getTextureCoords(),
			rock3Data.getNormals(), rock3Data.getIndices());
		grassModel = loader.loadToVAO(
			grassData.getVertices(), grassData.getTextureCoords(),
			grassData.getNormals(), grassData.getIndices());
		pineTreeModel = loader.loadToVAO(
				pineTreeData.getVertices(), pineTreeData.getTextureCoords(),
				pineTreeData.getNormals(), pineTreeData.getIndices());
		fallTreeModel = loader.loadToVAO(
				fallTreeData.getVertices(), fallTreeData.getTextureCoords(),
				fallTreeData.getNormals(), fallTreeData.getIndices());
		snowTreeModel = loader.loadToVAO(
				snowTreeData.getVertices(), snowTreeData.getTextureCoords(),
				snowTreeData.getNormals(), snowTreeData.getIndices());
		snowRock1Model = loader.loadToVAO(
				snowRock1Data.getVertices(), snowRock1Data.getTextureCoords(),
				snowRock1Data.getNormals(), snowRock1Data.getIndices());
		snowRock2Model = loader.loadToVAO(
				snowRock2Data.getVertices(), snowRock2Data.getTextureCoords(),
				snowRock2Data.getNormals(), snowRock2Data.getIndices());
		snowRock3Model = loader.loadToVAO(
				snowRock3Data.getVertices(), snowRock3Data.getTextureCoords(),
				snowRock3Data.getNormals(), snowRock3Data.getIndices());
		snowPineTreeModel = loader.loadToVAO(
				snowPineTreeData.getVertices(), snowPineTreeData.getTextureCoords(),
				snowPineTreeData.getNormals(), snowPineTreeData.getIndices());
		snowmanModel = loader.loadToVAO(
				snowmanData.getVertices(), snowmanData.getTextureCoords(),
				snowmanData.getNormals(), snowmanData.getIndices());
		lampModel = loader.loadToVAO(
				lampData.getVertices(), lampData.getTextureCoords(),
				lampData.getNormals(), lampData.getIndices());
		
		mushroomModelTex = new ModelTexture(loader.loadTexture("model/texture/mushroomTextureAtlas"));
		mushroomModelTex.setNumberOfRows(2);
		lampModelTex = new ModelTexture(loader.loadTexture("model/texture/lamp"));
		lampModelTex.setUseFakeLighting(true);
		
		tree = new TexturedModel(treeModel, new ModelTexture(loader.loadTexture("model/texture/treeTexture")));
		mushroomTest = new TexturedModel(mushroomTestModel, mushroomModelTex);
		rock1 = new TexturedModel(rock1Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		rock2 = new TexturedModel(rock2Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		rock3 = new TexturedModel(rock3Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("model/texture/tallGrassTexture")));
		pineTree = new TexturedModel(pineTreeModel, new ModelTexture(loader.loadTexture("model/texture/pineTreeTexture")));	
		fallTree = new TexturedModel(fallTreeModel, new ModelTexture(loader.loadTexture("model/texture/fallTreeTexture")));	
		snowTree = new TexturedModel(snowTreeModel, new ModelTexture(loader.loadTexture("model/texture/snowTreeTexture")));	
		snowRock1 = new TexturedModel(snowRock1Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		snowRock2 = new TexturedModel(snowRock2Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		snowRock3 = new TexturedModel(snowRock3Model, new ModelTexture(loader.loadTexture("model/texture/rockTexture")));
		snowPineTree = new TexturedModel(snowPineTreeModel, new ModelTexture(loader.loadTexture("model/texture/snowPineTreeTexture")));
		snowman = new TexturedModel(snowmanModel, new ModelTexture(loader.loadTexture("model/texture/snowmanTexture")));		
		lamp = new TexturedModel(lampModel, lampModelTex);
		
		LampEntity = new Entity(lamp, new Vector3f(185, -4.7f, -293), 0, 0, 0, 1);
		entities.add(new Entity(lamp, new Vector3f(370, 4.2f, -300), 0, 0, 0, 1));
		entities.add(new Entity(lamp, new Vector3f(293, -6.8f, -305), 0, 0, 0, 1));
		entities.add(LampEntity);
		
		sun = new Light(new Vector3f(0, 1000, -7000), new Vector3f(0.4f, 0.4f, 0.4f));
		lights.add(sun);
		
		light = new Light(new Vector3f(185, 10, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f));
		lights.add(new Light(new Vector3f(370, 17, -300), new Vector3f(0, 2, 2), new Vector3f(1, 0.1f, 0.002f)));
		lights.add(new Light(new Vector3f(293, 7, -305), new Vector3f(2, 2, 0), new Vector3f(1, 0.1f, 0.002f)));
		lights.add(light);
		
		player = new Player(snowman, new Vector3f(40, 40, 40), 0, 180, 0, 3);
	}
}
