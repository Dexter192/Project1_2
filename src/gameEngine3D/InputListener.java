package gameEngine3D;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;


public class InputListener implements InputProcessor{
	
	GameScreen3D gameScreen3D;
	PerspectiveCamera camera;
	boolean initialize = true;
	
	public InputListener(GameScreen3D gameScreen3D, PerspectiveCamera camera) {
		this.gameScreen3D = gameScreen3D;
		this.camera = camera;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 clickPosition = camera.unproject(new Vector3(screenX, screenY, 0));
		gameEngine3D.Golfball ball = gameScreen3D.getGolfball();
		System.out.println(camera);
		if(!initialize && ball.getVelocity().isZero()) {
			ball.setVelocity(clickPosition);
		}
		initialize = false;
		return false;
	}
	
	@Override
	public boolean keyDown(int keycode) {return false;}

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
