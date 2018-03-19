import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Golf extends Game{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Golfball ball;
	
	/**
	 * Implement the game initialization here. That should be stuff like the 
	 * course builder, the menu screen or and maybe the physics engine 
	 */
	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1920, 1080);
		batch = new SpriteBatch();
		ball = new Golfball();
	}

	/**
	 * This method updates the game. So every drawing, collision detection 
	 * etc. should be called here
	 */
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(ball.getSprite(), ball.getRectangle().x, ball.getRectangle().y);
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) ball.getRectangle().x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ball.getRectangle().x += 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) ball.getRectangle().y += 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) ball.getRectangle().y -= 200 * Gdx.graphics.getDeltaTime();
		   
		batch.end();
	}
}
