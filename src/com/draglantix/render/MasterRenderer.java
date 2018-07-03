package com.draglantix.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import com.draglantix.entities.Camera;
import com.draglantix.entities.Entity;
import com.draglantix.entities.Light;
import com.draglantix.models.TexturedModel;
import com.draglantix.particles.ParticleRenderer;
import com.draglantix.shaders.SelectionShader;
import com.draglantix.shaders.StaticShader;
import com.draglantix.shaders.TerrainShader;
import com.draglantix.shaders.WaterShader;
import com.draglantix.skybox.SkyboxRenderer;
import com.draglantix.terrains.Terrain;
import com.draglantix.water.WaterFrameBuffers;
import com.draglantix.water.WaterTile;

public class MasterRenderer {
	
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private static final float RED = 0.5444f;
	private static final float GREEN = 0.62f;
	private static final float BLUE = 0.69f;
	
	private Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private static EntityRenderer renderer;
	private static SelectionRenderer selectionRenderer;
	private SelectionShader selectionShader = new SelectionShader();
	
	private static TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private static WaterRenderer waterRenderer;
	private WaterShader waterShader = new WaterShader();
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxRenderer;
	
	public MasterRenderer(Loader loader, WaterFrameBuffers fbos) {
		enableCulling();
		createProjectionMatrix();
		renderer = new EntityRenderer(shader, projectionMatrix);
		selectionRenderer = new SelectionRenderer(selectionShader, projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		waterRenderer = new WaterRenderer(loader, waterShader, projectionMatrix, fbos);
		
	}
	
	public void updateProjectionMatrix() {
		createProjectionMatrix();
		renderer.updateProjectionMatrix(projectionMatrix);
		terrainRenderer.updateProjectionMatrix(projectionMatrix);
		waterRenderer.updateProjectionMatrix(projectionMatrix);
		selectionRenderer.updateProjectionMatrix(projectionMatrix);
		skyboxRenderer.updateProjectionMatrix(projectionMatrix);
		ParticleRenderer.updateProjectionMatrix(projectionMatrix);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling(){
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public void renderScene(Entity player, List<Entity> entities, Terrain terrain, List<Light> lights, 
			Camera camera, Vector4f clipPlane) {
		
		processEntity(player);
		
		processTerrain(terrain);
		
		for(Entity e : entities) {
			processEntity(e);
		}
		renderer(lights, camera, clipPlane);

		
	}
	
	public void renderSelection(List<Entity> entities, Terrain terrain, Entity player, Camera camera) { 
		
		for(Entity e : entities) {
			processEntity(e);
		}
		
		
		processTerrain(terrain);
		
		processEntity(player);
		
		rendererEntity(camera, player);

	}
	
	public void renderWater(List<WaterTile> waters, Camera camera, Light sun) {
		waterRenderer.render(waters, camera, sun, NEAR_PLANE, FAR_PLANE);
	}
	
	public void renderer(List<Light> lights, Camera camera, Vector4f clipPlane) {
		prepare();
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.renderer(terrains);
		terrainShader.stop();
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		terrains.clear();
		entities.clear();
	}
	
	public void rendererEntity(Camera camera, Entity player) {
		prepare();
		selectionShader.start();
		selectionShader.loadViewMatrix(camera);
		selectionRenderer.render(entities, player, terrains);
		selectionShader.stop();
		entities.clear();
	}
	
	public void processTerrain(Terrain terrain){
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch!=null) {
			batch.add(entity);
		}else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
	
	public void cleanUp(){
		selectionShader.cleanUp();
		shader.cleanUp();
		terrainShader.cleanUp();
		waterShader.cleanUp();
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 1);
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Window.getWidth() / (float) Window.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix._m00(x_scale);
		projectionMatrix._m11(y_scale);
		projectionMatrix._m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
		projectionMatrix._m23(-1);
		projectionMatrix._m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
		projectionMatrix._m33(0);
	}
	
}
