import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;


public class Golf extends Game{

	//private Rectangle board;
	private OrthographicCamera camera;
	private ShapeRenderer shapes;

	private int score = 0;

	private SpriteBatch batch;
	private Golfball ball;
	private Board board;
	/**
	 * Implement the game initialization here. That should be stuff like the 
	 * course builder, the menu screen or and maybe the physics engine 
	 */
	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 1000);
		shapes = new ShapeRenderer();
		batch = new SpriteBatch();
		ball = new Golfball();
		board = new Board();

	}

	/**
	 * This method updates the game. So every drawing, collision detection 
	 * etc. should be called here
	 */
	@Override
	public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();	
		batch.draw(board.getSprite(), board.getRectangle().x, board.getRectangle().y);
		batch.draw(ball.getSprite(), ball.getRectangle().x, ball.getRectangle().y);

		
      /*  shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeType.Filled);
        shapes.setColor(Color.GREEN);
        shapes.rect(150,0,200,400);
        shapes.setColor(Color.BLACK);
        shapes.circle(250, 350, 5);
        shapes.end();*/
        if(Gdx.input.isKeyPressed(Keys.LEFT))
        	ball.getRectangle().x -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Keys.RIGHT))
        	ball.getRectangle().x += 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Keys.UP))
        	ball.getRectangle().y += 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Keys.DOWN))
        	ball.getRectangle().y -= 200 * Gdx.graphics.getDeltaTime();
        batch.end();
	}
	public void dispose() {
		shapes.dispose();
		batch.dispose();

	}
}
