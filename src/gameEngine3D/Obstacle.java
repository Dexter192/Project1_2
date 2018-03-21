package gameEngine3D;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public abstract class Obstacle {

	protected Model model;
	protected ModelInstance modelInstance;
	protected ModelBuilder modelBuilder;
	protected Vector3 position;
	
	public Obstacle(float posX, float posY, float posZ, float height) {
		position = new Vector3(posX, posY+height/2, posZ);
		
		model = new Model();
		modelBuilder = new ModelBuilder();
		modelInstance = new ModelInstance(model);
		modelInstance.transform.translate(position);
	}
	
	public abstract void buildModel(Model model);
	
	public void setPosition(Vector3 position) {
		modelInstance.transform.translate(position);
	}
	
	public Model getModel() {
		return model;
	}
	
	public ModelInstance getInstance() {
		return modelInstance;
	}	
}
