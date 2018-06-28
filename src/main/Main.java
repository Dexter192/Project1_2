package main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;


public class Main {

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Golf - Group 10";
		cfg.width = 800;
		cfg.height = 600;
		cfg.useGL30 = true;
		cfg.resizable = false;
		new LwjglApplication(new MainGame(), cfg);
	}
}