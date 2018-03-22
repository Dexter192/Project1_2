package settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import menu.AbstractScreen;

public class SettingsScreen extends AbstractScreen {

    private Texture txtrBg;
    
	public SettingsScreen() {
		super();
		txtrBg    = new Texture(Gdx.files.internal("img/main_menu_bg.png"));
		SettingsSkin settingsSkin = new SettingsSkin();
	}
    
	@Override
	public void buildStage() {		
		Image bg = new Image(txtrBg);
		addActor(bg);
		
		TextField velocity = new TextField("Test", new Skin());
		addActor(velocity);
	}
}
