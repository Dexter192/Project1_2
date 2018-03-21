package menu;

import CourseBuilder.CourseBuilder;
import CourseBuilder.ObstacleSelector;
import gameEngine.GameScreen;
import gameEngine3D.GameScreen3D;

public enum ScreenEnum {	
	
	MAIN_MENU {
		public AbstractScreen getScreen() {
			return new MainMenuScreen();
		}
	},
	
	GAME_SCREEN_3D{
		public AbstractScreen getScreen() {
			return new GameScreen3D();
		}
	},
	
	COURSE_BUILDER{
		public AbstractScreen getScreen() {
			return new CourseBuilder();
		}
	},
	
	GAME_SCREEN{
		public AbstractScreen getScreen() {
			return new GameScreen();
		}
	};
		
	public abstract AbstractScreen getScreen();
}