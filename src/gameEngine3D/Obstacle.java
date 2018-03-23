package gameEngine3D;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

/**
 * This is an abstract class containing the basic functionality every obstacle should have.
 * Just like the Board and the Golfball it contains information of the Model, the ModelInstance 
 * and the ModelBuilder. To Specify the Shape and Object which should be created create a class 
 * which inherits (extends) this class and call the super constructor.
 * 
 * @author Daniel
 *
 */
public abstract class Obstacle {

	/** protected Attributes of the Obstacle. These can be used in inheriting classes **/
	protected Model model;
	protected ModelInstance modelInstance;
	protected ModelBuilder modelBuilder;
	protected Vector3 position;
	
	
	/**
	 * Create an instance of an abstract Obstacle
	 * @param posX The x position of the obstacle
	 * @param posY The y position of the obstacle
	 * @param posZ the z position of the obstacle
	 * @param height the height of the obstacle, 
	 * 		  needed to shift the obstacle on the 
	 *        y axis, because the x,y and z position 
	 *        of the obstacle is in the middle
	 */
	public Obstacle(float posX, float posY, float posZ, float height) {
		position = new Vector3(posX, posY+height/2, posZ);
		
		model = new Model();
		modelBuilder = new ModelBuilder();
		modelInstance = new ModelInstance(model);
		modelInstance.transform.translate(position);
	}
	
	/**
	 * Build the model. This method has to be overridden, since here you determine the
	 * actual shape of the model.
	 * @param model
	 */
	public abstract void buildModel(Model model);
	
	/**
	 * Set the position of the ModelInstance
	 * @param position the position of the ModelInstance
	 */
	public void setPosition(Vector3 position) {
		modelInstance.transform.translate(position);
	}
	
	/**
	 * 
	 * @return GEt the Model of the abstract obstacle
	 */
	public Model getModel() {
		return model;
	}
	
	/**
	 * 
	 * @return Gets the ModelInstance of the abstract obstacle
	 */
	public ModelInstance getInstance() {
		return modelInstance;
	}	
}
