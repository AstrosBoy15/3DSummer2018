package com.draglantix.particles;

import org.joml.Vector3f;

import com.draglantix.main.Main;
import com.draglantix.tools.Timer;

public class Particle {

	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	
	private double elapsedTime = 0;
	private double startTime;

	private Timer timer;
	
	public Particle(ParticleMaster particleMaster, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation,
			float scale) {
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		particleMaster.addParticle(this);
		timer = new Timer();
		startTime = timer.getTime();
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}
	
	protected boolean update(){
		
		double passed = timer.getDelta();
		
		elapsedTime += passed;
		
		velocity.y += Main.GRAVITY * gravityEffect * passed;
		Vector3f change = new Vector3f(velocity);
		change.mul((float)passed);
		change.add(position);
		
		return elapsedTime > lifeLength;
	}
	
}
