package menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

/**
 * This abstract class overtakes basic screen operations for you for example
 * clearing the screen before rendering or setting input processors
 * 
 * 
 * @author Daniel
 */
public abstract class AbstractScreen extends Stage implements Screen {

	/**
	 * Protected Constructor, such that it can be only called by extending classes
	 */
	protected AbstractScreen() {
		super(new StretchViewport(320.0f, 240.0f, new OrthographicCamera()));
	}

	/**
	 * Override this method to set a screen setting
	 */
	public abstract void buildStage();

	/**
	 * Renders the screen I.e. does what the render method did before in the Game
	 * instance
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.act(delta);
		super.draw();
	}

	/**
	 * sets this screen as the inputprocessing one
	 */
	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}

	/*
	 * ########################################################################## #
	 * # # The part below is not needed to be implemented. If you need it # #
	 * override it in the inheriting class if you want to use it # # #
	 * ##########################################################################
	 */

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}