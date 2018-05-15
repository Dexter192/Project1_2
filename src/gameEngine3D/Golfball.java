package gameEngine3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import physics.DifferentialEquationSolver;
import physics.Physics;

/**
 * A class representing the Golfball as a 3D sphere.
 * 
 * @author Daniel
 *
 */
public class Golfball {
	private Model ballModel;
	private ModelInstance ballInstance;
	private ModelBuilder modelBuilder;
	private BoundingBox boundingBox;
	private Vector3 position;
	private float radius;
	private float mass;
	private Vector3[] veloAccel = {new Vector3(0,0,0), new Vector3(0,0,0)};
	// Note that veloAccel[0] is the direction vector		
	public Golfball(float radius) {
		this.radius = radius;
		mass = 10;
		modelBuilder = new ModelBuilder();

		ballModel = modelBuilder.createSphere(radius * 2, radius * 2, radius * 2, 50, 50, new Material(),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
						| VertexAttributes.Usage.TextureCoordinates);

		position = new Vector3(0, radius * 2, 0);

		ballInstance = new ModelInstance(ballModel);
		ballInstance.transform.translate(position);
		getBoundingBox();
	}

	/**
	 * 
	 * @return The Model of the ball
	 */
	public Model getBallModel() {
		return ballModel;
	}

	/**
	 * 
	 * @return The ModelInstance of the ball
	 */
	public ModelInstance getBallInstance() {
		return ballInstance;
	}

	public Vector3 getVector() {
		return veloAccel[0];
	}

	/**
	 * Updates the ball. Primarily its position.
	 */
	public void update(DifferentialEquationSolver ode) {
		ignoreMinimalVelocity();
		// Transform the ballposition by the directionvector
		position.add(veloAccel[0]); 

		//ballInstance.transform.translate(veloAccel[0]);
		ballInstance.transform.setTranslation(position);
		
		Vector3 min = new Vector3(-radius, -radius, -radius);
		Vector3 max = new Vector3( radius,  radius,  radius);
		boundingBox = boundingBox.set(min.add(position), max.add(position));
		
		if(Math.abs(veloAccel[0].x)>0 || Math.abs(veloAccel[0].z)>0) 
			veloAccel = ode.rungeKutterMethod(veloAccel, position);
	}
		
	

	
	private void ignoreMinimalVelocity() {
		if (Math.abs(veloAccel[0].x) <= 0.01) {
			veloAccel[0].x = 0;
		}
		if (Math.abs(veloAccel[0].z) <= 0.01) {
			veloAccel[0].z = 0;
		}
	}

	public Vector3 getVelocity() {
		return veloAccel[0];
	}

	public void setVelocity(Vector3 directionVector) {
		veloAccel[0] = directionVector;
	}

	public void addVelocity(Vector3 directionVector) {
		veloAccel[0].add(directionVector);
		
	}
	
	public void setPosiition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getPosition() {
		return ballInstance.transform.getTranslation(new Vector3());
	}
	
	public void setPosition(Vector3 newPosition) {
		this.position = newPosition;
	}

	/**
	 * TODO: Implement propper bouncing of
	 */
	public void bounceOff(Vector3 axis) {
		veloAccel[0].scl(axis);
	}

	public float getRadius() {
		return radius;
	}
	
	public BoundingBox getBoundingBox() {
		if (boundingBox == null) {
			boundingBox = new BoundingBox();
			boundingBox = ballInstance.calculateBoundingBox(boundingBox);
			Vector3 min = new Vector3();
			Vector3 max = new Vector3();
			boundingBox.set(min.add(position), max.add(position));
		}
		return boundingBox;
	}
	public float getMass() {
		return mass;
	}
}
