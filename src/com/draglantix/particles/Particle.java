package com.draglantix.particles;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.draglantix.entities.Camera;
import com.draglantix.main.Main;
import com.draglantix.tools.Timer;

public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private Vector3f rotation;
	private float scale;
	
	private ParticleTexture texture;
	
	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend;
	
	private double elapsedTime = 0;
	private float distance;

	private Timer timer;
	
	private Vector3f change = new Vector3f(); 
	private float lifeFactor, atlasProgression;
	private int stageCount, index1, index2, column, row;
	private Vector2f atlasOffset;
	
	public Particle(Vector2f atlasOffset, ParticleTexture texture, Vector3f position, Vector3f velocity, 
			float gravityEffect, float lifeLength, Vector3f rotation, float scale) {
		this.atlasOffset = atlasOffset;
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		ParticleMaster.addParticle(this);
		timer = new Timer();
	}

	public float getDistance() {
		return distance;
	}

	public Vector2f getTexOffset1() {
		return texOffset1;
	}

	public Vector2f getTexOffset2() {
		return texOffset2;
	}

	public float getBlend() {
		return blend;
	}

	public ParticleTexture getTexture() {
		return texture;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}
	
	public Vector2f getAtlasOffset() {
		return atlasOffset;
	}
	
	protected boolean update(Camera camera){
		
		double passed = timer.getDelta();
		
		elapsedTime += passed;
		
		velocity.y += Main.GRAVITY * gravityEffect * passed;
		
		change.x = velocity.x;
		change.y = velocity.y;
		change.z = velocity.z;
		
		change.mul((float)passed);
		position.add(change);
		
		distance = camera.getPosition().sub(position).lengthSquared();
		
		updateTextureCoordInfo();
		
		return elapsedTime > lifeLength;
	}
	
	private void updateTextureCoordInfo() {
		lifeFactor = (float) (elapsedTime / lifeLength);
		
		stageCount = (texture.getNumberOfRows() * texture.getNumberOfRows());
		atlasProgression = lifeFactor * stageCount;
		index1 = (int) Math.floor(atlasProgression);
		index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}
	
	private void setTextureOffset(Vector2f offset, int index) {
		column = index % texture.getNumberOfRows();
		row =  index/ texture.getNumberOfRows();
		offset.x = (float)column / texture.getNumberOfRows();
		offset.y = (float)row / texture.getNumberOfRows();
	}
	
}
