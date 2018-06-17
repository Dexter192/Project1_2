package Obstacles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;

public class Hole extends Obstacle{

	private float radius;
	private int index;
	private Vector3 center;
	public Hole(float posX, float posY, float posZ, float radius) {
		super(posX, posY, posZ, radius);
		this.radius = radius;
		center = new Vector3((posX+radius/2),(posY),(posZ));
		//position.y = position.y - radius;
		buildModel();
		modelInstance = new ModelInstance(model);
		modelInstance.transform.translate(position);
	}

	@Override
	public void buildModel() {
		super.model = modelBuilder.createCylinder(radius*2, radius*2, radius*2, 1000, 
				new Material(ColorAttribute.createDiffuse(Color.BLUE)),
	            Usage.Position | Usage.Normal);		
	}

	public float getRadius() {
		return radius;
	}
	public void setColour(int i) {
		index = i;
		Attribute colourAttribute = ColorAttribute.createDiffuse(Color.WHITE);
		if(i == 0) colourAttribute = ColorAttribute.createDiffuse(Color.RED);		
		if(i == 1) colourAttribute = ColorAttribute.createDiffuse(Color.BLUE);
		if(i == 2) colourAttribute = ColorAttribute.createDiffuse(Color.YELLOW);		
		if(i == 3) colourAttribute = ColorAttribute.createDiffuse(Color.ORANGE);
		if(i == 4) colourAttribute = ColorAttribute.createDiffuse(Color.OLIVE);
		if(i == 5) colourAttribute = ColorAttribute.createDiffuse(Color.FOREST);
		modelInstance.materials.get(0).set(colourAttribute);
	}
	public Vector3 getCenter() {
		return center;
	}
	public int getIndex() {
		return index;
	}
}
