package com.draglantix.font;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class FontTexture {

	private static String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,: %\"/?!()-+";
	
	private int texture;
	private Vector2f position;
	private float scale;
	
	private Vector3f color;
	
	private int[] textureIndex;
	private int rows;
	
	public FontTexture(int texture, String msg, Vector2f position, float scale, int rows, Vector3f color) {
		this.texture = texture;
		this.position = position;
		this.scale = scale;
		this.rows = rows;
		this.color = color;
		
		textureIndex = new int[msg.length()];
		
		msg = msg.toUpperCase();
	    int length = msg.length();
	    for(int i = 0; i<length; i++){
	    	int c = letters.indexOf(msg.charAt(i));
	    	if(c < 0) continue;
	    		textureIndex[i]=c;
	    }
	    
	}
	
	public int[] getTextureIndex() {
		return textureIndex;
	}
	
	public float getTextureXOffset(int i){
		int column = textureIndex[i]%rows;
		return (float)column/(float)rows;
	}

	public float getTextureYOffset(int i){
		int row = textureIndex[i]/rows;
		return (float)row/(float)rows;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	public float getScale() {
		return scale;
	}
	
	public int getRows() {
		return rows;
	}

	public Vector3f getColor() {
		return color;
	}
	
	
}
