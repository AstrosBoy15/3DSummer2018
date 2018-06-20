package com.draglantix.particles;

import org.joml.Vector3f;

import com.draglantix.tools.Timer;

public class ParticleSystem {

	 private float pps;
	    private float speed;
	    private float gravityComplient;
	    private float lifeLength;
	     
	    private Timer timer;
	    
	    private ParticleMaster particleMaster;
	    
	    public ParticleSystem(ParticleMaster particleMaster, float pps, float speed, float gravityComplient, float lifeLength) {
	        this.pps = pps;
	        this.speed = speed;
	        this.gravityComplient = gravityComplient;
	        this.lifeLength = lifeLength;
	        this.particleMaster = particleMaster;
	        timer = new Timer();
	    }
	     
	    public void generateParticles(Vector3f systemCenter){
	        float delta = timer.getDelta();
	        float particlesToCreate = pps * delta;
	        int count = (int) Math.floor(particlesToCreate);
	        float partialParticle = particlesToCreate % 1;
	        for(int i=0;i<count;i++){
	            emitParticle(systemCenter);
	        }
	        if(Math.random() < partialParticle){
	            emitParticle(systemCenter);
	        }
	    }
	     
	    private void emitParticle(Vector3f center){
	        float dirX = (float) Math.random() * 2f - 1f;
	        float dirZ = (float) Math.random() * 2f - 1f;
	        Vector3f velocity = new Vector3f(dirX, 1, dirZ);
	        velocity.normalize();
	        velocity.normalize(speed);
	        new Particle(particleMaster, new Vector3f(center), velocity, gravityComplient, lifeLength, new Vector3f(0, 0, 0), 1);
	    }
	
}
