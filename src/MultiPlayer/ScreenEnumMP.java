package MultiPlayer;

import java.util.ArrayList;
import MultiPlayer.GameOverScreen;
import gameEngine3D.Golfball;
import menu.AbstractScreen;

public enum ScreenEnumMP {

	GAMEOVERSCREEN{
		public AbstractScreen getScreen(ArrayList<Golfball> golfball, boolean teammode) {
			return new GameOverScreen(golfball,teammode);
		}
	};

	public abstract AbstractScreen getScreen(ArrayList<Golfball> golfball, boolean teammode);
}
