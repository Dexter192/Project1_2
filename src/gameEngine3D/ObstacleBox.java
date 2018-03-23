package gameEngine3D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

/**
 * A class to create a 3D obstacle with the shape of a box
 * @author Daniel
 *
 */
public class ObstacleBox extends Obstacle {
	
	private float width, height, depth;
	
	/**
	 * Create an Obstacle with the shape of a Box
	 * @param posX The x position of the obstaclebox
	 * @param posY The y position of the obstaclebox
	 * @param posZ The z position of the obstaclebox
	 * @param width The width of the obstaclebox
	 * @param height The height of the obstaclebox
	 * @param depth The depth of the obstaclebox
	 */
	public ObstacleBox(float posX, float posY, float posZ, float width, float height, float depth) {
		super(posX, posY, posZ, height);
		this.height = height;
		this.width = width;
		this.depth = depth;
		
		buildModel(model);
		modelInstance = new ModelInstance(model);
		modelInstance.transform.translate(position);
	}

	@Override
	public void buildModel(Model model) {
		super.model = modelBuilder.createBox(width, height, depth, 
	            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
	            Usage.Position | Usage.Normal);	
	}
}
