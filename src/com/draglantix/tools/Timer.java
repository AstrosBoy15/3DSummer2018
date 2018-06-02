package com.draglantix.tools;

public class Timer {
	public static double getTimeSec() {
		return (double)System.nanoTime()/(double) 1000000000L;
	}
	
	public static double getTimeMin() {
		return getTimeSec()/60;
	}
}