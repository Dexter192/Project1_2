package gameEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.InputProcessor;
import menu.AbstractScreen;


public class GameScreen extends AbstractScreen {

	//private Rectangle board;
	private OrthographicCamera camera;

	private float g = (float) 9.81;
	private SpriteBatch batch;
	private Golfball ball;
	private Board board;
	private Hole hole;
	private float velocityX = 0;
	private float velocityY = 0;

	/**
	 * Implement the game initialization here. That should be stuff like the 
	 * course builder, the menu screen or and maybe the physics engine 
	 */
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 1000);
		batch = new SpriteBatch();
		ball = new Golfball();
		board = new Board();
		hole = new Hole();		
	}

	/**
	 * This method still updates the game. So every drawing, collision detection 
	 * etc. should be called here (This does not mean, that everything should be checked here :D )
	 * Create a collision detector class for example and just call that one with the required parameters
	 */
	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();	
		batch.draw(board.getSprite(), board.getRectangle().x, board.getRectangle().y);
		batch.draw(hole.getSprite(), hole.getCircle().x, hole.getCircle().y);
		if(!ball.getCircle().overlaps(hole.getCircle())) {
			batch.draw(ball.getSprite(), ball.getCircle().x, ball.getCircle().y);
		}
		if(ball.getCircle().x < 200) {
			velocityX = velocityX * (-1);
			ball.getCircle().x = 200;
		}
		if(ball.getCircle().x > 732) {
			velocityX = velocityX * (-1);
			ball.getCircle().x = 732;
		}
		if(ball.getCircle().y < 32) {
			velocityY = velocityY * (-1);
			ball.getCircle().y = 32;
		}
		if(ball.getCircle().y > 964) {
			velocityY = velocityY * (-1);
			ball.getCircle().y = 964;
		}
		
		if(Gdx.input.isKeyPressed(Keys.LEFT))
        	velocityX = 50;
        if(Gdx.input.isKeyPressed(Keys.RIGHT))
        	velocityX = -50;
        if(Gdx.input.isKeyPressed(Keys.UP))
        	velocityY = -50;
        if(Gdx.input.isKeyPressed(Keys.DOWN))
        	velocityY = 50;
        
      if(velocityX != 0 || velocityY != 0) {
    	if(velocityX != 0)
    	  velocityX +=  Gdx.graphics.getDeltaTime() * (fx(ball.getCircle().x, ball.getCircle().y, velocityX, velocityY)/ball.getMass());
    	if(velocityY != 0) 
        velocityY += Gdx.graphics.getDeltaTime() * (fy(ball.getCircle().x, ball.getCircle().y, velocityX, velocityY)/ball.getMass());
       //System.out.println(Gdx.graphics.getDeltaTime() * (fx(ball.getCircle().x, ball.getCircle().y, velocityX, velocityY)/ball.getMass()));
        if(velocityX < 0.1 && velocityX >-0.1) velocityX = 0;
        if(velocityY < 0.1 && velocityY > -0.1) velocityY = 0;
    	ball.getCircle().y -= velocityY * Gdx.graphics.getDeltaTime();
        ball.getCircle().x -= velocityX * Gdx.graphics.getDeltaTime();
//        System.out.println("velocityX " + velocityX + " velocityY " + velocityY);
      }
        batch.end();
	}
	public void dispose() {

		batch.dispose();

	}

	public double fy (float x, float y, float velocityX, float velocityY) {
		return  -(board.getFriction() * ball.getMass() * g * (velocityX + velocityY) / (Math.sqrt((velocityX*velocityX)+ (velocityY * velocityY))));
	}
	public double fx (float x, float y, float velocityX, float velocityY) {
		//assuming only friction plays a role at first 
		double a = board.getFriction() * ball.getMass() * g * (velocityX + velocityY);
		double b = Math.sqrt((velocityX*velocityX)+ (velocityY * velocityY));
		//System.out.println( " a " + a + " b " + b);
		return -(a/b);
	}
	public void setVelocities(float clickPositionX, float clickPositionY) {
	    velocityX = ball.getCircle().x + ball.getCircle().radius - clickPositionX;
	    velocityY = ball.getCircle().y + ball.getCircle().radius - clickPositionY;		
	}
	
	@Override
	public void buildStage() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 1000);
		batch = new SpriteBatch();
		ball = new Golfball();
		board = new Board();
		hole = new Hole();
		Gdx.input.setInputProcessor(new InputListener(this, camera));
	}
	
	public Vector3 getVelocity() {
		return new Vector3(velocityX, velocityY, 0);
	}

	@Override public void show() {}
	@Override public void resize(int width, int height) {}
	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void hide() {}	
}
