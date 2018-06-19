package com.draglantix.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Matrix4f;

import com.draglantix.entities.Camera;
import com.draglantix.render.Loader;

public class ParticleMaster {

	private List<Particle> particles = new ArrayList<Particle>();
	private ParticleRenderer renderer;
	
	public void init(Loader loader, Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(loader, projectionMatrix);
	}
	
	public void update() {
		Iterator<Particle> iterator = particles.iterator();
		while(iterator.hasNext()) {
			Particle p = iterator.next();
			boolean dead = p.update();
			if(dead) {
				iterator.remove();
			}
		}
	}
	
	public void renderParticles(Camera camera) {
		renderer.render(particles, camera);
	}
	
	public void cleanUp() {
		renderer.cleanUp();
	}
	
	public void addParticle(Particle particle) {
		particles.add(particle);
	}
	
}
