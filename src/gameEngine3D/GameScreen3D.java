package gameEngine3D;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;

import menu.AbstractScreen;

public class GameScreen3D extends AbstractScreen{

	private PerspectiveCamera camera;

	private Board board;
	private Golfball golfball;
	
	private ModelBatch modelBatch;
    private Environment environment;
    private CameraInputController camController;
    
	/**
	 * Implement the game initialization here. That should be stuff like the 
	 * course builder, the menu screen or and maybe the physics engine 
	 */
	public void create() {
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		
		modelBatch = new ModelBatch();
		
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, 10f, 10f);
		camera.lookAt(0,0,0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
        camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);
        
        board = new Board(10f, 1f, 20f);
        
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

        golfball.update();
        	
        modelBatch.end();
	}
	
	public void dispose() {

		modelBatch.dispose();
		golfball.getBallModel().dispose();
		board.getBoardModel().dispose();
		
	}

	@Override
	public void buildStage() {
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
		
		
		modelBatch = new ModelBatch();
		
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(10f, 10f, 10f);
		camera.lookAt(0,0,0);
		camera.near = 1f;
		camera.far = 300f;
		camera.update();
        camController = new CameraInputController(camera);
        Gdx.input.setInputProcessor(camController);
        
        board = new Board(10f, 1f, 20f);
        
        golfball = new Golfball(1);	
	}
	
	@Override public void show() {}
	@Override public void resize(int width, int height) {}
	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void hide() {}	

}
