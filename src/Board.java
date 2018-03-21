import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Rectangle;

public class Board {
	private Rectangle boardShape;
	private Texture boardImage;
	private double frictionConstant;
	
	public Board() {
		float green = 0; 
		Pixmap pixmap = new Pixmap( 700, 1000, Format.RGBA8888 );
		
		//pixmap.setColor(new Color(5, green, 5,5) );
		pixmap.fillRectangle( 100, 0, 700,1000 );
		System.out.println("b");
		boardImage = new Texture(pixmap);
		frictionConstant = 2;
		boardShape = new Rectangle();
		boardShape.x = 100;
		boardShape.y = 0;
		boardShape.width = 700 ;
		boardShape.height = 1000;
		
	}
	public double getFrictionConstant() {
		return frictionConstant;
	}
	public Rectangle getRectangle() {
		return boardShape;
	}
	
	public Texture getSprite() {
		return boardImage;
	}


}


