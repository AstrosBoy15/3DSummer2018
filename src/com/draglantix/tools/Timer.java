package com.draglantix.tools;

import org.lwjgl.glfw.GLFW;

public class Timer {

    private double lastTime;
   
    public Timer() {
        lastTime = getTime();
    }

    public double getTime() {
		return GLFW.glfwGetTime();
    }
    
	public float getDelta() {
        double time = getTime();
        float delta = (float) (time - lastTime);
        lastTime = time;
        return delta;
	}

}