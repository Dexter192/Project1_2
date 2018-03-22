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
		collisionWithWalls();
		if(Gdx.input.isKeyPressed(Keys.LEFT))
        	velocityX = 50;
        if(Gdx.input.isKeyPressed(Keys.RIGHT))
        	velocityX = -50;
        if(Gdx.input.isKeyPressed(Keys.UP))
        	velocityY = -50;
        if(Gdx.input.isKeyPressed(Keys.DOWN))
        	velocityY = 50;
        
      if(velocityX != 0 || velocityY != 0) {
    	  changeVelocity();
    	  moveBall();
      }
        batch.end();
	}
	public void moveBall() {
		  ball.getCircle().y -= velocityY * Gdx.graphics.getDeltaTime();
          ball.getCircle().x -= velocityX * Gdx.graphics.getDeltaTime();
	}
	public void changeVelocity() {
    	if(velocityX != 0)
      	  velocityX +=  Gdx.graphics.getDeltaTime() * (fx(ball.getCircle().x, ball.getCircle().y, velocityX, velocityY)/ball.getMass());
      	if(velocityY != 0) 
          velocityY += Gdx.graphics.getDeltaTime() * (fy(ball.getCircle().x, ball.getCircle().y, velocityX, velocityY)/ball.getMass());
          if(velocityX < 0.1 && velocityX >-0.1) velocityX = 0;
          if(velocityY < 0.1 && velocityY > -0.1) velocityY = 0;

	}
	public void dispose() {
		batch.dispose();
	}
	public void collisionWithWalls() {
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
		
	}
	public double fy (float x, float y, float velocityX, float velocityY) {
		double gravity = -9.81 * ball.getMass() * board.getHeight(y,x) ;
		System.out.println("gravity : " + gravity);
		double a = board.getFriction() * ball.getMass() * g * (velocityY + velocityX);
		double b = Math.sqrt((velocityX*velocityX)+ (velocityY * velocityY));
		//System.out.println( " fx " + (gravity - (a/b)));
		return (gravity - (a/b)) ;
	}
	public double fx (float x, float y, float velocityX, float velocityY) {
		//assuming only friction plays a role at first 
		double gravity = -g * ball.getMass() * board.getHeight(x,y) ;
		System.out.println("gravity : " + gravity);
		double a = board.getFriction() * ball.getMass() * g * (velocityX + velocityY);
		double b = Math.sqrt((velocityX*velocityX)+ (velocityY * velocityY));
		System.out.println( " fx " + (gravity - (a/b)));
		return (gravity - (a/b)) ;
	}
	public void setVelocities(float x, float y) {
		   // double distance = Math.sqrt(Math.pow((x - ball.getCircle().x), 2) + Math.pow(y - ball.getCircle().y, 2));
		    System.out.println("x " + x + " y " + y);
		    System.out.println("ball : x " + ball.getCircle().x + " y " + ball.getCircle().y);
		    velocityX = (0.1f) *(ball.getCircle().x-x);
		    velocityY = (0.1f)*(ball.getCircle().y -y) ;
		    System.out.println("VelocityX set to " + velocityX + " velocity y " + velocityY);

		}
	@Override
	public void buildStage() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 1000);
		batch = new SpriteBatch();
		ball = new Golfball();
		board = new Board();
		hole = new Hole();
		  Gdx.input.setInputProcessor(new InputProcessor() {
	            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
	        		Vector3 clickPosition = camera.unproject(new Vector3(screenX, screenY, 0));
	            	setVelocities(clickPosition.x, clickPosition.y);
	        		return false;
	        	}

				@Override
				public boolean keyDown(int keycode) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean keyUp(int keycode) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean keyTyped(char character) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean touchDown(int screenX, int screenY, int pointer, int button) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean touchDragged(int screenX, int screenY, int pointer) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean mouseMoved(int screenX, int screenY) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean scrolled(int amount) {
					// TODO Auto-generated method stub
					return false;
				}
	            });  
	}

	@Override public void show() {}
	@Override public void resize(int width, int height) {}
	@Override public void pause() {}
	@Override public void resume() {}
	@Override public void hide() {}	
}
