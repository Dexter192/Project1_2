package collisionDetector;

import com.badlogic.gdx.math.collision.BoundingBox;

import gameEngine3D.Golfball;
import gameEngine3D.Obstacle;

public class CollisionDetector {

	public CollisionDetector() {

	}

	public boolean detectCollision(Golfball ball, Obstacle obstacle) {
		boolean intersects = false;

		BoundingBox ballBoundingBox = new BoundingBox();
		ball.getBallInstance().calculateBoundingBox(ballBoundingBox);

		BoundingBox obstacleBoundingBox = new BoundingBox();
		obstacle.getInstance().calculateBoundingBox(obstacleBoundingBox);

		System.out.println(obstacleBoundingBox + " " + ballBoundingBox);
		
		
		if (ballBoundingBox.intersects(obstacleBoundingBox)) {
			intersects = true;
		}
		return intersects;
	}
	
	
	/**
	 * This method handles the collision between the two objects and returns the new direction vector
	 * @return
	 */
	public Vector3 handleCollision() {
		return null;
	}

}
