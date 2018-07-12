package com.draglantix.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.draglantix.audio.AudioMaster;
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
	
	private Vector3f position = new Vector3f(), rotation = new Vector3f(), ID;
	
	private Timer timer;
	
	public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
		AudioMaster.setListenerData(super.position.x, super.position.y, 0);
		timer = new Timer();
	}
	
	public void update(Terrain terrain) {
		
		double passed = timer.getDelta();
		
		checkInputs();
		
		rotation.y = currentTurnSpeed * (float) passed;
		
		super.increaseRotation(rotation);
		float distance = currentSpeed;
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotation().y)));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotation().y)));
		upwardsSpeed += Main.GRAVITY * (float) passed;
		
		position.x = dx * (float) passed;
		position.y = upwardsSpeed * (float) passed;
		position.z = dz * (float) passed;
		
		super.increasePosition(position);
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y<terrainHeight) {
			upwardsSpeed = 0;
			isInAir = false;
			super.getPosition().y = terrainHeight;
		}
		
		AudioMaster.setListenerData(super.position.x, super.position.y, 0);
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
