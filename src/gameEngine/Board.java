package gameEngine;
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
		float[] a = {0,0};
		float[] b = {0.02f,0};
		physics = new Physics(a,b);
		
		Pixmap pixmap = new Pixmap( 700, 1000, Format.RGBA8888 );
		float green = 60;
		int y = 0;
		for(int i = 0; i < 100; i ++ ) {
			green = 10 + (0.01f *physics.getHeight(i, 0)) ;	
			System.out.println("green : " + green);
			pixmap.setColor(0, green, 0,1);
			pixmap.fillRectangle(100,y, 700, 1000);
			y += 10;
		}
		boardImage = new Texture(pixmap);
		
		boardShape = new Rectangle();
		boardShape.x = 100;
		boardShape.y = 0;
		boardShape.width = 700 ;
		boardShape.height = 1000;
		frictionConstant = 10;
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


