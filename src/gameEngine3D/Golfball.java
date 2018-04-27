package gameEngine3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import gameEngine.Physics;

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
	private Vector3 position;
	private float radius;
	private float mass;
	private Physics physics;
	private float friction;
	public Golfball(float radius) {
		this.radius = radius;
		mass = 10;
		friction = 10; 
		directionVector = new Vector3(0, 0, 0);
		float[] a = { 0.01f,0 };
		float[] b = { 0.01f,0 };
		physics = new Physics(a, b);

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
		return directionVector;
	}

	/**
	 * Updates the ball. Primarily its position.
	 */
	public void update() {
		ignoreMinimalVelocity();
		// Transform the ballposition by the directionvector
		position.add(directionVector); //whaat

		// ballInstance.transform.translate(directionVector);
		ballInstance.transform.setTranslation(position);
		
		Vector3 min = new Vector3(-radius, -radius, -radius);
		Vector3 max = new Vector3( radius,  radius,  radius);
		boundingBox = boundingBox.set(min.add(position), max.add(position));
		
		if(Math.abs(directionVector.x) > 0 || Math.abs(directionVector.z) > 0) {
			float newx = 0, newy = 0; 
			if(Math.abs(directionVector.x) > 0.01) newx = Gdx.graphics.getDeltaTime()*fx(position.x,position.z, directionVector.x, directionVector.z)/mass;
			else newx = 0;
			if(Math.abs(directionVector.z) > 0.01) newy = Gdx.graphics.getDeltaTime()*fy(position.x,position.z, directionVector.x, directionVector.z)/mass;
			else newy = 0;
		System.out.println("changeX " + newx + " changeY " + newy);
		directionVector.x += newx;
		directionVector.z += newy;
		System.out.println("veloX " + directionVector.x + " veloY " + directionVector.y );
		//directionVector.scl(0.1f);
		}
		}
	
	public float fy(float x, float y, float velocityX, float velocityY) {
		float gravity = -g * mass * physics.getPartialDerivativeY(y);
		//System.out.println("gravity : " + gravity);
		float a = friction* g * velocityY;
		float b = (float) Math.sqrt((velocityX * velocityX) + (velocityY * velocityY));
		if ((-gravity - (a/b))> 0) System.out.println("SOMETHING WENT WRONG - y");
		return (-gravity - (a / b));
	}

	public float fx(float x, float y, float velocityX, float velocityY) {
		float gravity = -g * mass * physics.getPartialDerivativeX(x);
		// System.out.println("gravity : " + gravity);
		float a = friction * g * velocityX; //velocity switches signs every iteration, fuck knows why
		float b = (float) Math.sqrt((velocityX * velocityX) + (velocityY * velocityY));
		//System.out.println( " fx " + (-gravity - (a/b)));
		if ((-gravity - (a/b))> 0) System.out.println("SOMETHING WENT WRONG - x");
		return (-gravity - (a / b));
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
