package gameEngine;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Terrain {
	
	private Vector3 coordinates;
	private Rectangle terrainShape;
	private Texture terrainImage;
	private float frictionConstant = 1;
	private int height, width;
	
	public Terrain(Vector3 coordinates, int height, int width) {
		this.coordinates = coordinates;
		this.height = height;
		this.width = width;
		
		Pixmap pixmap = new Pixmap( width, height, Format.RGBA8888 );
		pixmap.setColor(0, coordinates.z, 0,1);
		pixmap.fillRectangle(0, 0, width, height);
	
		terrainImage = new Texture(pixmap);
	}
	
	public Texture getSprite() {
		return terrainImage;
	}
	public Vector3 getPosition() {
		return coordinates;
	}
	
	public String toString() {
		return coordinates + ", " + width;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
