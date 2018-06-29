package com.draglantix.main;

import java.util.Random;

public class World {

	public static int[][] map = new int[10][5];
	
	private static Random rand = new Random();
	
	public static void init() {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				map[i][j] = rand.nextInt(2);
				System.out.println(map[i][j]);
			}
		}
	}
	
}
