package ai;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import Obstacles.Hole;
import Obstacles.Obstacle;
import collisionDetector.CollisionDetector;
import gameEngine3D.GameScreen3D;
import gameEngine3D.Golfball;
import physics.VectorComputation;

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

	private HashSet<AStarTile> openList;
	private HashSet<AStarTile> closedList;
	private AStarTile lastTile;
	
	private List<AStarTile> pathToHole;
	private BoundingBox courseDimensions;
	private Vector3 holePosition;
	private Vector3 startPosition;
	private Vector3 goalPosition;
	private CollisionDetector collisionDetector;
	private Set<Obstacle> obstacleList;
	private boolean hasFoundPath = false;
	private float stepSize; 
	private Golfball golfBall;
	private GameScreen3D gameScreen;
	private GeneticHitStrength geneticHitStrength;
	

	/*
	 * Currently this should be able to find the hole in the entire course.
	 * Identifying the shortest path to it will be a different thing though.
	 */

	public AStar(GameScreen3D gamescreen) {
		this.golfBall = gamescreen.getGolfball();
		courseDimensions = gamescreen.getCouserDimensions();
		holePosition = gamescreen.getHole().getBoundingBox().getCenter(new Vector3());
		collisionDetector = new CollisionDetector();
		openList = new HashSet<>();
		closedList = new HashSet<>();
		obstacleList = gamescreen.getAllObstacles();
		pathToHole = new ArrayList<>();
		gameScreen = gamescreen;
		geneticHitStrength = new GeneticHitStrength();
		this.stepSize = computeStepsize();
		goalPosition = new Vector3(0,0,0);
		startPosition = new Vector3(0,0,0);
	}


	public void makeMove() {
		if(!pathToHole.isEmpty()) {			
			geneticHitStrength.updateStrengthPerUnit(startPosition, goalPosition, golfBall.getPosition());
		}
		findPathToHole();
		List<AStarTile> straightPath = computeStraightPathFromPosition();
		
		startPosition = new Vector3(straightPath.get(0).getPosition());
		goalPosition = new Vector3(straightPath.get(straightPath.size()-1).getPosition());
		
		Vector3 velocityVector = new Vector3(geneticHitStrength.getHitStrength(golfBall.getPosition(), goalPosition));
		golfBall.setVelocity(velocityVector);
	}
	

	public void findPathToHole() {
		openList.clear();
		closedList.clear();
		pathToHole.clear();
		hasFoundPath = false;
		
		AStarTile start = new AStarTile(gameScreen.getGolfball().getPosition(), holePosition);
		openList.add(start);
		
		while (!hasFoundPath && !openList.isEmpty()) {
			AStarTile cheapestElement = findCheapestElement();
			expandArea(cheapestElement);
		}
		if (hasFoundPath) {
			AStarTile temp = lastTile;
			pathToHole.add(temp);
			while(temp.getParent() != null) {
				temp = temp.getParent();
				pathToHole.add(temp);
			}
			pathToHole = flipList(pathToHole);
		}
	}
	
	public List<AStarTile> flipList(List<AStarTile> pathToHole) {
		List<AStarTile> flippedPathToHole = new ArrayList<>();		
		for(int i = pathToHole.size()-1; i >= 0; i--) {
			flippedPathToHole.add(pathToHole.get(i));
		}
		return flippedPathToHole;
	}
	
	public void setToNextPosition() {
		if(lastTile == null) {
			return;
		}
			golfBall.setPosiition(lastTile.getPosition());
			lastTile = lastTile.getParent();
	}

	/**
	 * Computes all the areas around the current position with a distance of the
	 * hole radius.
	 * 
	 * @param expandablePosition
	 */
	private void expandArea(AStarTile expandTile) {
		Vector3 expandPosition = expandTile.getPosition();
		// TODO: see, if we still are in the feasible region

		System.out.println("RemainingDist: " + VectorComputation.getInstance().getDistance(holePosition, expandTile.getPosition()));
		
		HashSet<AStarTile> neighbours = new HashSet<>(); 
		// Build a "Box" around the current position by computing the center positions
		// of the touching boxes
		// The for loop is for the y positions. We build a square below the position of
		// 9 tiles, one above and one at the same y level with 8 tiles
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					if (x == 0 && y == 0 && z == 0)
						continue;
					Vector3 newPosition = new Vector3();
					newPosition.x = expandPosition.x + x * stepSize;
					newPosition.y = expandPosition.y + y * stepSize;
					newPosition.z = expandPosition.z + z * stepSize;
					AStarTile temp = new AStarTile(expandTile, newPosition, holePosition);
					neighbours.add(temp);
				}
			}
		}

		// Prehas split this 'monster' into multiple methods to make it more clear and
		// readable
		for (AStarTile v : neighbours) {
			// If the current postition is already in one of the two sets just to the next
			// position.
			if (isInOpenList(v) || isInClosedList(v))
				continue;
			
			BoundingBox boundingBox = buildBoundingBoxAroundPosition(v.getPosition());
			// Check, if we are still int the bounds of the course dimenstions
//			if (collisionDetector.determineIntersection(boundingBox, courseDimensions)) {
				// Only add positions, which dont intersect with an obstacle.
				boolean intersectsWithObstacle = false;
				for (Obstacle o : obstacleList) {
					// If we intersect with the hole we found a solution!
					if (o instanceof Hole && collisionDetector.determineIntersection(boundingBox, o.getBoundingBox())) {
						hasFoundPath = true;
						lastTile = v;
						// System.out.println("A* Intersection position is at: " + boundingBox + "\nThe
						// hole position at: " + o.getBoundingBox().toString());
					}
					if (collisionDetector.determineIntersection(boundingBox, o.getBoundingBox())) {
						intersectsWithObstacle = true;
						break;
					}
				}
				if (!intersectsWithObstacle)
//					System.out.println("Add: " + boundingBox);
					openList.add(v);
//			}

		}

		closedList.add(expandTile);
		openList.remove(expandTile);
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
		// Vectro3.sub/add did not work....
		Vector3 min = new Vector3(position.x - stepSize/2, position.y - stepSize/2, position.z - stepSize/2);
		Vector3 max = new Vector3(position.x + stepSize/2, position.y + stepSize/2, position.z + stepSize/2);
		return new BoundingBox(min, max);
	}
	
	private AStarTile findCheapestElement() {
		AStarTile cheapestTile = null;
		float minCost = Integer.MAX_VALUE;
		for(AStarTile a : openList) {
			if(a.getTotalCost() < minCost) { 
				cheapestTile = a;
				minCost = a.getTotalCost();
			}
		}
		return cheapestTile;
	}

	public boolean isInOpenList(AStarTile comparableTile) {
		for(AStarTile a : openList) {
			if(a.getPosition().equals(comparableTile.getPosition())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isInClosedList(AStarTile comparableTile) {
		for(AStarTile a : closedList) {
			if(a.getPosition().equals(comparableTile.getPosition())) {
				return true;
			}
		}
		return false;
	}
	
	private float computeStepsize() {
		float stepSize = Integer.MAX_VALUE;
		Set<Obstacle> obstacleList = gameScreen.getAllObstacles();
		Iterator<Obstacle> it = obstacleList.iterator();
	
		while(it.hasNext()) {
			Obstacle o = it.next();
			float minObstacleSize = Math.min(o.getBoundingBox().getWidth(), o.getBoundingBox().getHeight());
			minObstacleSize = Math.min(minObstacleSize, o.getBoundingBox().getDepth());
			stepSize = Math.min(minObstacleSize, stepSize);			
		}
		stepSize = (float) (stepSize*0.99);
		return stepSize;
	}

	private List<AStarTile> computeStraightPathFromPosition() {
		List<AStarTile> straightPath = new ArrayList<>();
		Vector3 temp = new Vector3(pathToHole.get(0).getPosition());
		Vector3 direction = temp.sub(pathToHole.get(1).getPosition());

		straightPath.add(pathToHole.get(0));
		straightPath.add(pathToHole.get(1));
		
		for(int i = 1; i < pathToHole.size()-1; i++) {
			temp = new Vector3(pathToHole.get(i).getPosition());
			Vector3 subDirection = temp.sub(pathToHole.get(i+1).getPosition());
			if(direction.x <= subDirection.x + 0.05 * stepSize && direction.z <= subDirection.z + 0.05 * stepSize &&
			   direction.x >= subDirection.x - 0.05 * stepSize && direction.z >= subDirection.z - 0.05 * stepSize ) {
				straightPath.add(pathToHole.get(i));
			}
			else {
				break;
			}
		}
		
		return straightPath;
	}
	
	class GeneticHit {
		
		
		
	}
}
