package MultiPlayer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import gameEngine3D.Golfball;
import physics.VectorComputation;

public class ElasticBand {

	private ModelInstance lineInstance;
	private Model lineModel;
	private ModelBuilder modelBuilder;
	private float maxAllowedDistance;
	private Golfball ball1;
	private Golfball ball2;
	private boolean elasticity;
	


	public ElasticBand(float maxAllowedDistance, Golfball ball1, Golfball ball2, boolean elasticity) {
		buildModel();
		this.maxAllowedDistance = maxAllowedDistance;
		this.ball1 = ball1;
		this.ball2 = ball2;
		this.elasticity = elasticity;
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
	public float calculateDistance(Vector3 pointA, Vector3 pointB) {
    	double x = Math.pow(pointA.x-pointB.x, 2);
    	double y = Math.pow(pointA.y-pointB.y, 2);
    	double z = Math.pow(pointA.z-pointB.z, 2);
    	double distance = Math.sqrt(x+y+z);
    	return (float) distance;
	}
	public void updateLine( ) {
		Vector3 golfballPosition = ball1.getPosition();
		Vector3 golfball2Position = ball2.getPosition();
		Color lineColor = Color.BLACK;
		// There must be a more efficient way, than disposing and redeclaring the
		// line...
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
		builder.setColor(lineColor);
		builder.line(golfballPosition, golfball2Position);
		lineModel = modelBuilder.end();
		lineInstance = new ModelInstance(lineModel);
		if(calculateDistance(golfballPosition, golfball2Position) > maxAllowedDistance) {
			if(elasticity) {
				ball1.setVelocity(ball1.getVelocity().scl(-1f));
				//change here
				ball2.setVelocity(ball1.getVelocity().scl(-0.5f));
			}
			else{
				ball1.setVelocity(new Vector3(0,0,0));//ball1.getVelocity().scl(-0.01f));
				ball2.setVelocity(new Vector3(0,0,0));
			}
		}
		//System.out.println("distance " + calculateDistance(golfballPosition, mousePosition));
	}

	public void setLine(Vector3 a, Vector3 b, Color color) {
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
		builder.setColor(color);
		builder.line(a, b);
		lineModel = modelBuilder.end();
		lineInstance = new ModelInstance(lineModel);		
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


