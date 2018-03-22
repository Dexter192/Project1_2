package menu;

import java.io.IOException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * A factory method for creating Image-buttons and assign them to a screen
 * @author Daniel
 *
 */
public class ButtonFactory {
	
	/**
	 * Create an Imagebutton 
	 * @param texture the texture of the Button
	 * @return returns the Image-Button
	 */
	public static ImageButton createButton(Texture texture) {
		return new ImageButton(new TextureRegionDrawable(new TextureRegion(texture) ) );
	}
	
	/**
	 * The method creates an input listener for the Image-button and links it to the screen
	 * @param screen the Screen on which the button is displayed
	 * @return returns the inputlistener for the created button
	 */
	public static InputListener createListener(final ScreenEnum screen) {
		return new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				try {
					ScreenManager.getInstance().showScreen(screen);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		};
	}
}