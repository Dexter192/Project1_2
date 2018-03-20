package GameEngine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public class Hole {
	private Circle holeShape;
	private Texture holeImage;
	
	
	public Hole() {
		
		Pixmap pixmap = new Pixmap( 64, 64, Format.RGBA8888 );
		pixmap.setColor(Color.BLACK );
		pixmap.fillCircle( 32, 32, 32 );
		
		holeImage = new Texture(pixmap);
		
		holeShape = new Circle();
		holeShape.x = 450;
		holeShape.y = 900;
		holeShape.radius = 32;
		
	}
	
	public Circle getCircle() {
		return holeShape;
	}
	
	public Texture getSprite() {
		return holeImage;
	}
}
