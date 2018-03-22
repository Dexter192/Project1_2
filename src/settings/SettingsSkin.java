package settings;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SettingsSkin{
	
	private Skin skin;
	
	public SettingsSkin() {
		TextureAtlas textureAtlas = new TextureAtlas();
		skin = new Skin();
		skin.addRegions(textureAtlas);
		TextureRegion hero = skin.get("arcade", TextureRegion.class);
	}
	
}
