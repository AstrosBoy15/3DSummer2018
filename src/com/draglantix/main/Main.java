package com.draglantix.main;

import java.util.List;
import java.util.Random;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.draglantix.entities.Camera;
import com.draglantix.entities.Entity;
import com.draglantix.entities.Player;
import com.draglantix.render.Loader;
import com.draglantix.render.Window;
import com.draglantix.terrains.Terrain;
import com.draglantix.tools.Timer;

public class Main {

	private float version = 0.12f;
	
	private boolean pause = false;
	private Random rand = new Random();
	
	private Window window;
	private Assets assets;
	private Loader loader;
	private Camera camera;
	
	private Timer timer;
	private float targetFPS = 60.0f;
	private float FPS;
	private float frameCount;
	private double timePassed;
	
	public static final float GRAVITY = -50;
	
	public Main() {
		init();
		spawnEntities();
		runGame();
	}
	
	public void runGame() {
		double frameCap = 1.0/targetFPS;
		timer = new Timer();
		double unprocessed = 0;
		
		while(!window.shouldClose()){
			double passed = timer.getDelta();
			unprocessed += passed;
			
			while(unprocessed >= frameCap) {
				unprocessed-=frameCap;
			
				tick();
				render();
				
				if(window.shouldClose()) {
					break;
				}
				
				calculateFPS();
	        }
		}
	    cleanUp(); 
	}
	
	private void init() {
		Window.setCallbacks();
		
		if (!GLFW.glfwInit()) {
			throw new IllegalStateException("Failed to initalize GLFW!");
		}
		
		window = new Window();
		window.createWindow("Engine v"+version);
		
		GL.createCapabilities();
		
		assets = new Assets();
		
		loader = assets.loader;
		camera = new Camera(assets.player);
			
		//World.init();
	}
	
	public void tick() {
		window.update();
		
		if(Window.hasResized()){
			GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
			assets.renderer.updateProjectionMatrix();
		}
		
		if(Window.getInput().isKeyPressed(GLFW.GLFW_KEY_P)) {
			pause = !pause;
		}
		
		if(Window.getInput().isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			GLFW.glfwSetWindowShouldClose(Window.getWindow(), true);
		}
		
		assets.particleSystem.generateParticles(assets.player.getPosition());
		assets.particleSystem2.generateParticles(new Vector3f(0, 40, 0));
		
		assets.particleMaster.update(camera);
		
		int currentX = (int) (1-assets.player.getPosition().x/Terrain.SIZE);
		int currentZ = (int) (1-assets.player.getPosition().z/Terrain.SIZE);
		if(currentZ < assets.terrains.length) {
			if(currentX < assets.terrains[currentZ].length) {
				assets.currentTerrain = assets.terrains[currentX][currentZ];
			}
		}
		
		if(!pause) {
			assets.player.move(assets.currentTerrain);
			camera.move();
		
			Vector3f ID = assets.entitySelector.getEntityAtMousePos();
			Entity selected = getEntityWithID(ID, assets.entities);
			
			if(selected!=null) {
				if(Window.getInput().isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
					assets.currentSelection = selected;
				}
			}else {
				if(Window.getInput().isMouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
					assets.currentSelection = null;
				}
			}
				
			if(assets.currentSelection!=null) {
				assets.currentSelection.increaseRotation(0, 1, 0);
			}
		}
	
	}
	
	public void render() {

		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		
		assets.waterBuffers.bindReflectionFrameBuffer();
		float distance = 2 * (camera.getPosition().y - assets.water.getHeight());
		camera.getPosition().y -= distance;
		camera.invertPitch();
		assets.renderer.renderScene(assets.player, assets.entities, assets.terrains, assets.lights, camera, new Vector4f(0, 1, 0, -assets.water.getHeight()+1f));
		camera.getPosition().y += distance;
		camera.invertPitch();
		
		assets.waterBuffers.bindRefractionFrameBuffer();
		assets.renderer.renderScene(assets.player, assets.entities, assets.terrains, assets.lights, camera, new Vector4f(0, -1, 0, assets.water.getHeight()+1f));
		
		assets.waterBuffers.unbindCurrentFrameBuffer();
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		assets.renderer.renderScene(assets.player, assets.entities, assets.terrains, assets.lights, camera, new Vector4f(0, -1, 0, 1000000));
		assets.renderer.renderWater(assets.waters, camera, assets.sun);
		
		assets.particleMaster.renderParticles(camera);
		
		assets.guiRenderer.render(assets.guis);
		assets.fontRenderer.render(assets.fonts);
		
		assets.selectionBuffers.bindSelectionBuffer();
		assets.renderer.renderEntities(assets.entities, assets.terrains, assets.player, camera);
		
		window.swapBuffers();
	}
	
	public void calculateFPS() {
		frameCount++;
		
		timePassed += timer.getDelta();
		
		if(timePassed>=1) {
			timePassed = 0;
			FPS = frameCount;
			frameCount = 0;
			System.out.println("FPS: " +FPS);
		}
	}
		
	private void spawnEntities() {
		for(int i = 0; i < assets.terrains.length; i++) {
			for(int j = 0; j < assets.terrains[i].length; j++) {
				if(j<assets.terrains[i].length/2) {
					assets.terrains[j][i] = new Terrain(-j, -i, loader, assets.texturePackSnow, assets.blendMap, "terrain/heightmap");
					for(int w=0; w < 1; w++) {
						assets.entities.add(new Entity(assets.snowTree, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 5));
						assets.entities.add(new Entity(assets.snowRock1, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 1));
						assets.entities.add(new Entity(assets.snowRock2, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 2));
						assets.entities.add(new Entity(assets.snowRock3, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 3));
						assets.entities.add(new Entity(assets.snowPineTree, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 5));
						assets.entities.add(new Entity(assets.snowman, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 3));
					}
				}else {
					assets.terrains[j][i] = new Terrain(-j, -i, loader, assets.texturePackGrass, assets.blendMap, "terrain/heightmap");
					for(int w=0; w < 1; w++) {
						
						assets.entities.add(new Entity(assets.tree, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 5));
						assets.entities.add(new Entity(assets.mushroomTest, rand.nextInt(4), generateEntityPos(assets.terrains[j][i]), 0, 0, 0, .5f));
						assets.entities.add(new Entity(assets.rock1, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 1));
						assets.entities.add(new Entity(assets.rock2, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 2));
						assets.entities.add(new Entity(assets.rock3, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 3));
						assets.entities.add(new Entity(assets.grass, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 5));
						assets.entities.add(new Entity(assets.pineTree, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 5));
						assets.entities.add(new Entity(assets.fallTree, generateEntityPos(assets.terrains[j][i]), 0, 0, 0, 5));
					}
				}
			}
		}
		createEntityIDSystem(assets.entities, assets.player);
	}
	
	private Vector3f generateEntityPos(Terrain terrain){
		float x = (rand.nextFloat() * Terrain.SIZE) + terrain.getX();
		float z = (rand.nextFloat() * Terrain.SIZE) + terrain.getZ();
		float y = terrain.getHeightOfTerrain(x, z);
		return new Vector3f(x, y, z);
	}
	
	public void createEntityIDSystem(List<Entity> entities, Player player) {
		float x=0, y=0, z=0;
		double delta = 1;
		for(Entity e : entities){
			if(x<255) {
				x+=delta;
			}else if(y<255){
				x=0;
				y+=delta;
			}else if(z<255){
				y=0;
				z+=delta;
			}
			e.setID(new Vector3f(x/255, y/255, z/255));
		}
		player.setID(new Vector3f(0, 0, 0));
	}
	
	public Entity getEntityWithID(Vector3f iD, List<Entity> entities) {
		for(Entity e : entities) {
			
			if(e.getID().x == iD.x/255 &&
			   e.getID().y == iD.y/255 &&
			   e.getID().z == iD.z/255) {
				return e;
			}
		}
		return null;
	}
	
	
	public void cleanUp() {
		loader.cleanUp();
		assets.renderer.cleanUp();
		assets.guiRenderer.cleanUp();
		assets.fontRenderer.cleanUp();
		assets.selectionBuffers.cleanUp();
		assets.waterBuffers.cleanUp();
		assets.particleMaster.cleanUp();
		GLFW.glfwTerminate();
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
}