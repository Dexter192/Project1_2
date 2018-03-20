package menu;

import gameEngine.GameScreen;

public enum ScreenEnum {	
	
	MAIN_MENU {
		public AbstractScreen getScreen() {
			return new MainMenuScreen();
		}
	},
	
	GAME_SCREEN{
		public AbstractScreen getScreen() {
			return new GameScreen();
		}
	};
		
	public abstract AbstractScreen getScreen();
}