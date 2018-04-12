package gameEngine3D;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import collisionDetector.CollisionDetector;
import menu.AbstractScreen;

public class GameScreen3D extends AbstractScreen {

	private PerspectiveCamera camera;

	private Golfball golfball;
	private ModelBatch modelBatch;
	private Environment environment;
	private CameraInputController camController;
	private CollisionDetector collisionDetector;
	private InputMultiplexer inputMultiplexer;
	public LineIndicator indicatorLine;

	private Set<Obstacle> obstacleList = new HashSet<Obstacle>();

	@Override
	public void buildStage() {
		// initialize environment
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		modelBatch = new ModelBatch();
		collisionDetector = new CollisionDetector();

		// initialize camera
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, 10f, 10f);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
		camController = new CameraInputController(camera);

		// initialize obstacles
		for (int i = 0; i < 100; i += 10) {
			for (int j = 0; j < 100; j += 10) {
				Obstacle box = new ObstacleBox(i, i / 10, j, 10f, 1f, 10f);
				obstacleList.add(box);
			}
		}
		// initialize golfball
		golfball = new Golfball(1);
		// inizialize hit indicator line
		indicatorLine = new LineIndicator();

		// initialize input
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new InputListener(this, camera));
		inputMultiplexer.addProcessor(camController);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	/**
	 * This method updates the game. So every drawing, collision detection etc.
	 * should be called here
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camController.update();
		modelBatch.begin(camera);
		modelBatch.render(golfball.getBallInstance());
		modelBatch.render(indicatorLine.getInstance());

		for (Obstacle o : obstacleList) {
			modelBatch.render(o.getInstance());
		}
		// First update, than draw or the other way around?
		golfball.update();
		updateCameraPosition();
		modelBatch.end();
		Vector3 mousePosition = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

		indicatorLine.updateLine(golfball.getPosition(), mousePosition);

		// System.out.println(mousePosition);
		// System.out.println(indicatorLine.getInstance().transform.getTranslation(new
		// Vector3()));

	}

	public void dispose() {
		modelBatch.dispose();
		golfball.getBallModel().dispose();
	}

	public Golfball getGolfball() {
		return golfball;
	}

	public void updateCameraPosition() {
		camera.translate(golfball.getVelocity());
		camera.update();
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

}
