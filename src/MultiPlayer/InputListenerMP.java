package MultiPlayer;


import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

import gameEngine3D.Golfball;
import physics.DifferentialEquationSolver;
import physics.VectorComputation;

public class InputListenerMP implements InputProcessor {

	GameScreenMultiPlayer gameScreenMultiPlayer;
	ArrayList<Golfball> golfballs;
	Golfball[] players;
	boolean initialize = true;
	public InputListenerMP(GameScreenMultiPlayer gameScreenMultiPlayer, ArrayList<Golfball> golfballs) {
		this.gameScreenMultiPlayer = gameScreenMultiPlayer;
		this.golfballs = golfballs;
		
	
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Vector3 mousePosition = gameScreenMultiPlayer.getWorldCoords();
		DifferentialEquationSolver ode = gameScreenMultiPlayer.getDifferentialEquationSolver();
			Golfball g = gameScreenMultiPlayer.getCurrentPlayer();
			Vector3 directionVector = g.getPosition().sub(mousePosition);
			//float strength = VectorComputation.getInstance().getDistance(mousePosition, g.getPosition())/100;
			float strength = gameScreenMultiPlayer.getStrength();
					
			System.out.println("STRENGTH " + strength);
			directionVector.nor();
			
			if (!initialize && g.getVelocity().isZero()) {
				directionVector.y = 0;
				g.setInitialPosition(g.getPosition());
				g.setVelocity(directionVector.scl(strength));
				g.incrementScore();
			
			}
			initialize = false;
		
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}


