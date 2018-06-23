package com.draglantix.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joml.Matrix4f;

import com.draglantix.entities.Camera;
import com.draglantix.render.Loader;

public class ParticleMaster {

	private static Map<ParticleTexture, List<Particle>> particles = new HashMap<ParticleTexture, List<Particle>>();
	private ParticleRenderer renderer;
	private ParticleShader shader = new ParticleShader();
	
	public void init(Loader loader, Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(loader, shader, projectionMatrix);
	}
	
	public void update(Camera camera) {
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();		
		while(mapIterator.hasNext()) {
			List<Particle> list = mapIterator.next().getValue();
			Iterator<Particle> iterator = list.iterator();
			while(iterator.hasNext()) {
				Particle p = iterator.next();
				boolean dead = p.update(camera);
				if(dead) {
					iterator.remove();
					if(list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
			InsertionSort.sortHighToLow(list);
		}
	}
	
	public void renderParticles(Camera camera) {
		shader.start();
		renderer.render(particles, camera);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
	
	public static void addParticle(Particle particle) {
		List<Particle> list = particles.get(particle.getTexture());
		if(list == null) {
			list = new ArrayList<Particle>();
			particles.put(particle.getTexture(), list);
		}
		list.add(particle);
	}
	
}
