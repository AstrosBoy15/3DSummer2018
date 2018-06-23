package com.draglantix.particles;

public class ParticleTexture {

	private int textureID;
	private int numberOfRows;
	private boolean additiveBlend;
	
	public ParticleTexture(int textureID, int numberOfRows, boolean additiveBlend) {
		this.textureID = textureID;
		this.numberOfRows = numberOfRows;
		this.additiveBlend = additiveBlend;
	}

	public int getTextureID() {
		return textureID;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}

	public boolean isAdditiveBlend() {
		return additiveBlend;
	}
	
}
