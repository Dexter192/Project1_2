package Obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class Hole extends Obstacle{

	private float radius;
	
	public Hole(float posX, float posY, float posZ, float radius) {
		super(posX, posY, posZ, radius);
		this.radius = radius;

		position.y = position.y - radius;
		
		buildModel();
		modelInstance = new ModelInstance(model);
		modelInstance.transform.translate(position);
	}

	@Override
	public void buildModel() {
		super.model = modelBuilder.createCylinder(radius*2, radius*2, radius*2, 1000, 
				new Material(ColorAttribute.createDiffuse(Color.BLACK)),
	            Usage.Position | Usage.Normal);		
	}

	public float getRadius() {
		return radius;
	}

}
