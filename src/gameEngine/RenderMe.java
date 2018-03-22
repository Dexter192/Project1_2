package gameEngine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.math.Rectangle;

public class RenderMe
{
	private Rectangle boardShape;
	private Texture boardImage;
	private float frictionConstant;
	
	public RenderMe(AABB2D k) 
	{	
		Pixmap pixmap = new Pixmap( (int)(k.getHigh()).x, (int)(k.getHigh()).y, Format.RGBA8888 ); //change(size)
		
		// Apply terrain settings.
		if (k.terrain == 0) // Grass.
		{
			pixmap.setColor(Color.GREEN);
			frictionConstant = 1;
		}
		else if (k.terrain == 1)
		{
			pixmap.setColor(Color.YELLOW); // Sand.
			frictionConstant = 4;
		}
		else if (k.terrain == 2)
		{
			pixmap.setColor(Color.BLUE); // Water.
			frictionConstant = 1;
		}
		else if (k.terrain == 3)
		{
			pixmap.setColor(Color.WHITE); // Obstacle (not passable).
			frictionConstant = 1;
		}
		
		pixmap.fillRectangle((int)(k.getLow()).x, (int)(k.getLow()).y, (int)(k.getHigh()).x, (int)(k.getHigh()).y); //Change (fill), first two are xy start, second are xy end
		
		boardImage = new Texture(pixmap); //colouring shit or something idk, not texture as in pattern (i think...)
		
		boardShape = new Rectangle();
		boardShape.x = (k.getLow()).x; //starting coordinates
		boardShape.y = (k.getLow()).y; //above
		boardShape.width = ((k.getHigh()).x - (k.getLow()).x) ;
		boardShape.height = ((k.getHigh()).y - (k.getLow()).y) ;
		
	}
	
	
	/*public RenderMe(BoundingSphere2D k) 
	{	
		Pixmap pixmap = new Pixmap( (k.getHigh()).x, (k.getHigh()).y, Format.RGBA8888 ); //change(size)
		
		// Apply terrain settings.
		pixmap.setColor(Color.GREEN);
		frictionConstant = 1;
		pixmap.fillRectangle((k.getLow()).x, (k.getLow()).y, (k.getHigh()).x, (k.getHigh()).y); //Change (fill), first two are xy start, second are xy end
		
		boardImage = new Texture(pixmap); //colouring shit or something idk, not texture as in pattern (i think...)
		
		boardShape = new Sphere(); // is this right?
		boardShape.x = (k.getLow()).x;
		boardShape.y = (k.getLow()).y;
		boardShape.width = ((k.getHigh()).x - (k.getLow()).x);
		boardShape.height = ((k.getHigh()).y - (k.getLow()).y);
		
	}*/
	
	public Rectangle getRectangle() {	return boardShape;	} // representing 
	
	public Texture getSprite() {	return boardImage;	} //Printing

	public float getFriction() {	return frictionConstant;	} 
}

			/*
			Pixmap pixmap = new Pixmap( 700, 1000, Format.RGBA8888 ); //change(size)
			
			pixmap.setColor(Color.GREEN); //Change
			pixmap.fillRectangle(100, 0, 700, 1000); //Change (fill), first two are xy start, second are xy end
			
			boardImage = new Texture(pixmap); //colouring shit or something idk, not texture as in pattern (i think...)
			
			boardShape = new Rectangle(); //change for sphere
			boardShape.x = 100; //starting coordinates
			boardShape.y = 0; //above
			boardShape.width = 700 ;
			boardShape.height = 1000;
			frictionConstant = 1;
			*/