package collisionDetector;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import Obstacles.Hole;
import Obstacles.Obstacle;
import gameEngine3D.Golfball;
import physics.DifferentialEquationSolver;
import physics.VectorComputation;

public class CollisionDetector {

	private static boolean inHole = false;
	private DifferentialEquationSolver ode;
	public CollisionDetector(DifferentialEquationSolver ode) {
		this.ode = ode;
	}

	public boolean detectCollision(Golfball ball, Obstacle obstacle) {
		boolean intersects = false;
		
		BoundingBox ballBoundingBox = ball.getBoundingBox();

		BoundingBox obstacleBoundingBox = obstacle.getBoundingBox();
//
		// System.out.println("Ball: " + ballBoundingBox + " , " + obstacleBoundingBox);

		if (obstacle instanceof Hole) {
//			System.out.println(ball.getPosition() + ", " + obstacle.getBoundingBox().getCenter(new Vector3()));
			if (determineHoleIntersection(ball, (Hole) obstacle)) {
				inHole = true;
				handleHoleCollision(ball, (Hole) obstacle);
			}
			else {				
				inHole = false;
			}
		}
		else if (determineIntersection(ballBoundingBox, obstacleBoundingBox) && !inHole) {
			handleCollision(ball, obstacleBoundingBox);
		}
		return intersects;
	}

	/**
	 * This code may not work for rotated obstacles
	 * 
	 * @param ball
	 * @param obstacle
	 * @return
	 */
	private boolean determineIntersection(BoundingBox ball, BoundingBox obstacle) {
		Vector3 ballMin = ball.min, ballMax = ball.max, obstacleMin = obstacle.min, obstacleMax = obstacle.max;

		if (ballMin.x < obstacleMax.x && ballMax.x > obstacleMin.x && ballMin.y < obstacleMax.y
				&& ballMax.y > obstacleMin.y && ballMin.z < obstacleMax.z && ballMax.z > obstacleMin.z) {
			return true;
		}

		return false;
	}

	private boolean determineHoleIntersection(Golfball ball, Hole hole) {
		Vector3 holeCenter = new Vector3();
		hole.getBoundingBox().getCenter(holeCenter);
		float xz = VectorComputation.getInstance().getDistanceXZ(ball.getPosition(), holeCenter);

		if (xz < hole.getRadius()) {
			return true;
		}

		return false;
	}

	private void handleHoleCollision(Golfball ball, Hole hole) {
		Vector3 holeCenter = new Vector3();
		hole.getBoundingBox().getCenter(holeCenter);
		float xz = VectorComputation.getInstance().getDistanceXZ(ball.getPosition(), holeCenter);

		Vector3 velocity = holeCenter.sub(ball.getPosition());
		velocity.scl(0.05f);
		ball.setPosiition(ball.getPosition().add(velocity));
	}

	/**
	 * 
	 * @param ball
	 * @param obstacle
	 */
	private void handleCollision(Golfball ball, BoundingBox obstacle) {
		BoundingBox ballBox = ball.getBoundingBox();
		Vector3 ballMin = ballBox.min, ballMax = ballBox.max, obstacleMin = obstacle.min, obstacleMax = obstacle.max;
		Vector3 ballPosition = ball.getPosition();
		Vector3 reflectionAxis = new Vector3(1, 1, 1);

		// TODO: Corner collision. Think this is a good approach but may not work as
		// intended
		// if(ballMin.x < obstacleMax.x && ballMax.x > obstacleMax.x && ballMin.z <
		// obstacleMax.z && ballMax.z > obstacleMax.z) {
		// reflectionAxis = new Vector3(-1,1,-1);
		// ballPosition.x = obstacleMax.x + ball.getRadius() + 0.05f;
		// ballPosition.z = obstacleMax.z + ball.getRadius() + 0.05f;
		// }
		// else if(ballMax.x > obstacleMin.x && ballMin.x < obstacleMin.x && ballMax.z >
		// obstacleMin.z && ballMin.z < obstacleMin.z) {
		// reflectionAxis = new Vector3(-1,1,-1);
		// ballPosition.x = obstacleMin.x - ball.getRadius() - 0.05f;
		// ballPosition.z = obstacleMin.z - ball.getRadius() - 0.05f;
		// }

		// Collision with the x sides of an obstacle
		if (ballMin.x < obstacleMax.x && ballMax.x > obstacleMax.x) {
			reflectionAxis = new Vector3(-1, 1, 1);
			ballPosition.x = obstacleMax.x + ball.getRadius() + 0.05f;
		} else if (ballMax.x > obstacleMin.x && ballMin.x < obstacleMin.x) {
			reflectionAxis = new Vector3(-1, 1, 1);
			ballPosition.x = obstacleMin.x - ball.getRadius() - 0.05f;
		}
		// Collisions with the y side of an obstacle
		else if (ballMin.y < obstacleMax.y && ballMax.y > obstacleMax.y) {
			reflectionAxis = new Vector3(1, -1, 1);
			ballPosition.y = obstacleMax.y + ball.getRadius() + 0.05f;
		} else if (ballMax.y > obstacleMin.y && ballMin.y < obstacleMin.y) {
			reflectionAxis = new Vector3(1, -1, -1);
			ballPosition.y = obstacleMin.y - ball.getRadius() - 0.05f;
		}
		// Collisions with the z sides of an obstacle
		else if (ballMin.z < obstacleMax.z && ballMax.z > obstacleMax.z) {
			reflectionAxis = new Vector3(1, 1, -1);
			ballPosition.z = obstacleMax.z + ball.getRadius() + 0.05f;
		} else if (ballMax.z > obstacleMin.z && ballMin.z < obstacleMin.z) {
			reflectionAxis = new Vector3(1, 1, -1);
			ballPosition.z = obstacleMin.z - ball.getRadius() - 0.05f;
		}

		// TODO: Absorb force when colliding
		ball.bounceOff(reflectionAxis);
		ball.setPosiition(ballPosition);
		ball.update(ode);
	}
}
