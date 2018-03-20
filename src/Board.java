import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Rectangle;

public class Board {
	private Rectangle boardShape;
	private Texture boardImage;
	private float frictionConstant;
	
	public float getFriction() {
		return frictionConstant;
	}
	
	public Board() {
		
		Pixmap pixmap = new Pixmap( 700, 1000, Format.RGBA8888 );
		
		pixmap.setColor(Color.GREEN);
		pixmap.fillRectangle( 100, 0, 700,1000 );
		
		boardImage = new Texture(pixmap);
		
		boardShape = new Rectangle();
		boardShape.x = 100;
		boardShape.y = 0;
		boardShape.width = 700 ;
		boardShape.height = 1000;
		frictionConstant = 1;
	}
	
	public Rectangle getRectangle() {
		return boardShape;
	}
	
	public Texture getSprite() {
		return boardImage;
	}




}


