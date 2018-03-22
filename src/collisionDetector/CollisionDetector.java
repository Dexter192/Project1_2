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
		
		System.out.println(ballBoundingBox + " - " + obstacleBoundingBox);
		
		if(ballBoundingBox.intersects(obstacleBoundingBox)) {
			intersects = true;
		}
		return intersects;
	}
	
}
