package gameEngine3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import gameEngine.Physics;
import physics.VectorComputation;

/**
 * A class representing the Golfball as a 3D sphere.
 * 
 * @author Daniel
 *
 */
public class Golfball {
	private final float g = 9.81f;
	private Model ballModel;
	private ModelInstance ballInstance;
	private ModelBuilder modelBuilder;
	private Vector3 directionVector;
	private BoundingBox boundingBox;
	private boolean moveWithKeys = true;
	private Vector3 position;
	private float radius;
	private float mass;
	private Physics physics;
	private float friction;
	public Golfball(float radius) {
		this.radius = radius;
		directionVector = new Vector3(0, 0, 0);

		modelBuilder = new ModelBuilder();

		ballModel = modelBuilder.createSphere(radius * 2, radius * 2, radius * 2, 50, 50, new Material(),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
						| VertexAttributes.Usage.TextureCoordinates);

		position = new Vector3(100, radius * 2, 100);

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
		return directionVector;
	}

	/**
	 * Updates the ball. Primarily its position.
	 */
	public void update() {
		ignoreMinimalVelocity();
		// Transform the ballposition by the directionvector
		position.add(directionVector);

		// ballInstance.transform.translate(directionVector);
		ballInstance.transform.setTranslation(position);

		Vector3 min = new Vector3(-radius, -radius, -radius);
		Vector3 max = new Vector3( radius,  radius,  radius);
		boundingBox = boundingBox.set(min.add(position), max.add(position));

		directionVector.scl(0.95f);
		
		moveWithKeys();
	}

	private void moveWithKeys() {
		if(moveWithKeys) {
			if (Gdx.input.isKeyPressed(Keys.Q)) {
				position.add(new Vector3(0.1f, 0, 0));
			}
			if (Gdx.input.isKeyPressed(Keys.E)) {
				position.add(new Vector3(-0.1f, 0, 0));
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				position.add(new Vector3(0, 0, 0.1f));
			}
			if (Gdx.input.isKeyPressed(Keys.A)) {
				position.add(new Vector3(0, 0, -0.1f));
			}
		}
	}
	
	private void ignoreMinimalVelocity() {
		if (Math.abs(directionVector.x) <= 0.01) {
			directionVector.x = 0;
		}
		if (Math.abs(directionVector.y) <= 0.01) {
			directionVector.y = 0;
		}
		if (Math.abs(directionVector.z) <= 0.01) {
			directionVector.z = 0;
		}
	}

	public Vector3 getVelocity() {
		return directionVector;
	}

	public void setVelocity(Vector3 directionVector) {
		this.directionVector = directionVector;
	}

	public void addVelocity(Vector3 directionVector) {
		this.directionVector.add(directionVector);
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
		directionVector.scl(axis);
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
}
