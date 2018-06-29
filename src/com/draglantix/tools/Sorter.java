package com.draglantix.tools;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.draglantix.particles.Particle;

public class Sorter {

	private static ParticleComparator pc = new ParticleComparator();
	
	public static void sortParticles(List<Particle> list) {
		Collections.sort(list, pc);
	}
	
	public static class ParticleComparator implements Comparator<Particle>{

		@Override
		public int compare(Particle o1, Particle o2) {
			float dis1 = o1.getDistance();
			float dis2 = o2.getDistance();
			if (dis1 < dis2) return -1;
			if (dis1 > dis2) return 1;
			return 0;
		}
		
	}
	
}
