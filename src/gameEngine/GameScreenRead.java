package gameEngine;
import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.FileInputStream;

import menu.AbstractScreen;



public class GameScreenRead extends AbstractScreen {
	private int count = 0;
	//private Rectangle board;
	private OrthographicCamera camera;
	private float g = (float) 9.81;
	private SpriteBatch batch;
	private Golfball ball;
	private Board board;
	private Hole hole;
	private float velocityX = 0;
	private float velocityY = 0;
	private int[][] velocities = new int[100][2];
	private int max;

	
	/**
	 * This method still updates the game. So every drawing, collision detection 
	 * etc. should be called here (This does not mean, that everything should be checked here :D )
	 * Create a collision detector class for example and just call that one with the required parameters..
	 */  
	@Override
	public void render(float delta)  {
		if(velocityX == 0 && velocityY == 0) {
			if(count < max) {
			velocityX = velocities[count][0];
			velocityY = velocities[count][1];
			count ++;
//			System.out.println("HHHHIIIII");
			}
			else {
				count = 0; 
				velocityX = velocities[count][0];
				velocityY = velocities[count][1];
			}
		}
		Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for(Terrain t : board.getTerrain()) {

			batch.draw(t.getSprite(), t.getPosition().x, t.getPosition().y);
		}
	
		batch.draw(hole.getSprite(), hole.getCircle().x, hole.getCircle().y);
		if(!ball.getCircle().overlaps(hole.getCircle())) {
			batch.draw(ball.getSprite(), ball.getCircle().x, ball.getCircle().y);
		}

		else {
			velocityX = 0;
			velocityY = 0;
		}
		Vector3 ballPos = camera.unproject(new Vector3(ball.getCircle().x, ball.getCircle().y, 0));
		if(board.getHeight(ballPos.x,ballPos.y ) < 0) {
			velocityX = 0; velocityY = 0;
		}
//		System.out.println(ballPos);

		collisionWithWalls();
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
        	if(velocityX > 0) velocityX += 10;
        	else velocityX -= 10;
			if(velocityY > 0) velocityY += 10;
			else velocityY -=10;
		}
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
//        System.out.println("Velocity x :" + velocityX + " Velocity Y :" + velocityY);
	}
	public void dispose() {
		batch.dispose();
	}
	public void collisionWithWalls() {

		if(ball.getCircle().x < 100) {
			velocityX = velocityX * (-1);
			ball.getCircle().x = 100;
		}
		if(ball.getCircle().x > 885) {
			velocityX = velocityX * (-1);
			ball.getCircle().x = 885 ;
		}
		if(ball.getCircle().y < 0) {
			velocityY = velocityY * (-1);
			ball.getCircle().y = 0;
		}
		if(ball.getCircle().y > 975) {
			velocityY = velocityY * (-1);
			ball.getCircle().y = 975;
		}
		
	}
	public double fy (float x, float y, float velocityX, float velocityY) {
		float gravity = -g * ball.getMass() * board.getPhysics().getPartialDerivativeY(y) ;
//		System.out.println("gravity : " + gravity);
		double a = 10 * g * velocityY ;
		double b = Math.sqrt((velocityX*velocityX)+ (velocityY * velocityY));
//		System.out.println( " fy " + (gravity - (a/b)));
		return (-gravity - (a/b)) ;
	}

	public double fx (float x, float y, float velocityX, float velocityY) {
		float gravity = -g * ball.getMass() * board.getPhysics().getPartialDerivativeX(x) ;
//		System.out.println("gravity : " + gravity);
		double a = 10 * g * velocityX ;
		double b = Math.sqrt((velocityX*velocityX)+ (velocityY * velocityY));
//		System.out.println( " fx " + (gravity - (a/b)));
		return (-gravity - (a/b)) ;
	}

	

/*
	public void setVelocities(float clickPositionX, float clickPositionY) {
	    velocityX = ball.getCircle().x + ball.getCircle().radius - clickPositionX;
	    velocityY = ball.getCircle().y + ball.getCircle().radius - clickPositionY;		
	}s

	*/

	@Override
	public void buildStage() {
//		System.out.println("hi");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 1000);
		batch = new SpriteBatch();
		ball = new Golfball();
		board = new Board();
		hole = new Hole();
		try{
			
			FileReader reader = new FileReader("Velocity.txt");
//			System.out.println("still here");
			Scanner in = new Scanner(reader);
		int i = 0; 
		
		while(in.hasNextLine()){
			String inputLine = in.nextLine();
			String[] splitted = inputLine.split(" ");
			velocities[i][0] = Integer.parseInt(splitted[0]);
			velocities[i][1] =Integer.parseInt(splitted[1]);
			i++;
		}
		max = i;
		in.close();
		}
		catch(Exception e) {
//		System.out.println("o");
		}
		for(int i = 0; i < max; i++) {
//			System.out.println("v" + velocities[i][0] +  " " + velocities[i][1]);
		}
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

