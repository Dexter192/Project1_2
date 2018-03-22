package gameEngine;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Terrain {
	
	private Vector3 co;
	private Rectangle terrainShape;
	private Texture terrainImage;
	private float frictionConstant;
	private int height, width;
	
	public Terrain(Vector3 co, int height, int width) {
		this.co = co;
		this.height = height;
		this.width = width;
//		System.out.println(co.z);
		Pixmap pixmap = new Pixmap( width, height, Format.RGBA8888 );
		pixmap.setColor(0, co.z, 0,1);
		pixmap.fillRectangle(0, 0, width, height);
	
		terrainImage = new Texture(pixmap);
	}
	
	public Texture getSprite() {
		return terrainImage;
	}
	public Vector3 getPosition() {
		return co;
	}
	
	public String toString() {
		return co + ", " + width;
	}
}
