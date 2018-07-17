package com.draglantix.particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Matrix4f;

import com.draglantix.entities.Camera;
import com.draglantix.render.ParticleRenderer;
import com.draglantix.shaders.ParticleShader;
import com.draglantix.tools.Loader;
import com.draglantix.tools.Sorter;

public class ParticleMaster {

	private static ParticleTexture texture;
	private static List<Particle> particles = new ArrayList<Particle>();
	private ParticleRenderer renderer;
	private ParticleShader shader = new ParticleShader();
	
	public void init(Loader loader, Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(loader, shader, projectionMatrix);
	}
	
	public void update(Camera camera) {
		Iterator<Particle> iterator = particles.iterator();
		while(iterator.hasNext()) {
			Particle p = iterator.next();
			boolean dead = p.update(camera);
			if(dead) {
				iterator.remove();
			}
		}
	}
	
	public void renderParticles(Camera camera) {
		shader.start();
		renderer.render(particles, texture, camera);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	public static void addParticle(Particle particle) {
		if(texture==null) {
			texture = particle.getTexture();
		}
		if(particles.size()<ParticleRenderer.MAX_INSTANCES) {
			particles.add(particle);
		}
		Sorter.sortParticles(particles);
	}
	
}
