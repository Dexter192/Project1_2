import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Golfball {

	private Rectangle ballShape;
	private Texture ballImage;
	
	
	public Golfball() {
		
		Pixmap pixmap = new Pixmap( 64, 64, Format.RGBA8888 );
		pixmap.setColor( 0, 1, 0, 0.75f );
		pixmap.fillCircle( 32, 32, 32 );
		
		ballImage = new Texture(pixmap);
		
		ballShape = new Rectangle();
		ballShape.x = 800 / 2 - 64 / 2;
		ballShape.y = 20;
		ballShape.width = 64;
		ballShape.height = 64;
		
	}
	
	public Rectangle getRectangle() {
		return ballShape;
	}
	
	public Texture getSprite() {
		return ballImage;
	}
}
