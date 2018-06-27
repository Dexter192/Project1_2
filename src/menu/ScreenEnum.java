package menu;

import MultiPlayer.GameScreenMultiPlayer;
import gameEngine3D.GameScreen3D;

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

	GAME_SCREEN {
		public AbstractScreen getScreen() {
			 return new GameScreenMultiPlayer();
		}
	};

	public abstract AbstractScreen getScreen();
}