package gameEngine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public class Golfball {

	private Circle ballShape;
	private Texture ballImage;
	private float mass;
	private int radius = 12;
	private Vector3 previousPosition;

	public Golfball() {

		Pixmap pixmap = new Pixmap(radius * 2, radius * 2, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fillCircle(radius, radius, radius);

		ballImage = new Texture(pixmap);

		ballShape = new Circle();
		ballShape.radius = radius;
		ballShape.x = 200;
		ballShape.y = 200;
		previousPosition = new Vector3(ballShape.x, ballShape.y, 0);
		mass = 1;
	}

	public float getMass() {
		return mass;
	}

	public void setPreviousPosition(Vector3 previousPositon) {
		this.previousPosition = previousPositon;
	}

	public Vector3 getPreviousPosition() {
		return previousPosition;
	}

	public Circle getCircle() {
		return ballShape;
	}

	public Texture getSprite() {
		return ballImage;
	}
}
