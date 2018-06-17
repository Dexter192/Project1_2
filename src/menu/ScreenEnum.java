package menu;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.math.Vector3;

import CourseBuilder.CourseBuilder;
import MultiPlayer.GameOverScreen;
import MultiPlayer.GameScreenMultiPlayer;
import gameEngine.GameScreen;
import gameEngine3D.GameScreen3D;
import gameEngine3D.Golfball;
import settings.SettingsScreen;

public enum ScreenEnum {

	MAIN_MENU {
		public AbstractScreen getScreen() {
			return new MainMenuScreen();
		}
	},

	GAME_SCREEN_3D {
		public AbstractScreen getScreen() {
			return new GameScreen3D();
		}
	},

	COURSE_BUILDER {
		public AbstractScreen getScreen() {
			return new CourseBuilder();
		}
	},

	SETTINGS_SCREEN {
		public AbstractScreen getScreen() {
			return new SettingsScreen();
		}
	},

	GAME_SCREEN {
		public AbstractScreen getScreen() {
			 return new GameScreenMultiPlayer();
		}
	};

	public abstract AbstractScreen getScreen();
}