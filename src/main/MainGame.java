package main;

import com.badlogic.gdx.Game;

import menu.ScreenEnum;
import menu.ScreenManager;

public class MainGame extends Game {

	public MainGame() {
	}

	@Override
	public void create() {
		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen( ScreenEnum.MAIN_MENU );	
	}
}
