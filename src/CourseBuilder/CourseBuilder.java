package CourseBuilder;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import Obstacles.Obstacle;
import menu.AbstractScreen;

public class CourseBuilder extends AbstractScreen {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Set<Obstacle> obstacleList = new HashSet<Obstacle>();

	@Override
	public void buildStage() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1000, 1000);
		batch = new SpriteBatch();

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.end();
	}

}
