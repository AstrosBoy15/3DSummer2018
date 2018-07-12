package com.draglantix.entities;

import org.joml.Vector3f;

import com.draglantix.models.TexturedModel;

public class Entity {

	private TexturedModel model;
	protected Vector3f position;
	private Vector3f rotation;
	private Vector3f nextPos, nextRot;
	private float scale;
	
	private int textureIndex = 0;
	
	private Vector3f ID;
	
	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		updateNextVectors();
	}
	
	public Entity(TexturedModel model, int index, Vector3f position, Vector3f rotation, float scale) {
		this.textureIndex = index;
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		updateNextVectors();
	}
	
	public void updateNextVectors() {
		nextPos = new Vector3f(position);
		nextRot = new Vector3f(rotation);
	}
	
	public float getTextureXOffset(){
		int column = textureIndex%model.getTexture().getNumberOfRows();
		return (float)column/(float)model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset(){
		int row = textureIndex/model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
	}
	
	public void increasePosition(Vector3f delta) {
		nextPos.add(delta);
		
		position.lerp(nextPos, 0.07f);
	}
	
	public void increaseRotation(Vector3f delta) {
		nextRot.add(delta);
		
		rotation.lerp(nextRot, 0.07f);
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public Vector3f getID() {
		return ID;
	}

	public void setID(Vector3f iD) {
		ID = iD;
	}
	
}
