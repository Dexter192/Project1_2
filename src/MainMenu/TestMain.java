package MainMenu;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import GameEngine.Golf;


public class TestMain {

	public static void main(String[] args) {
		/* I have not really worked with Libgdx before but I think this is a good way
		 * to construct a game. There may be (several) different approaches.
		 * I will have more time to concentrate on the project after Monday but I had to
		 * work on two assignments over the weekend. Prehaps you guys can already figure 
		 * out how to layout a grass ground or something like that.
	   	 */
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Golf - Group 10";
		cfg.width = 1920;
		cfg.height = 1080;
		cfg.useGL30 = true;
		cfg.resizable = false;
		new LwjglApplication(new Golf(), cfg);
	}
}