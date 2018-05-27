package com.draglantix.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.draglantix.render.Window;

public class Camera {
	
	private Vector3f position = new Vector3f(0, .2f, 0);
	private float pitch = 0;
	private float yaw;
	private float roll;
	private float sprintSpeed;
	
	public Camera() {
		
	}
	
	public void move() {
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			position.z-=.2f * sprintSpeed * Math.cos(Math.toRadians(yaw));
			position.x+=.2f * sprintSpeed * Math.sin(Math.toRadians(yaw));
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			position.z+=.2f * sprintSpeed * Math.cos(Math.toRadians(yaw));
			position.x-=.2f * sprintSpeed * Math.sin(Math.toRadians(yaw));
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
			position.z+=.2f * sprintSpeed * Math.cos(Math.toRadians(yaw + 90));
			position.x-=.2f * sprintSpeed * Math.sin(Math.toRadians(yaw + 90));
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
			position.z+=.2f * sprintSpeed * Math.cos(Math.toRadians(yaw - 90));
			position.x-=.2f * sprintSpeed * Math.sin(Math.toRadians(yaw - 90));
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT) && position.y > 0.2f) {
			position.y-=.2f;
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_SPACE)) {
			position.y+=.2f;
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
			sprintSpeed = 4;
		} else {
			sprintSpeed = 1;
		}
		
		float dx = Window.getInput().getMousePos().x - Window.getWidth()/2;
		float dy = Window.getInput().getMousePos().y - Window.getHeight()/2;
		
		yaw += dx * (180/(float)Window.getWidth()/2);
		pitch += dy * (180/(float)Window.getHeight()/2);
		
		if(pitch>85) pitch = 85;
		if(pitch<-85) pitch = -85;

		Window.getInput().setMousePos(Window.getWidth()/2, Window.getHeight()/2);
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}

}
