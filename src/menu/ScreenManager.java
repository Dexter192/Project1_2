package menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * Singleton instance of the screen manager. It is used to switch the screen.
 * 
 * @author Daniel
 *
 */
public class ScreenManager {
	
	private static ScreenManager instance;
	
	// Reference to game
	private Game game;
	
	// Singleton: private constructor
	private ScreenManager() {
		super();
	}
	
	/**
	 * Retrieve the singleton instance of the ScreenManager
	 * @return instance The instance of the Screenmanager
	 */
	public static ScreenManager getInstance() {
		if (instance == null) {
			instance = new ScreenManager();
		}
		return instance;
	}
	
	/**
	 * Initialize the Screenmanager with the game in which the 
	 * screens should be displayed
	 * @param game the game where the screen should be displayed
	 */
	public void initialize(Game game) {
		this.game = game;
	}
	
	/**
	 * Displays the Screen in the game and executes the buildStage method.
	 * @param screenEnum the screen which should be displayed
	 */
	public void showScreen(ScreenEnum screenEnum) {
		
		// Get current screen to dispose it
		Screen currentScreen = game.getScreen();
		
		// Show new screen
		AbstractScreen newScreen = screenEnum.getScreen();
		newScreen.buildStage();
		game.setScreen(newScreen);
		
		// Dispose previous screen
		if (currentScreen != null) {
			currentScreen.dispose();
		}
	}
}