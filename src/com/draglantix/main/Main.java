package com.draglantix.main;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.draglantix.entities.Camera;
import com.draglantix.entities.Entity;
import com.draglantix.render.Loader;
import com.draglantix.render.Window;
import com.draglantix.tools.Timer;

public class Main {

	private float version = 0.14f;
	
	private boolean pause = false;
	
	private Window window;
	private Assets assets;
	private Loader loader;
	private Camera camera;
	
	private World world;
	
	private Timer timer;
	private float targetFPS = 60.0f;
	private float FPS;
	private float frameCount;
	private double timePassed;
	
	public static final float GRAVITY = -50;
	
	public Main() {
		init();
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
		
		world = assets.world;
		
		camera = assets.camera;
		
		GL11.glEnable(GL13.GL_MULTISAMPLE);
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
		
		//assets.particleSystem.generateParticles(assets.player.getPosition());
		//assets.particleSystem2.generateParticles(new Vector3f(400, 40, 300));
		
		assets.particleMaster.update(camera);
		
		if(!pause) {
			assets.player.move(world.terrain);
			camera.move();
		
			Vector3f ID = assets.entitySelector.getEntityAtMousePos();
			Entity selected = world.getEntityWithID(ID, assets.entities);
			
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

		assets.renderer.renderShadowMap(assets.entities, world.terrains, assets.player, assets.sun);
		
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		
		assets.waterBuffers.bindReflectionFrameBuffer();
		float distance = 2 * (camera.getPosition().y - assets.water.getHeight());
		camera.getPosition().y -= distance;
		camera.invertPitch();
		assets.renderer.renderScene(assets.player, assets.entities, world.terrain, assets.lights, camera, new Vector4f(0, 1, 0, -assets.water.getHeight()+1f));
		camera.getPosition().y += distance;
		camera.invertPitch();
		
		assets.waterBuffers.bindRefractionFrameBuffer();
		assets.renderer.renderScene(assets.player, assets.entities, world.terrain, assets.lights, camera, new Vector4f(0, -1, 0, assets.water.getHeight()+1f));
		
		assets.waterBuffers.unbindCurrentFrameBuffer();
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		assets.renderer.renderScene(assets.player, assets.entities, world.terrain, assets.lights, camera, new Vector4f(0, -1, 0, 1000000));
		assets.renderer.renderWater(assets.waters, camera, assets.sun);
		
		//assets.particleMaster.renderParticles(camera);
		
		assets.guiRenderer.render(assets.guis);
		assets.fontRenderer.render(assets.fonts);
		
		assets.selectionBuffers.bindSelectionBuffer();
		assets.renderer.renderSelection(assets.entities, world.terrain, assets.player, camera);
		
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