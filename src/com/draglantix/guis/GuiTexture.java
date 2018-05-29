package com.draglantix.guis;

import org.joml.Vector2f;

public class GuiTexture {

	private int texture;
	private Vector2f position;
	private float scale;
	
	public GuiTexture(int texture, Vector2f position, float scale) {
		super();
		this.texture = texture;
		this.position = position;
		this.scale = scale;
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
	
	
	
}
