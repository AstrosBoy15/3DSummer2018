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
	
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, Vector3f rotation,
			float scale) {
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
	
	protected boolean update(Camera camera){
		
		double passed = timer.getDelta();
		
		elapsedTime += passed;
		
		velocity.y += Main.GRAVITY * gravityEffect * passed;
		Vector3f change = new Vector3f(velocity);
		change.mul((float)passed);
		position.add(change);
		
		distance = camera.getPosition().sub(position).lengthSquared();
		
		updateTextureCoordInfo();
		
		return elapsedTime > lifeLength;
	}
	
	private void updateTextureCoordInfo() {
		float lifeFactor = (float) (elapsedTime / lifeLength);
		int stageCount = texture.getNumberOfRows() * texture.getNumberOfRows();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		this.blend = atlasProgression % 1;
		setTextureOffset(texOffset1, index1);
		setTextureOffset(texOffset2, index2);
	}
	
	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % texture.getNumberOfRows();
		int row =  index / texture.getNumberOfRows();
		offset.x = (float)column / texture.getNumberOfRows();
		offset.y = (float)row / texture.getNumberOfRows();
	}
	
}
