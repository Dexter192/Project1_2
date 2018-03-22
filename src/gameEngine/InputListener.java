package gameEngine;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class InputListener implements InputProcessor{
	
	private GameScreen gameScreen;
	private OrthographicCamera camera;
	private boolean initialize = true;
	
	public InputListener(GameScreen gameScreen, OrthographicCamera camera) {
		this.gameScreen = gameScreen;
		this.camera = camera;
	}
	
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 clickPosition = camera.unproject(new Vector3(screenX, screenY, 0));
		if(!initialize && gameScreen.getVelocity().isZero())gameScreen.setVelocities(clickPosition.x, clickPosition.y);
		initialize = false;
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) { return false;}
	
	@Override
	public boolean keyUp(int keycode) {return false;}
	
	@Override
	public boolean keyTyped(char character) {return false;}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	
	@Override
	public boolean scrolled(int amount) {return false;}  
}
