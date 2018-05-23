package com.draglantix.render;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.*;

import com.draglantix.tools.Input;

public class Window {
	
	private static long window;
	
	private static int width;

	private static int height;
	private static boolean fullscreen;
	private static boolean hasResized;
	private GLFWWindowSizeCallback windowSizeCallback;
	
	private static Input input;
	
	public static void setCallbacks() {
		glfwSetErrorCallback(GLFWErrorCallback.createPrint(System.err));
	}
	
	private void setLocalCallbacks(){
		windowSizeCallback = new GLFWWindowSizeCallback(){

			@Override
			public void invoke(long argWindow, int argWidth, int argHeight) {
				width = argWidth;
				height = argHeight;
				hasResized = true;
			}
			
		};
		
		glfwSetWindowSizeCallback(window, windowSizeCallback);
	}
	
	public Window() {
		GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
		setSize(vid.width(), vid.height());
		setFullscreen(true);
		hasResized = false;
	}
	
	public void createWindow(String title){
		
		window = glfwCreateWindow(
				width,
				height,
				title, 
				0,
				0);
		
		if(window == 0)
			throw new IllegalStateException("Failed to create window!");
		
		if(!fullscreen) {
			GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window,
					(vid.width()-width)/2,
					(vid.height()-height)/2);
			
			glfwShowWindow(window);
		}
		
		glfwMakeContextCurrent(window);
		
		input = new Input(window);
		setLocalCallbacks();
	}
	
	public void cleanUp(){
		glfwFreeCallbacks(window);
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(window);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	
	public void update() {
		hasResized = false;
		input.update();
		glfwPollEvents();
	}
	
	public static int getWidth() {return width;}
	public static int getHeight() {return height;}
	public static boolean hasResized() { return hasResized; }
	public static boolean isFullscreen() { return fullscreen; }
	public static long getWindow() { return window; }
	public static Input getInput() { return input; }
}