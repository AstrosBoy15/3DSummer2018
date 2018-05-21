package com.draglantix.main;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Input {
	private long window;
	
	private boolean keys[];
	
	public Input(long window) {
		this.window = window;
		this.keys = new boolean[GLFW_KEY_LAST];
		for(int i = 0; i < GLFW_KEY_LAST; i++)
			keys[i]=false;
	}
	
	public boolean isKeyDown(int key){
		return glfwGetKey(window, key) == 1;
	}
	
	public boolean isKeyPressed(int key) {
		return (isKeyDown(key) && !keys[key]);
	}
	
	public boolean isKeyReleased(int key) {
		return (!isKeyDown(key) && keys[key]);
	}
	
	public boolean isMouseButtonDown(int button){
		return glfwGetMouseButton(window, button) == 1;
	}
	
	public Vector2f getMousePos() {
		double[] posx = new double[] {0};
		double[] posy = new double[] {0};
		DoubleBuffer.wrap(posx);
		GLFW.glfwGetCursorPos(window, posx, posy);
		double x = posx[0];
		double y = posy[0];
		return new Vector2f((float)x, (float)y);
	}
	
	public void update() {
		for(int i = 32; i < GLFW_KEY_LAST; i++)
			keys[i]= isKeyDown(i);
	}
}