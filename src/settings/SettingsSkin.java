package settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SettingsSkin {

	private Skin skin;

	public SettingsSkin() {
		TextureAtlas textureAtlas = new TextureAtlas();
		skin = new Skin(Gdx.files.internal("arcade/arcade-ui.json"));
		skin.addRegions(textureAtlas);
	}

}
