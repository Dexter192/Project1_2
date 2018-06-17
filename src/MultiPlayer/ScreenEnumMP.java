package MultiPlayer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.math.Vector3;

import CourseBuilder.CourseBuilder;
import MultiPlayer.GameOverScreen;
import MultiPlayer.GameScreenMultiPlayer;
import gameEngine.GameScreen;
import gameEngine3D.GameScreen3D;
import gameEngine3D.Golfball;
import menu.AbstractScreen;
import settings.SettingsScreen;

public enum ScreenEnumMP {

	GAMEOVERSCREEN{
		public AbstractScreen getScreen(ArrayList<Golfball> golfball, boolean teammode) {
			return new GameOverScreen(golfball,teammode);
		}
	};

	public abstract AbstractScreen getScreen(ArrayList<Golfball> golfball, boolean teammode);
}
