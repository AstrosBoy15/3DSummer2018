package com.draglantix.shadows;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.draglantix.models.RawModel;
import com.draglantix.terrains.Terrain;
import com.draglantix.tools.Maths;

public class ShadowMapTerrainRenderer {

	private Matrix4f projectionViewMatrix;
	private ShadowShader shader;

	/**
	 * @param shader
	 *            - the simple shader program being used for the shadow render
	 *            pass.
	 * @param projectionViewMatrix
	 *            - the orthographic projection matrix multiplied by the light's
	 *            "view" matrix.
	 */
	protected ShadowMapTerrainRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}

	/**
	 * Renders entieis to the shadow map. Each model is first bound and then all
	 * of the entities using that model are rendered to the shadow map.
	 * 
	 * @param entities
	 *            - the entities to be rendered to the shadow map.
	 */
	public void renderer(List<Terrain>terrains) {
		for(Terrain terrain:terrains) {
			bindModel(terrain.getModel());
			prepareInstance(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);;
		}
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Binds a raw model before rendering. Only the attribute 0 is enabled here
	 * because that is where the positions are stored in the VAO, and only the
	 * positions are required in the vertex shader.
	 * 
	 * @param rawModel
	 *            - the model to be bound.
	 */
	private void bindModel(RawModel rawModel) {
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
	}

	/**
	 * Prepares an entity to be rendered. The model matrix is created in the
	 * usual way and then multiplied with the projection and view matrix (often
	 * in the past we've done this in the vertex shader) to create the
	 * mvp-matrix. This is then loaded to the vertex shader as a uniform.
	 * 
	 * @param entity
	 *            - the entity to be prepared for rendering.
	 */
	private void prepareInstance(Terrain terrain) {
		Matrix4f modelMatrix = Maths.createTransformationMatrix(
				new Vector3f(terrain.getX(), 0, terrain.getZ()), 0, 0, 0, 1);
		Matrix4f mvpMatrix = new Matrix4f();
		projectionViewMatrix.mul(modelMatrix, mvpMatrix);
		shader.loadMvpMatrix(mvpMatrix);
	}

}
