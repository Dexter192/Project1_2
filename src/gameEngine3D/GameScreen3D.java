package gameEngine3D;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import Obstacles.Hole;
import Obstacles.Obstacle;
import Obstacles.ObstacleBox;
import ai.AStar;
import collisionDetector.CollisionDetector;
import menu.AbstractScreen;

/**
 * To make the axis a bit more clear set showaxis to true. When doing that, the
 * x axis is shown in red, the y axis is shown in blue, the z axis is shown in
 * green
 * 
 * @author Daniel
 *
 */
public class GameScreen3D extends AbstractScreen {

	private PerspectiveCamera camera;
	private boolean showAxis = true;

	private Obstacle collisionBox;
	private Golfball golfball;
	private Hole hole;
	private ModelBatch modelBatch;
	private Environment environment;
	private CameraInputController camController;
	private CollisionDetector collisionDetector;
	private InputMultiplexer inputMultiplexer;
	public LineIndicator indicatorLine;
	public LineIndicator[] axis = new LineIndicator[3];
	public static BoundingBox courseDimensions;

	
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
		camera.position.set(10f, 15f, 10f);
		camera.lookAt(0, 0, 0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
		camController = new CameraInputController(camera);

		initObstacles();
		
		// initialize golfball
		golfball = new Golfball(1);
		
		hole = new Hole(-10, 0.01f, -10, golfball.getRadius()*2);
		obstacleList.add(hole);
		
		// inizialize hit indicator line
		indicatorLine = new LineIndicator();

		// initialize input
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(new InputListener(this, indicatorLine));
		inputMultiplexer.addProcessor(camController);
		Gdx.input.setInputProcessor(inputMultiplexer);

		if (showAxis) {
			axis[0] = new LineIndicator();
			axis[1] = new LineIndicator();
			axis[2] = new LineIndicator();
		}
		
		for(Obstacle o : obstacleList) {
			collisionDetector.detectCollision(golfball, o);
		}
		
		calculateCouseDimensions(obstacleList);

		AStar aStar = new AStar(this);
		aStar.findPathToHole();
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

		// Dis-/Enabling the axis. Makes axis a bit more clear
		if (showAxis) {
			modelBatch.render(axis[0].getInstance());
			modelBatch.render(axis[1].getInstance());
			modelBatch.render(axis[2].getInstance());
		}

		modelBatch.render(indicatorLine.getInstance());		
		
		modelBatch.render(hole.getInstance());
		
		if (showAxis) {
			axis[0].setLine(new Vector3(100, 0, 0), new Vector3(-100, 0, 0), Color.RED);
			axis[1].setLine(new Vector3(0, 100, 0), new Vector3(0, -100, 0), Color.BLUE);
			axis[2].setLine(new Vector3(0, 0, 100), new Vector3(0, 0, -100), Color.LIME);
		}

		// First update, than draw or the other way around?
		golfball.update();
		updateCameraPosition();
		modelBatch.end();

		Vector3 mousePosition = getWorldCoords();
		indicatorLine.updateLine(golfball.getPosition(), mousePosition);
	
		collisionBox.rotate(new Vector3(0,0,1), 1);
		
		for (Obstacle o : obstacleList) {
			modelBatch.render(o.getInstance());
			collisionDetector.detectCollision(golfball, o);
		}		
	}

	/**
	 * Get the golfball from the current game
	 * @return the golfball of the game
	 */
	public Golfball getGolfball() {
		return golfball;
	}

	/**
	 * Update the camera position according to the ball velocity
	 */
	public void updateCameraPosition() {
		// TODO: we might have to do this manually --> ask in project meeting, prehaps
		// ask pietro
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			camera.rotateAround(golfball.getPosition(), new Vector3(0, 1, 0), -2f);
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			camera.rotateAround(golfball.getPosition(), new Vector3(0, 1, 0), 2f);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			camera.translate(new Vector3(0,-1,0));
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			camera.translate(new Vector3(0,1,0));
		}
		
		camera.lookAt(golfball.getPosition());
		camera.translate(golfball.getVelocity());
		camera.update();
	}
	
	/**
	 * Initialize all ostacles with position and add them to the obstacle list
	 */
	private void initObstacles() {
		Obstacle box = new ObstacleBox(0, 0, 0, 100f, 1f, 100f);
		obstacleList.add(box);					
		
		collisionBox = new ObstacleBox(10, 0, 10, 10f, 10f, 10f);
		collisionBox.setColor(Color.BLUE);
		obstacleList.add(collisionBox);
	
	}

	/**
	 * Calculate the bounding box of the whole couse. 
	 * The resulting boundingbox will be used for the A*-path finding, to avoid searching areas out of the course
	 * 
	 * @param obstacleList The list of all obstacles in the course
	 */
	private void calculateCouseDimensions(Set<Obstacle> obstacleList) {
		courseDimensions = new BoundingBox();
		Vector3 min = golfball.getPosition();
		Vector3 max = golfball.getPosition();
		for(Obstacle o : obstacleList) {
			if(max.x < o.getBoundingBox().max.x) 
				max.x = o.getBoundingBox().max.x;
			if(max.y < o.getBoundingBox().max.y) 
				max.y = o.getBoundingBox().max.y;
			if(max.z < o.getBoundingBox().max.z) 
				max.z = o.getBoundingBox().max.z;
			if(min.x > o.getBoundingBox().min.x) 
				min.x = o.getBoundingBox().min.x;
			if(min.y > o.getBoundingBox().min.y)
				min.y = o.getBoundingBox().min.y;
			if(min.z > o.getBoundingBox().min.z) 
				min.z = o.getBoundingBox().min.z;			
		}
		courseDimensions.set(min, max);
	}
	
	/**
	 * Retrieve the bounding box spanning over the hole course
	 * @return the boundingbox containing all elements in the course
	 */
	public BoundingBox getCouserDimensions() {
		if(courseDimensions == null) {
			calculateCouseDimensions(obstacleList);
		}
		return courseDimensions;
	}

	public Obstacle getHole() {
		return hole;
	}
	
	public Set<Obstacle> getAllObstacles() {
		return obstacleList;
	}
	
	public void dispose() {
		modelBatch.dispose();
		golfball.getBallModel().dispose();
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

	/**
	 * returns world coordinates, relative to the screen coordinates, where the
	 * mouse is currently at. It computes the intersection from the mouse location
	 * and a plane, spanned over the xz axis
	 * 
	 * @return
	 */
	public Vector3 getWorldCoords() {
		Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());

		Plane plane = new Plane();
		plane.set(0, 1, 0, 0);// the xz plane with direction z facing screen

		plane.d = 0;// ***** the depth in 3d for the coordinates

		Vector3 worldCoords = new Vector3();
		Intersector.intersectRayPlane(ray, plane, worldCoords);
		return worldCoords;
	}
}
