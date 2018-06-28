package MultiPlayer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import gameEngine3D.Golfball;
public class ElasticBand {

	private ModelInstance lineInstance;
	private Model lineModel;
	private ModelBuilder modelBuilder;
	private float maxAllowedDistance;
	private Golfball ball1;
	private Golfball ball2;
	private boolean elasticity;
	private boolean done = false;
	//Constructor
	public ElasticBand(float maxAllowedDistance, Golfball ball1, Golfball ball2, boolean elasticity) {
		buildModel();	
		this.maxAllowedDistance = maxAllowedDistance;
		this.ball1 = ball1;
		this.ball2 = ball2;
		this.elasticity = elasticity;
	}

	//Update for continuous distance checks (including elastic band mode)
	public void updateLine( ) {
		this.update();
		if(calculateDistance(ball1.getPosition(),ball2.getPosition()) > maxAllowedDistance) {
			maxAllowedDistance++;
			if(elasticity&& !done ) {
				Vector3 temp = addVelocity(ball1.getVelocity().scl(-1f),ball2.getVelocity().scl(-0.5f));
				Vector3 temp2 = addVelocity(ball2.getVelocity().scl(-1f),ball1.getVelocity().scl(-0.5f));
				ball1.setVelocity(temp);
				ball2.setVelocity(temp2);
				
				done = true; 
			}
			else{
				if(isMoving(ball1.getVelocity())) ball1.setVelocity(ball1.getVelocity().scl(-0.1f));
				if(isMoving(ball2.getVelocity())) ball2.setVelocity(ball2.getVelocity().scl(-0.1f));
			}
		}
		else  {

			if(done) maxAllowedDistance--;
			done = false;
		}
	}
	//Update for default game
	public void updateLineDefaultGame() {
		this.update();
		if(!isMoving(ball1.getVelocity()) && !isMoving(ball2.getVelocity())) {
			if(calculateDistance(ball1.getPosition(), ball2.getPosition()) > maxAllowedDistance) {
				ball1.setBack();
				ball2.setBack();
			}
			else {
				ball1.setInitialPosition(ball1.getPosition());
				ball2.setInitialPosition(ball2.getPosition());
			}
		}
	}
	//GRAPHICS
	public void buildModel() {
		modelBuilder = new ModelBuilder();
		modelBuilder.begin();
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
	public void update() {
		Color lineColor = Color.BLACK;
		modelBuilder.begin();
		MeshPartBuilder builder = modelBuilder.part("line", 1, 3, new Material());
		builder.setColor(lineColor);
		builder.line(ball1.getPosition(),ball2.getPosition());
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
	
	//supporting computational methods
	public Vector3 addVelocity(Vector3 a, Vector3 b) {
		return new Vector3((a.x+b.x), (a.y+b.y), (a.z+b.z));
	}

	public boolean isMoving (Vector3 vector) {
		if(compare(vector, new Vector3 (0,0,0))) return false;
		else return true;
	}
	public float calculateDistance(Vector3 pointA, Vector3 pointB) {
    	double x = Math.pow(pointA.x-pointB.x, 2);
    	double y = Math.pow(pointA.y-pointB.y, 2);
    	double z = Math.pow(pointA.z-pointB.z, 2);
    	double distance = Math.sqrt(x+y+z);
    	return (float) distance;
	}
	public boolean compare(Vector3 one, Vector3 two) {
		if(one.x == two.x && one.y == two.y && one.z == two.z) return true;
		else return false;
	}
}


