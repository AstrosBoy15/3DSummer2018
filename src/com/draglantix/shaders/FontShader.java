package com.draglantix.shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
 
public class FontShader extends ShaderProgram{
     
    private static final String VERTEX_FILE = "shaders/fontVertex.glsl";
    private static final String FRAGMENT_FILE = "shaders/fontFragment.glsl";
     
    private int location_transformationMatrix;
	private int location_numberOfRows;
	private int location_offset;
	private int location_fontColor;
    
    public FontShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }
    
    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
		location_offset = super.getUniformLocation("offset");
		location_fontColor = super.getUniformLocation("fontColor");
    }
    
    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
    
    public void loadFontColor(Vector3f color) {
    	super.loadVector(location_fontColor, color);
    }
    
    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }
 
	public void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(location_numberOfRows, numberOfRows);
	}
	
	public void loadOffset(float x, float y) {
		super.load2DVector(location_offset, new Vector2f(x, y));
	}     
 
}