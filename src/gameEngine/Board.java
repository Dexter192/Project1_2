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
		float[] a = {1, 0};
		float[] b = {-1, 0};
		physics = new Physics(a,b);
		
		Pixmap pixmap = new Pixmap( 700, 1000, Format.RGBA8888 );
		float green = 60;
		int y = 0;
		int x = 100;
		for(int i = 0; i < 100; i ++ ) {
			for(int j = 0; j < 70; j ++ ) {
				float height = physics.getHeight(i, j);
				if(height < 0) {
					pixmap.setColor(0, 0, 5,1);
					pixmap.fillRectangle(x, y, 700, 1000);
					System.out.println("blue : " + height);
				}
				else{
					green = 10 + (0.01f *height) ;	
					System.out.println("green : " + height);
					pixmap.setColor(0, green, 0,1);
					pixmap.fillRectangle(x, y, 700, 1000);
				}
				x += 20;
			}
			y += 10;
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


