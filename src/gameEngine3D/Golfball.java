package gameEngine3D;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

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
	private Vector3 directionVector;

	public Golfball(float radius) {
		directionVector = new Vector3(0, 0, 0);

		modelBuilder = new ModelBuilder();

		ballModel = modelBuilder.createSphere(radius * 2, radius * 2, radius * 2, 50, 50, new Material(),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
						| VertexAttributes.Usage.TextureCoordinates);

		ballInstance = new ModelInstance(ballModel);
		ballInstance.transform.translate(new Vector3(0, (float) (0.1 + radius), 0));

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
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			directionVector.x = 0.4f;
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			directionVector.x = -0.4f;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			directionVector.z = 0.4f;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			directionVector.z = -0.4f;
		}

		ignoreMinimalVelocity();

		// Transform the ballposition by the directionvector
		ballInstance.transform.translate(directionVector);
		directionVector.scl(0.9f);
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

	public void setVelocity(Vector3 clickPosition) {
		System.out.println(clickPosition);
		System.out.println(getPosition());
		this.directionVector = getPosition().sub(clickPosition);
		directionVector.scl(0.1f);
	}

	public Vector3 getPosition() {
		return ballInstance.transform.getTranslation(new Vector3());
	}

	/**
	 * TODO: Implement propper bouncing of
	 */
	public void bounceOff() {
		directionVector.scl(-1);
	}

}
