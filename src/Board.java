import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

public class Board {
	
	private Model boardModel;
	private ModelInstance boardInstance;
	private ModelBuilder modelBuilder;
	
	public Board(float width, float height, float depth) {
	
		modelBuilder = new ModelBuilder();
		
		boardModel = modelBuilder.createBox(width, height, depth, 
	            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
	            Usage.Position | Usage.Normal);
		boardInstance = new ModelInstance(boardModel);
	
	}	
	
	public Model getBoardModel() {
		return boardModel;
	}
	
	public ModelInstance getBoardInstance() {
		return boardInstance;
	}
	
}


