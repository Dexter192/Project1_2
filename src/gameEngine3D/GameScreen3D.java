package gameEngine3D;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

import collisionDetector.CollisionDetector;
import menu.AbstractScreen;

public class GameScreen3D extends AbstractScreen{

	private PerspectiveCamera camera;

	private Board board;
	private Golfball golfball;
	private ModelBatch modelBatch;
    private Environment environment;
    private CameraInputController camController;
    private CollisionDetector collisionDetector;

    private Set<Obstacle> obstacleList = new HashSet<Obstacle>();
    
    @Override
	public void buildStage() {
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		
		modelBatch = new ModelBatch();
		collisionDetector = new CollisionDetector();
		
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, 10f, 10f);
		camera.lookAt(0,0,0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
        camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);
        
        board = new Board(50f, 0.2f, 20f);
        
        for(int i = 2; i < 10; i++) {
            Obstacle box = new ObstacleBox(i,0,-i,1f,1f,1f);
            obstacleList.add(box);
        }
        
        
        golfball = new Golfball(1);	
	}
    
	/**
	 * This method updates the game. So every drawing, collision detection 
	 * etc. should be called here
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camController.update();
        modelBatch.begin(camera);
        modelBatch.render(board.getBoardInstance());
        modelBatch.render(golfball.getBallInstance());

        for(Obstacle o : obstacleList) {
        	modelBatch.render(o.getInstance());
        	if(collisionDetector.detectCollision(golfball, o)) {
        		golfball.bounceOff();
        	}
        }
        
        golfball.update();
        
        modelBatch.end();
	}
	
	public void dispose() {

		modelBatch.dispose();
		golfball.getBallModel().dispose();
		board.getBoardModel().dispose();
		
	}
	
	@Override public void show() {}
	@Override public void resize(int width, int height) {}
	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void hide() {}	

}
