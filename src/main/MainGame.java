package main;

import java.io.IOException;

import com.badlogic.gdx.Game;

import menu.ScreenEnum;
import menu.ScreenManager;

public class MainGame extends Game {

	public MainGame() {
	}

	@Override
	public void create(){
		ScreenManager.getInstance().initialize(this);
		try {
			ScreenManager.getInstance().showScreen( ScreenEnum.MAIN_MENU );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
