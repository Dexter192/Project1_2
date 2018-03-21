package menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Align;

public class MainMenuScreen extends AbstractScreen {
	
	private Texture txtrBg;
	private Texture txtrPlay;
	private Texture txtrExit;
	private Texture txtrBuild;
	
	
	public MainMenuScreen() {
		super();
		txtrBg    = new Texture(Gdx.files.internal("img/main_menu_bg.png"));
		txtrPlay  = new Texture(Gdx.files.internal("img/btn_play.png"));
		txtrExit  = new Texture(Gdx.files.internal("img/btn_exit.png"));
		txtrBuild = new Texture(Gdx.files.internal("img/btn_build.png"));
	}

	@Override
	public void buildStage() {
		
		// Adding actors
		Image bg = new Image(txtrBg);
		addActor(bg);
		
		ImageButton btnPlay = ButtonFactory.createButton(txtrPlay);
		btnPlay.setSize(40f, 20f);
		btnPlay.setPosition(getWidth() / 2, 160.f, Align.center);
		addActor(btnPlay);

		ImageButton btnPlay3D = ButtonFactory.createButton(txtrPlay);
		btnPlay3D.setSize(40f, 20f);
		btnPlay3D.setPosition(getWidth() / 2, 120.f, Align.center);
		addActor(btnPlay3D);
		
		ImageButton btnBuild = ButtonFactory.createButton(txtrBuild);
		btnBuild.setSize(40f, 20f);
		btnBuild.setPosition(getWidth() / 2, 80.f, Align.center);
		addActor(btnBuild);
		
		ImageButton btnExit = ButtonFactory.createButton(txtrExit);
		btnExit.setSize(40f, 20f);
		btnExit.setPosition(getWidth() / 2, 40.f, Align.center);
		addActor(btnExit);

		
		// Setting listeners
		btnPlay.addListener( ButtonFactory.createListener(ScreenEnum.GAME_SCREEN));
		
		btnPlay3D.addListener(ButtonFactory.createListener(ScreenEnum.GAME_SCREEN_3D));
		
		btnBuild.addListener(ButtonFactory.createListener(ScreenEnum.COURSE_BUILDER));
		
		btnExit.addListener(
				new InputListener() {
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
						Gdx.app.exit();
						return false;
					}
				});
	}

	@Override
	public void dispose() {
		super.dispose();
		txtrBg.dispose();
		txtrPlay.dispose();
		txtrExit.dispose();
	}
}