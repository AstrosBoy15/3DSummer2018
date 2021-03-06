package com.draglantix.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joml.Vector3f;

import com.draglantix.assets.Assets;
import com.draglantix.audio.Source;
import com.draglantix.entities.Entity;
import com.draglantix.entities.Player;
import com.draglantix.terrains.Terrain;

public class World {

	private Assets assets;
	private Random rand = new Random();
	
	public Terrain terrain;
	
	public List<Terrain> terrains = new ArrayList<Terrain>();
	
	public World(Assets assets) {
		this.assets = assets;
		terrain = new Terrain(0, 0, assets.loader, assets.texturePackGrass, assets.blendMap);
		terrains.add(terrain);
	}
	
	public void spawnEntities() {
		
		for(int w=0; w < 50; w++) {
//			assets.entities.add(new Entity(assets.snowTree, generateEntityPos(terrain), 0, 0, 0, 5));
//			assets.entities.add(new Entity(assets.snowRock1, generateEntityPos(terrain), 0, 0, 0, 1));
//			assets.entities.add(new Entity(assets.snowRock2, generateEntityPos(terrain), 0, 0, 0, 2));
//			assets.entities.add(new Entity(assets.snowRock3, generateEntityPos(terrain), 0, 0, 0, 3));
//			assets.entities.add(new Entity(assets.snowPineTree, generateEntityPos(terrain), 0, 0, 0, 5));
//			assets.entities.add(new Entity(assets.snowman, generateEntityPos(terrain), 0, 0, 0, 3));
//			
			assets.entities.add(new Entity(assets.tree, generateEntityPos(terrain), generateEntityRot(), 5));
			assets.entities.add(new Entity(assets.mushroomTest, rand.nextInt(4), generateEntityPos(terrain), generateEntityRot(), .5f));
			assets.entities.add(new Entity(assets.rock1, generateEntityPos(terrain), generateEntityRot(), 1));
			assets.entities.add(new Entity(assets.rock2, generateEntityPos(terrain), generateEntityRot(), 2));
			assets.entities.add(new Entity(assets.rock3, generateEntityPos(terrain), generateEntityRot(), 3));
			assets.entities.add(new Entity(assets.grass, generateEntityPos(terrain), generateEntityRot(), 5));
			assets.entities.add(new Entity(assets.pineTree, generateEntityPos(terrain), generateEntityRot(), 5));
			Entity fallTree = new Entity(assets.fallTree, generateEntityPos(terrain), generateEntityRot(), 5);
			
			assets.entities.add(fallTree);
			
			Source musicSource = new Source();
			musicSource.setPosition(fallTree.getPosition().x, fallTree.getPosition().y, fallTree.getPosition().z);
			musicSource.setLooping(true);
			musicSource.setVolume(1f);
			musicSource.play(assets.musicAssets.insanityMusic);
			
		}
		createEntityIDSystem(assets.entities, assets.player);
	}
	
	private Vector3f generateEntityPos(Terrain terrain){
		float x = (rand.nextFloat() * Terrain.SIZE) + terrain.getX();
		float z = (rand.nextFloat() * Terrain.SIZE) + terrain.getZ();
		float y = terrain.getHeightOfTerrain(x, z);
		return new Vector3f(x, y, z);
	}
	
	private Vector3f generateEntityRot(){
		float x = 0;
		float y = (rand.nextFloat() * 360);
		float z = 0;
		return new Vector3f(x, y, z);
	}
	
	public void createEntityIDSystem(List<Entity> entities, Player player) {
		float x=0, y=0, z=0;
		double delta = 1;
		for(Entity e : entities){
			if(x<255) {
				x+=delta;
			}else if(y<255){
				x=0;
				y+=delta;
			}else if(z<255){
				y=0;
				z+=delta;
			}
			e.setID(new Vector3f(x/255, y/255, z/255));
		}
		player.setID(new Vector3f(0, 0, 0));
	}
	
	public Entity getEntityWithID(Vector3f iD, List<Entity> entities) {
		for(Entity e : entities) {
			
			if(e.getID().x == iD.x/255 &&
			   e.getID().y == iD.y/255 &&
			   e.getID().z == iD.z/255) {
				return e;
			}
		}
		return null;
	}
	
	
}
