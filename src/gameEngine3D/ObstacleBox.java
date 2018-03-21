package gameEngine3D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

public class ObstacleBox extends Obstacle {
	
	private float width, height, depth;
	
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
	            new Material(ColorAttribute.createDiffuse(Color.YELLOW)),
	            Usage.Position | Usage.Normal);	
	}
}
