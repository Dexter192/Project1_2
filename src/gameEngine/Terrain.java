package gameEngine;

import com.badlogic.gdx.graphics.Color;
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
	
	public Terrain(Vector3 coordinates, int height, int width, float hh) {
		this.coordinates = coordinates;
		this.height = height;
		this.width = width;
		
		Pixmap pixmap = new Pixmap( width, height, Format.RGBA8888 );
//		System.out.println("Z " + coordinates.z);
		float green = (float) Math.abs(Math.sin(coordinates.z/hh));
		pixmap.setColor(0, green, 0,1);
		
		if(coordinates.z < 0) {
			pixmap.setColor(Color.BLUE);
		}
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
	
	public float getFrictionConstant() { 
		return frictionConstant;
	}
	
}
