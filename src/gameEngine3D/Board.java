package gameEngine3D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/**
 * This class represents a 3D Model of a Board.
 * It holds a Model, a ModelInstance and a ModelBuilder
 * <ul>
 * 	<li> Model - The Model containing information about 
 * 				 size and position of the board. It can 
 * 				 be retrieved by calculating the 
 * 				 BoundingBox of the model
 *  <li> ModelInstance - The Modelinstance is what is drawn on the screen.
 *  					 It is build from the Model
 *  <li> Modelbuilder - Actually creates the model 
 * </ul>
 * 
 * @author Daniel
 * 
 */
public class Board {
	
	private Model boardModel;
	private ModelInstance boardInstance;
	private ModelBuilder modelBuilder;
	
	/**
	 * Build a Rectangle at position 0,0,0 with a specified width, height and depth
	 * @param width the width of the board
	 * @param height the height of the board
	 * @param depth the depth of the board
	 */
	public Board(float width, float height, float depth) {
	
		modelBuilder = new ModelBuilder();
		
		boardModel = modelBuilder.createBox(width, height, depth, 
	            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
	            Usage.Position | Usage.Normal);
		boardInstance = new ModelInstance(boardModel);
	
	}	
	
	/**
	 * 
	 * @return The Model of the board
	 */
	public Model getBoardModel() {
		return boardModel;
	}
	
	/**
	 * 
	 * @return The ModelInstance of the board
	 */
	public ModelInstance getBoardInstance() {
		return boardInstance;
	}
	
}


