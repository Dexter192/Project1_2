package ai;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import Obstacles.Hole;
import Obstacles.Obstacle;
import collisionDetector.CollisionDetector;
import gameEngine3D.GameScreen3D;

public class AStar {

	// TODO: We dont need to find the path to the hole. Apparently the A* algorithm
	// is allowed to know, where the hole is. In that case replacing the sets with a
	// map and assigning costs to a vector/position would be more useful
	// The structure of the algorithm stays very similar though.
	// TODO: The step size might be adjusted to the smallest obstacle size
	// Only add the element with the cheapest cost to the expandable list
	// Take the cheapest element of the queue and iterate over it
	// Create new individual objects for the nodes.
	
	//Objekte müssen Nodes haben!

	private GameScreen3D gamescreen;
	private BoundingBox courseDimensions;
	private Vector3 holeDimensions;
	private Vector3 ballPosition;
	private HashSet<Vector3> exploredAreas;
	private HashSet<Vector3> expandableAreas;
	private CollisionDetector collisionDetector;
	private Set<Obstacle> obstacleList;
	private boolean hasFoundPath = false;

	/*
	 * Currently this should be able to find the hole in the entire course.
	 * Identifying the shortest path to it will be a different thing though.
	 */

	public AStar(GameScreen3D gamescreen) {
		this.gamescreen = gamescreen;
		courseDimensions = gamescreen.getCouserDimensions();
		holeDimensions = gamescreen.getHole().getBoundingBox().getDimensions(new Vector3());
		ballPosition = gamescreen.getGolfball().getPosition();
		collisionDetector = new CollisionDetector();
		expandableAreas = new HashSet<>();
		expandableAreas.add(ballPosition);
		exploredAreas = new HashSet<>();
		obstacleList = gamescreen.getAllObstacles();
	}

	public void findPathToHole() {
		System.out.println("Hole: " + gamescreen.getHole().getBoundingBox());

		while (!hasFoundPath && !expandableAreas.isEmpty()) {
			Iterator<Vector3> itr = expandableAreas.iterator();
			expandArea(itr.next());
		}
		if (hasFoundPath)
			System.out.println("Found Path!");
		// TODO: iterate through all expandable areas center positions, check if there
		// is a an overlap otherwise expand the area and shift it in the explored area
		// set
	}

	/**
	 * Computes all the areas around the current position with a distance of the
	 * hole radius.
	 * 
	 * @param expandablePosition
	 */
	private void expandArea(Vector3 expandablePosition) {
		// TODO: see, if we still are in the feasible region
		Vector3[] unexploredPositions = new Vector3[26];
		for (int i = 0; i < unexploredPositions.length; i++)
			unexploredPositions[i] = new Vector3();
		// Build a "Box" around the current position by computing the center positions
		// of the touching boxes
		// The for loop is for the y positions. We build a square below the position of
		// 9 tiles, one above and one at the same y level with 8 tiles
		int counter = 0;
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					if (x == 0 && y == 0 && z == 0)
						continue;
					unexploredPositions[counter].x = expandablePosition.x + x * holeDimensions.x;
					unexploredPositions[counter].y = expandablePosition.y + y * holeDimensions.y;
					unexploredPositions[counter].z = expandablePosition.z + z * holeDimensions.z;
					counter++;
				}
			}
		}

		// Prehas split this 'monster' into multiple methods to make it more clear and
		// readable
		for (Vector3 v : unexploredPositions) {
			// If the current postition is already in one of the two sets just to the next
			// position.
			if (exploredAreas == null || exploredAreas.contains(v) || expandableAreas.contains(v))
				continue;
			BoundingBox boundingBox = buildBoundingBoxAroundPosition(v);
			// Check, if we are still int the bounds of the course dimenstions
			if (collisionDetector.determineIntersection(boundingBox, courseDimensions)) {
				// Only add positions, which dont intersect with an obstacle.
				boolean intersectsWithObstacle = false;
				for (Obstacle o : obstacleList) {
					// If we intersect with the hole we found a solution!
					if (o instanceof Hole && collisionDetector.determineIntersection(boundingBox, o.getBoundingBox())) {
						hasFoundPath = true;
						// System.out.println("A* Intersection position is at: " + boundingBox + "\nThe
						// hole position at: " + o.getBoundingBox().toString());
					}
					if (!collisionDetector.determineIntersection(boundingBox, o.getBoundingBox())) {
						intersectsWithObstacle = true;
						break;
					}
				}
				if (!intersectsWithObstacle)
					expandableAreas.add(v);
			}

		}

		exploredAreas.add(expandablePosition);
		expandableAreas.remove(expandablePosition);
	}

	/**
	 * Builds a boundingbox around a given vector. The boundingbox has the size of
	 * the hole bounding box TODO: Prehaps scale down the bounding box, if it does
	 * not find the hole, since it might just barely overjump it.
	 * 
	 * @param position
	 *            The center position of the point, where the boundingbox should be
	 *            build around
	 * @return A boundingbox, with the dimensions of the hole and the center in the
	 *         position from the parameter
	 */
	private BoundingBox buildBoundingBoxAroundPosition(Vector3 position) {
		Vector3 holeRadius = new Vector3(holeDimensions.x / 2, holeDimensions.y / 2, holeDimensions.z / 2);
		// Vectro3.sub/add did not work....
		Vector3 min = new Vector3(position.x - holeRadius.x, position.y - holeRadius.y, position.z - holeRadius.z);
		Vector3 max = new Vector3(position.x + holeRadius.x, position.y + holeRadius.y, position.z + holeRadius.z);
		return new BoundingBox(min, max);
	}

}
