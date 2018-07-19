package com.draglantix.main;

import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.AL11;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.draglantix.assets.Assets;
import com.draglantix.audio.AudioMaster;
import com.draglantix.entities.Camera;
import com.draglantix.entities.Entity;
import com.draglantix.postProcessing.PostProcessing;
import com.draglantix.tools.Loader;
import com.draglantix.tools.Timer;
import com.draglantix.tools.Window;

public class Main {

	private float version = 0.15f;
	
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
		
		AudioMaster.init();
		AL10.alDistanceModel(AL11.AL_EXPONENT_DISTANCE_CLAMPED);
		
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
			assets.updateProcessingFBO();
			assets.processing.updateDimensions(Window.getWidth(), Window.getHeight());
		}
		
		if(Window.getInput().isKeyPressed(GLFW.GLFW_KEY_P)) {
			pause = !pause;
		}
		
		if(Window.getInput().isKeyPressed(GLFW.GLFW_KEY_ESCAPE)) {
			GLFW.glfwSetWindowShouldClose(Window.getWindow(), true);
		}
	
		if(!pause) {
			//assets.particleSystem.generateParticles(assets.player.getPosition());
			//assets.particleSystem2.generateParticles(new Vector3f(400, 40, 300));
			
			//assets.particleMaster.update(camera);
			
			assets.player.update(world.terrain);
			camera.update();
		
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
				assets.currentSelection.increaseRotation(new Vector3f(0, 1, 0), false);
			}
		}
	
	}
	
	public void render() {
		
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		
		assets.waterReflection.bindFrameBuffer();
		float distance = 2 * (camera.getPosition().y - assets.water.getHeight());
		camera.getPosition().y -= distance;
		camera.invertPitch();
		assets.renderer.renderScene(assets.player, assets.entities, world.terrain, assets.lights, camera, new Vector4f(0, 1, 0, -assets.water.getHeight()+1f));
		camera.getPosition().y += distance;
		camera.invertPitch();
		assets.waterReflection.unbindFrameBuffer();
		
		assets.waterRefraction.bindFrameBuffer();
		assets.renderer.renderScene(assets.player, assets.entities, world.terrain, assets.lights, camera, new Vector4f(0, -1, 0, assets.water.getHeight()+1f));
		assets.waterRefraction.unbindFrameBuffer();
		
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		
		//assets.multisampleFbo.bindFrameBuffer();		
		assets.renderer.renderScene(assets.player, assets.entities, world.terrain, assets.lights, camera, new Vector4f(0, -1, 0, 1000000));
		assets.renderer.renderWater(assets.waters, camera, assets.sun);		
		//assets.particleMaster.renderParticles(camera);		
		//assets.multisampleFbo.unbindFrameBuffer();
		//assets.multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT0, assets.outputFbo); //TODO Add this feature to AA the reflection texture of the water.
		//assets.multisampleFbo.resolveToFbo(GL30.GL_COLOR_ATTACHMENT1, assets.outputFbo2);
		//assets.processing.doPostProcessing(assets.outputFbo.getColourTexture(), assets.outputFbo2.getColourTexture());

		assets.guiRenderer.render(assets.guis);
		assets.fontRenderer.render(assets.fonts);
		
		assets.selectionBuffers.bindFrameBuffer();
		assets.renderer.renderSelection(assets.entities, world.terrain, assets.player, camera);
		assets.selectionBuffers.unbindFrameBuffer();
		
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
		assets.processing.cleanUp();
		loader.cleanUp();
		AudioMaster.cleanUp();
		assets.renderer.cleanUp();
		assets.guiRenderer.cleanUp();
		assets.fontRenderer.cleanUp();
		assets.selectionBuffers.cleanUp();
		assets.waterReflection.cleanUp();
		assets.waterRefraction.cleanUp();
		assets.multisampleFbo.cleanUp();
		assets.outputFbo.cleanUp();
		assets.outputFbo2.cleanUp();
		assets.particleMaster.cleanUp();
		GLFW.glfwTerminate();
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
}