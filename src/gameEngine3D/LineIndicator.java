package gameEngine3D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import physics.VectorComputation;

public class LineIndicator {

	private ModelInstance lineInstance;
	private Model lineModel;
	private ModelBuilder modelBuilder;

	public LineIndicator() {
		buildModel();
	}

	/**
	 * Builds a model
	 */
	public void buildModel() {
		modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
		builder.setColor(Color.RED);
		builder.line(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		lineModel = modelBuilder.end();
		lineInstance = new ModelInstance(lineModel);
	}

	public void updateLine(Vector3 golfballPosition, Vector3 mousePosition) {

		float lineLength = VectorComputation.getInstance().getDistanceXZ(golfballPosition, mousePosition);
		Color lineColor = computeLineColor(lineLength);
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
		builder.setColor(lineColor);
		builder.line(golfballPosition, mousePosition);
		lineModel = modelBuilder.end();
		lineInstance = new ModelInstance(lineModel);
	}

	public void setLine(Vector3 a, Vector3 b, Color color) {
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
		builder.setColor(color);
		builder.line(a, b);
		lineModel = modelBuilder.end();
		lineInstance = new ModelInstance(lineModel);		
	}
	/**
	 * Computes a color for the line between the ball and the cursor. This line
	 * should become more red, if the curser is far from the ball and green, if it
	 * is close.
	 * 
	 * @param lineLength
	 *            The length of the line, that is between the ball and the cursor
	 * @return the color of the line
	 */
	public Color computeLineColor(float lineLength) {
		Color lineColor = new Color();
		lineColor.b = 0;
		lineColor.g = (lineLength >= 255) ? 255 : (int)255-lineLength; 
		lineColor.r = 255-lineColor.g;
		
		return lineColor;

	}

	public ModelInstance getInstance() {
		return lineInstance;
	}

	public Model getModel() {
		return lineModel;
	}
	
	public void dispose() {
		lineModel.dispose();
	}

}
