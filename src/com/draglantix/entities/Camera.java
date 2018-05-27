package com.draglantix.entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.draglantix.render.Window;

public class Camera {
	
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera() {}
	
	public void move() {
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) {
			position.z-=2f;
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) {
			position.z+=2f;
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) {
			position.x-=2f;
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) {
			position.x+=2f;
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
			position.y+=2f;
		}
		if(Window.getInput().isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL)) {
			position.y-=2f;
		}
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
