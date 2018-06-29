package com.draglantix.particles;

import java.util.Random;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.draglantix.tools.Timer;

public class ParticleSystem {

	 private float pps;
	    private float speed, gravityComplient, lifeLength, scale,
	    maxScale, delta, particlesToCreate, partialParticle, dirX, dirZ;
	    
	    private int count, pointer = 0;
	    
	    private Vector3f velocity = new Vector3f(),
	    		rotation = new Vector3f();
	    
	    private Random rand = new Random();
	    
	    private ParticleTexture texture;
	    private Timer timer;
	    private Particle[] particles = new Particle[ParticleRenderer.MAX_INSTANCES];
	    private Vector2f atlasOffset;
	    
	    public ParticleSystem(Vector2f atlasOffset, ParticleTexture texture, float pps, float speed, float gravityComplient, float lifeLength, float maxScale) {
	    	this.atlasOffset = atlasOffset;
	    	this.maxScale = maxScale;
	    	this.texture = texture;
	    	this.pps = pps;
	        this.speed = speed;
	        this.gravityComplient = gravityComplient;
	        this.lifeLength = lifeLength;
	        timer = new Timer();
	    }
	     
	    public void generateParticles(Vector3f systemCenter){
	        delta = timer.getDelta();
	        particlesToCreate = pps * delta;
	        count = (int) Math.floor(particlesToCreate);
	        partialParticle = particlesToCreate % 1;
	        
	        scale = rand.nextFloat() * maxScale + maxScale/2; 
	        
	        for(int i=0;i<count;i++){
	            emitParticle(systemCenter);
	        }
	        if(Math.random() < partialParticle){
	            emitParticle(systemCenter);
	        }
	    }
	     
	    private void emitParticle(Vector3f center){
	    	dirX = (float) Math.random() * 2f - 1f;
	        dirZ = (float) Math.random() * 2f - 1f;
	        velocity = new Vector3f(dirX, 1, dirZ);
	        velocity.normalize();
	        velocity.normalize(speed);
	  
	        if(pointer>=particles.length) {
	        	pointer = 0;
	        }
	        particles[pointer++] = new Particle(atlasOffset, texture, new Vector3f(center), velocity, gravityComplient, lifeLength, rotation, scale);
	    }
	
}
