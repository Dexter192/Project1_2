import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Golf extends Game{

	//private Rectangle board;
	private OrthographicCamera camera;

	private int score = 0;
	private final double g = 9.81;
	private SpriteBatch batch;
	private Golfball ball;
	private Board board;
	private Hole hole;
	private double velocityX = 0;
	private double velocityY = 0;
	private double accelerationX = 1;
	private double accelerationY = 1;
	/**
	 * Implement the game initialization here. That should be stuff like the 
	 * course builder, the menu screen or and maybe the physics engine 
	 */
	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 1000);
		batch = new SpriteBatch();
		ball = new Golfball();
		board = new Board();
		hole = new Hole();

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
		batch.draw(hole.getSprite(), hole.getCircle().x, hole.getCircle().y);
		if(!ball.getCircle().overlaps(hole.getCircle()))
			batch.draw(ball.getSprite(), ball.getCircle().x, ball.getCircle().y);
		
        if(Gdx.input.isKeyPressed(Keys.LEFT))
        	velocityX = -50;
        if(Gdx.input.isKeyPressed(Keys.RIGHT))
        	velocityX = 50;
        if(Gdx.input.isKeyPressed(Keys.UP))
        	velocityY = 50;
        if(Gdx.input.isKeyPressed(Keys.DOWN))
        	velocityY = -50;
        
		if(ball.getCircle().x < (100+96)) {
			velocityX = velocityX* (-1);
			ball.getCircle().x = 196;
		}
		if(ball.getCircle().x > (800-64)) {
			velocityX = velocityX * (-1);
			ball.getCircle().x = 736;
		}
		if (ball.getCircle().y < (0+32)) {
			velocityY = velocityY * (-1);
			ball.getCircle().y = 32;
		}
		if(ball.getCircle().y > (1000-64)) {
			velocityY = velocityY * (-1);
			ball.getCircle().y = 936;
		}
        batch.end();

        ball.getCircle().x += velocityX * Gdx.graphics.getDeltaTime();
        ball.getCircle().y += velocityY * Gdx.graphics.getDeltaTime(); 
        
        //velocityX += (Gdx.graphics.getDeltaTime()* ((-ball.getMass()*9.81*board.getFrictionConstant()*velocityX)/(Math.sqrt((velocityX * velocityX) + (velocityY * velocityY)))/ball.getMass()));
        //velocityY += (Gdx.graphics.getDeltaTime()* ((-ball.getMass()*9.81*board.getFrictionConstant()*velocityY)/(Math.sqrt((velocityX * velocityX) + (velocityY * velocityY)))/ball.getMass()));
        //velocityX = velocityX *(accelerationX * ball.getMass());
        //velocityY = velocityY * ( accelerationY * ball.getMass());
		/*acceleration = force / ball.getMass();
		force = gravity + friction;
		friction = - (board.getFrictionConstant() *ball.getMass() * g)*/
		//accelerationX = (board.getFrictionConstant() * 9.81 * ball.getMass()) / );
		//accelerationY = (board.getFrictionConstant() * 9.81 * ball.getMass()) / Math.sqrt((velocityX * velocityX) + (velocityX * velocityY));
		//velocityX += 0.1 * Gdx.graphics.getDeltaTime();
		//velocityY += 0.1 * Gdx.graphics.getDeltaTime();
		
        System.out.println("x " + ball.getCircle().x + "y " + ball.getCircle().y);
        System.out.println("VX " + velocityX + "VY " + velocityY);
	}
	public float movementX() {
		return 0;
	}
	public void dispose() {
		batch.dispose();
	}

}
