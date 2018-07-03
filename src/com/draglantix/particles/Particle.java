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
	
	private int colorArray;
	private Vector3f[] colors;
	private Vector3f currentColor = new Vector3f(),
			nextColor = new Vector3f();
	
	private ParticleTexture texture;
	
	private Vector2f texOffset1 = new Vector2f();
	private Vector2f texOffset2 = new Vector2f();
	private float blend, colorBlend;
	
	private double elapsedTime = 0;
	private float distance;

	private Timer timer;
	
	private Vector3f change = new Vector3f(); 
	private float lifeFactor, atlasProgression;
	private int stageCount, index1, index2, column, row;
	private Vector2f atlasOffset;
	
	public Particle(int colorArray, Vector2f atlasOffset, ParticleTexture texture, Vector3f position, Vector3f velocity, 
			float gravityEffect, float lifeLength, Vector3f rotation, float scale) {
		this.colorArray = colorArray;
		this.atlasOffset = atlasOffset;
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		
		initColors();
		
		ParticleMaster.addParticle(this);
		timer = new Timer();
	}	
	
	private void initColors() {
		colors = new Vector3f[5];
		
		if(colorArray == 0) {
			colors[0] = new Vector3f(1, 1, 1);
			colors[1] = new Vector3f(1, 1, .2f);
			colors[2] = new Vector3f(1, .5f, 0);
			colors[3] = new Vector3f(1, 0, 0);
			colors[4] = new Vector3f(0, 0, 0);
		}else {
			colors[0] = new Vector3f(1, 1, 1);
			colors[1] = new Vector3f(.5f, 1, 1);
			colors[2] = new Vector3f(0, 1, 1);
			colors[3] = new Vector3f(0, .5f, 1);
			colors[4] = new Vector3f(0, 0, 1);
		}
	}
	
	public Vector3f getCurrentColor() {
		return currentColor;
	}
	
	public Vector3f getNextColor() {
		return nextColor;
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
	
	public float getColorBlend() {
		return colorBlend;
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
		
		updateColor();
		
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
	
	private void updateColor() {
		int maxColor = (colors.length);
		
		float progression = lifeFactor * maxColor;
		
	    int colorIndex = (int) Math.floor(progression);
	   
	    colorBlend = progression % 1;
	
	    if(colorIndex > maxColor-1) {
	    	colorIndex = maxColor-1;
	    }
	    int colorIndex2 = colorIndex < maxColor - 1 ? colorIndex + 1 : colorIndex;
	    
		currentColor.x = colors[colorIndex].x;
		currentColor.y = colors[colorIndex].y;
		currentColor.z = colors[colorIndex].z;
		
		nextColor.x = colors[colorIndex2].x;
		nextColor.y = colors[colorIndex2].y;
		nextColor.z = colors[colorIndex2].z;
		
	}
	
}
