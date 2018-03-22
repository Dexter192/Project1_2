package gameEngine;
import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Rectangle;
public class Board {
	private Rectangle boardShape;
	private Texture boardImage;
	private float frictionConstant;
	private Physics physics;
	public Board() {
		float[] a = {-1, 1};
		float[] b = {-1, 0};
		physics = new Physics(a,b);
		
		Pixmap pixmap = new Pixmap( 700, 1000, Format.RGBA8888 );
		float green = 60;
		int y = 0;
		int x = 100;
		for(double i = 0; i < 10; i += 0.5 ) {
			for(double j = 0; j < 7; j += 0.5 ) {
				float height = physics.getHeight(i, j);
				if(height < 0) {
					pixmap.setColor(0, 0, 5,1);
					pixmap.fillRectangle(x, y, 700, 1000);
					System.out.println("blue : " + height);
				}
				else{
					green = 10 + (0.1f *height) ;	
					System.out.println("green : " + height);
					pixmap.setColor(0, green, 0,1);
					pixmap.fillRectangle(x, y, 700, 1000);
				}
				x += 2;
			}
			y += 1;
			x = 100;
		}
		boardImage = new Texture(pixmap);
		
		boardShape = new Rectangle();
		boardShape.x = 100;
		boardShape.y = 0;
		boardShape.width = 700 ;
		boardShape.height = 1000;
		frictionConstant = 1;
	}
	public float getHeight(double x, double y) {
		return physics.getHeight(x, y);
	}
	public Rectangle getRectangle() {
		return boardShape;
	}
	
	public Texture getSprite() {
		return boardImage;
	}

	public float getFriction() {
		return frictionConstant;
	}
}


