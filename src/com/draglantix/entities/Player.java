package com.draglantix.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.draglantix.main.Main;
import com.draglantix.models.TexturedModel;
import com.draglantix.render.Window;
import com.draglantix.terrains.Terrain;
import com.draglantix.tools.Timer;

public class Player extends Entity {
	
	private static final float RUN_SPEED = 100;
	private static final float TURN_SPEED = 160;
	private static final float JUMP_POWER = 30;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed = 0;
	
	private boolean isInAir = false;
	
	private Vector3f ID;
	
	private Timer timer;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		timer = new Timer();
	}
	
	public void move(Terrain terrain) {
		
		double passed = timer.getDelta();
		
		checkInputs();
		
		super.increaseRotation(0, currentTurnSpeed * (float) passed, 0);
		float distance = currentSpeed;
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx * (float) passed, 0, dz * (float) passed);
		upwardsSpeed += Main.GRAVITY * (float) passed;
		super.increasePosition(0, upwardsSpeed * (float) passed, 0);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y<terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
	}
	
	private void jump() {
		if(!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
	}
	
	private void checkInputs() {
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			this.currentSpeed = RUN_SPEED;
		} else if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			this.currentSpeed = -RUN_SPEED;
		} else {
			this.currentSpeed = 0;
		}
		
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (Window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
		
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			jump();
		}
		
		
	}
	
	public Vector3f getID() {
		return ID;
	}

	public void setID(Vector3f iD) {
		ID = iD;
	}
	

}
