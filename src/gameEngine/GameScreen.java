package gameEngine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import physics.Physics;
import menu.AbstractScreen;

public class GameScreen extends AbstractScreen {

	// private Rectangle board;
	private OrthographicCamera camera;
	private float g = (float) 9.81;
	private SpriteBatch batch;
	private Golfball ball;
	private Board board;
	private Hole hole;
	private float velocityX = 0;
	private float velocityY = 0;
	private int strokes;

	@Override
	public void buildStage() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 1000);
		batch = new SpriteBatch();
		ball = new Golfball();
		board = new Board();
		hole = new Hole();
		strokes = 0;

		Gdx.input.setInputProcessor(new InputListener(this, camera));
	}

	/**
	 * This method still updates the game. So every drawing, collision detection
	 * etc. should be called here (This does not mean, that everything should be
	 * checked here :D ) Create a collision detector class for example and just call
	 * that one with the required parameters..
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		checkIfOnWater();
		for (Terrain t : board.getTerrain()) {
			batch.draw(t.getSprite(), t.getPosition().x, t.getPosition().y);
		}

		batch.draw(hole.getSprite(), hole.getCircle().x, hole.getCircle().y);
		if (!ball.getCircle().overlaps(hole.getCircle())) {
			batch.draw(ball.getSprite(), ball.getCircle().x, ball.getCircle().y);
		} else {
			velocityX = 0;
			velocityY = 0;
		}
		Vector3 ballPos = camera.unproject(new Vector3(ball.getCircle().x, ball.getCircle().y, 0));
		if (board.getHeight(ballPos.x, ballPos.y) < 0) {
			velocityX = 0;
			velocityY = 0;
		}
		// System.out.println(ballPos);

		collisionWithWalls();
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			if (velocityX > 0)
				velocityX += 10;
			else
				velocityX -= 10;
			if (velocityY > 0)
				velocityY += 10;
			else
				velocityY -= 10;
		}

		if (velocityX != 0 || velocityY != 0) {
			changeVelocity();
			moveBall();
		}
		batch.end();
	}

	public void checkIfOnWater() {
		if (board
				.getTileOn(new Vector3(ball.getCircle().x + ball.getCircle().radius,
						ball.getCircle().y + ball.getCircle().radius, 0)) != null
				&& board.getTileOn(new Vector3(ball.getCircle().x + ball.getCircle().radius,
						ball.getCircle().y + ball.getCircle().radius, 0)).getPosition().z < 0) {
			ball.getCircle().x = ball.getPreviousPosition().x;
			ball.getCircle().y = ball.getPreviousPosition().y;
			velocityX = 0;
			velocityY = 0;
		}
	}

	public void moveBall() {
		ball.getCircle().y -= velocityY * Gdx.graphics.getDeltaTime();
		ball.getCircle().x -= velocityX * Gdx.graphics.getDeltaTime();
	}
// TODO: check if this is correct, since i believe it oscillates due to wrong method calls
	public void changeVelocity() {

		if (velocityX != 0)
			velocityX += Gdx.graphics.getDeltaTime()
					* (fx(ball.getCircle().x, ball.getCircle().y, velocityX, velocityY) / ball.getMass());
		if (velocityY != 0)
			velocityY += Gdx.graphics.getDeltaTime()
					* (fy(ball.getCircle().x, ball.getCircle().y, velocityX, velocityY) / ball.getMass());
		if (Math.abs(velocityX)< 0.1)velocityX = 0;
		if (Math.abs(velocityY)< 0.1)velocityY = 0;
	}

	public void dispose() {
		batch.dispose();
	}

	public void collisionWithWalls() {

		if (ball.getCircle().x < 100) {
			velocityX = velocityX * (-1);
			ball.getCircle().x = 100;
		}
		if (ball.getCircle().x > 885) {
			velocityX = velocityX * (-1);
			ball.getCircle().x = 885;
		}
		if (ball.getCircle().y < 0) {
			velocityY = velocityY * (-1);
			ball.getCircle().y = 0;
		}
		if (ball.getCircle().y > 975) {
			velocityY = velocityY * (-1);
			ball.getCircle().y = 975;
		}

	}

	public double fy(float x, float y, float velocityX, float velocityY) {
		float gravity = -g * ball.getMass() * board.getPhysics().getPartialDerivativeY(y);
		// System.out.println("gravity : " + gravity);
		double a = 10 * g * velocityY;
		double b = Math.sqrt((velocityX * velocityX) + (velocityY * velocityY));
		// System.out.println( " fx " + (gravity - (a/b)));
		return (-gravity - (a / b));
	}

	public double fx(float x, float y, float velocityX, float velocityY) {
		float gravity = -g * ball.getMass() * board.getPhysics().getPartialDerivativeX(x);
		// System.out.println("gravity : " + gravity);
		double a = 10 * g * velocityX;
		double b = Math.sqrt((velocityX * velocityX) + (velocityY * velocityY));
		// System.out.println( " fx " + (gravity - (a/b)));
		return (-gravity - (a / b));
	}

	public void setVelocities(float x, float y) {
		// double distance = Math.sqrt(Math.pow((x - ball.getCircle().x), 2) +
		// Math.pow(y - ball.getCircle().y, 2));
		// System.out.println("x " + x + " y " + y);
		// System.out.println("ball : x " + ball.getCircle().x + " y " +
		// ball.getCircle().y);
		velocityX = (ball.getCircle().x - x);
		velocityY = (ball.getCircle().y - y);
		// System.out.println("VelocityX set to " + velocityX + " velocity y " +
		// velocityY);
	}
	/*
	 * public void setVelocities(float clickPositionX, float clickPositionY) {
	 * velocityX = ball.getCircle().x + ball.getCircle().radius - clickPositionX;
	 * velocityY = ball.getCircle().y + ball.getCircle().radius - clickPositionY; }s
	 * 
	 */

	public Vector3 getVelocity() {
		return new Vector3(velocityX, velocityY, 0);
	}

	public Golfball getGolfball() {
		return ball;
	}

	public void hit() {
		strokes++;
		System.out.println("Strokes: " + strokes);
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
