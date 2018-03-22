package gameEngine3D;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Terrain3D  {
	
	private Vector3 co;
	private Rectangle terrainShape;
	private Texture terrainImage;
	private float frictionConstant;
	private int height, width;
	protected Model model;
	protected ModelInstance modelInstance;
	protected ModelBuilder modelBuilder;
	protected Vector3 position;
	
	public Terrain3D(Vector3 co, int height, int width) {
		this.co = co;
		this.height = height;
		this.width = width;
		Pixmap pixmap = new Pixmap( width, height, Format.RGBA8888 );
		pixmap.setColor(0, 0, 5,1);
		pixmap.fillRectangle((int)co.x, (int)co.y, width, height);
	
		terrainImage = new Texture(pixmap);
	}
	public Vector3 getPosition() {
		return co;
	}
	public Model getModel() {
		return model;
	}
	public ModelInstance getInstance() {
		return modelInstance;
	}	

}
