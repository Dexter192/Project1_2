import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

public class Golfball {

	private Circle ballShape;
	private Texture ballImage;
	private double mass;
	
	public Golfball() {
		
		Pixmap pixmap = new Pixmap( 64, 64, Format.RGBA8888 );
		pixmap.setColor(Color.RED );
		pixmap.fillCircle( 32, 32, 32 );
		mass = 5.5;
		ballImage = new Texture(pixmap);
		
		ballShape = new Circle();
		ballShape.x = 200;
		ballShape.y = 200;
		ballShape.radius = 32;

	}
	public double getMass() {
		return mass;
	}
	public Circle getCircle() {
		return ballShape;
	}
	
	public Texture getSprite() {
		return ballImage;
	}
}
