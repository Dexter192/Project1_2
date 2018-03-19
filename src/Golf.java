import org.lwjgl.opengl.GL30;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Golf extends Game{
	private Rectangle board;
	private OrthographicCamera camera;
	private ShapeRenderer shapes;
	private float posX;
	private float posY;
	private int score = 0;
	/**
	 * Implement the game initialization here. That should be stuff like the 
	 * course builder, the menu screen or and maybe the physics engine 
	 */
	@Override
	public void create() {
		posX = 250;
		posY = 5;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 500, 500);
		shapes = new ShapeRenderer();
		
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
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeType.Filled);
        shapes.setColor(Color.GREEN);
        shapes.rect(150,0,200,400);
        shapes.setColor(Color.BLACK);
        shapes.circle(250, 350, 5);
        shapes.setColor(Color.RED);
        shapes.circle( posX ,posY, 5);
        shapes.end();
        if(Gdx.input.isKeyPressed(Keys.LEFT))
            posX-=10;
        if(Gdx.input.isKeyPressed(Keys.RIGHT))
            posX+=10;
        if(Gdx.input.isKeyPressed(Keys.UP))
            posY+=10;
        if(Gdx.input.isKeyPressed(Keys.DOWN))
            posY-=10;
        
        if(posX > 245 && posX < 255 && posY > 345 && posY < 355) {
        	shapes.setColor(Color.BLACK);
        	shapes.circle(250, 350, 5);
        	score ++;
        	System.out.println(score);
        }
	}
	public void dispose() {
		shapes.dispose();
	}
}
