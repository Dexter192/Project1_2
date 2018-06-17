package MultiPlayer;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

import gameEngine3D.Golfball;
import menu.AbstractScreen;

public class GameOverScreen extends AbstractScreen {
	private Stage stage;
	private Skin skin;
	private ArrayList<Golfball> golfballs;
	private LabelStyle textStyle;
	private BitmapFont font;
	private ArrayList<Label> text;
	private ArrayList<Label> teamText;
	private SpriteBatch modelBatch;
	private Texture txtrBg;
	private Image bg;
	private boolean teammode;
	private int players ;
	private ArrayList<Golfball> teams;
	public GameOverScreen(ArrayList<Golfball> golfballs, boolean teammode) {
		this.players = golfballs.size();
		this.teammode= teammode;
		if(teammode) this.teams = makeTeamModeScores(golfballs);
		this.golfballs = bubbleSort(golfballs);
		txtrBg = new Texture(Gdx.files.internal("img/main_menu_bg.png"));
	}
	@Override
	public void buildStage() {
		// TODO Auto-generated method stub
		
		 bg = new Image(txtrBg);
		addActor(bg);
		modelBatch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
	     textStyle = new LabelStyle();
	     font = new BitmapFont();
	     textStyle.font = font;
	      
	     text = new ArrayList<>();
	     stage = new Stage();
	     for(int i = 0; i < golfballs.size(); i++) {
	    	 int x =golfballs.get(i).getIndex()+1;
	    	 String player = "Player " + x + " Score: " + golfballs.get(i).getScore();
	      text.add(new Label(player,textStyle));
	      text.get(i).setFontScale(2);
	      text.get(i).setX(Gdx.graphics.getWidth()/2 - 100f);
	      text.get(i).setY((Gdx.graphics.getHeight()-190)-(i*50));
	      stage.addActor(text.get(i));
	     }
	     
	     if(teammode) {
	    	 teamText = new ArrayList<>();
		     for(int i = 0; i < teams.size(); i++) {
		    	 int x = getTeamIndex(teams.get(i).getIndex());
		    	 String player = "Team " + x + " Score: " + teams.get(i).getScore();
		      teamText.add(new Label(player,textStyle));
		      teamText.get(i).setFontScale(2);
		      teamText.get(i).setX(Gdx.graphics.getWidth()/2 - 100f);
		      teamText.get(i).setY(((Gdx.graphics.getHeight()-190)-golfballs.size()*50)-(i*50));
		      stage.addActor(teamText.get(i));
		     }
	     }
	}
	
	public void render(float delta) {
		modelBatch.begin();
		draw();
		stage.draw();
		modelBatch.end();
	}
	
	public int compare(Golfball ball1, Golfball ball2) {
		int a = ball1.getScore();
		int b = ball2.getScore();
		if(a < b) return -1;
		else if (b < a)return 1;
		else return 0;
	}
	public ArrayList<Golfball> bubbleSort (ArrayList<Golfball> list){
		boolean swaps = true;
		int length = list.size();
		while (swaps) {
			swaps = false;
			for(int i = 0; i < length-1; i++) {
				if(compare(list.get(i), list.get(i+1)) > 0) {
					Golfball placeholder = list.get(i);
					list.set(i, list.get(i+1));
					list.set(i+1, placeholder);
					swaps = true;
				}
			}
			length --;
		}
		return list;
	}
	/**
	 * This is based on the assumption, that the team size = 2
	 * @param list
	 * @return
	 */
	public ArrayList<Golfball> makeTeamModeScores(ArrayList<Golfball> list){
		ArrayList<Golfball> result = new ArrayList<>();
		for(int i = 0; i < list.size(); i+=2) {
			if(compare(list.get(i), list.get(i+1))<0) result.add(list.get(i+1));
			else result.add(list.get(i));
		}
		result =bubbleSort(result);
		return result;	
	}
	public int getTeamIndex(int i) {
		int index = (int) i / 2;
		return (index+1);
	}
}

