package ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import Obstacles.Hole;
import Obstacles.*;
import Obstacles.Obstacle;
import collisionDetector.CollisionDetector;
import gameEngine3D.GameScreen3D;
import gameEngine3D.Golfball;
import physics.VectorComputation;


public class AStar 
{
	 
	private HashSet<AStarTile> openList; // Viable area, needs to be evaluated.
	private HashSet<AStarTile> closedList; // Cleared area.
	private ArrayList<AStarTile> pathList; 
	private AStarTile lastTile; // Final tile in the path.
		
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
	public static boolean strokeHeuristic = true;

		public AStar(GameScreen3D gamescreen) {
	
		this.golfBall = gamescreen.getGolfball();
		courseDimensions = gamescreen.getCouserDimensions();
		holePosition = gamescreen.getHole().getBoundingBox().getCenter(new Vector3());
		obstacleList = gamescreen.getAllObstacles();
		
		collisionDetector = new CollisionDetector();
		openList = new HashSet<>();
		closedList = new HashSet<>();
		obstacleList = gamescreen.getAllObstacles();
		pathToHole = new ArrayList<>();
		gameScreen = gamescreen;
		geneticHitStrength = new GeneticHitStrength();
		this.stepSize = 0.25f;//computeStepsize();
		goalPosition = new Vector3(0,0,0);
		startPosition = new Vector3(0,0,0);
	}



	public Collection<Obstacle> makeMove() {

		gameScreen.pause();
		float delta = Gdx.graphics.getDeltaTime();
		float starttime = System.nanoTime();
		if(!pathToHole.isEmpty()) {			
			geneticHitStrength.updateStrengthPerUnit(startPosition, goalPosition, golfBall.getPosition());
		}
		findPathToHole();
		List<AStarTile> straightPath = computeStraightPathFromPosition();
		float endTime = System.nanoTime();
		float time = endTime-starttime;
		System.out.println(time);
		Collection<Obstacle> obstacleList = buildPathIllustration();
		if(straightPath.size() != 0) {
		startPosition = new Vector3(straightPath.get(0).getPosition());
		goalPosition = new Vector3(straightPath.get(straightPath.size()-1).getPosition());}
		
//		System.out.println(startPosition + " " + goalPosition);
		
		Vector3 velocityVector = new Vector3(geneticHitStrength.getHitStrength(golfBall.getPosition(), goalPosition));
		
		golfBall.setVelocity(velocityVector);
		gameScreen.resume();
		return obstacleList;
	}
	
	

	private Collection<Obstacle> buildPathIllustration() {
		Collection<Obstacle> obstacleList = new HashSet<>();
		Iterator<AStarTile> itr = pathToHole.iterator();
		Color c = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1f);
		while(itr.hasNext()) {
			AStarTile tile = itr.next();
			Vector3 position = tile.getPosition();
			obstacleList.add(new ObstacleBox(position.x, position.y, position.z, stepSize, stepSize, stepSize, c));
		}		
		return obstacleList;
	}


	public void findPathToHole() {
		openList.clear();
		closedList.clear();
		pathToHole.clear();
		hasFoundPath = false;
		
		AStarTile start = new AStarTile(gameScreen.getGolfball().getPosition(), holePosition); // Initial tile constructor.
		openList.add(start);
		AStarTile cheapestElement = null; ///* THIS LINE GOT ADDED SO A NEW AStarTile OBJECT WASN'T CREATED EACH LOOP *///
		
		// Search.
		//float startTime = System.currentTimeMillis();
		while (!hasFoundPath && !openList.isEmpty()) 
		{
			cheapestElement = findCheapestElement(); // Checks openList and finds the best valued tile.
			aStarStep(cheapestElement); // Uses the "best valued tile" to the expand the openlist, rinse and repeat.
		}
		//float endTime = System.currentTimeMillis();
		if (hasFoundPath) {
			AStarTile temp = lastTile;
			pathToHole.add(temp);
			while(temp.getParent() != null) {
				temp = temp.getParent();
				pathToHole.add(temp);
			}
			pathToHole = flipList(pathToHole);
		}
		else System.out.println("ERROR: Path impossible.");

	}
	
	public List<AStarTile> flipList(List<AStarTile> pathToHole) {
		List<AStarTile> flippedPathToHole = new ArrayList<>();		
		for(int i = pathToHole.size()-1; i >= 0; i--) {
			flippedPathToHole.add(pathToHole.get(i));
		}
		return flippedPathToHole;
	}
		
	
	private AStarTile findCheapestElement()  {
		AStarTile cheapestTile = null;
		float minCost = Integer.MAX_VALUE;
		for(AStarTile a : openList) // Scan the openList for the lowest value tile.
			if(a.getTotalCost() < minCost) { 
				cheapestTile = a;
				minCost = a.getTotalCost();
			}
		return cheapestTile;
	}

	
	/**
	 * Computes all the areas around the current position with a distance of the
	 * hole radius.
	 * 
	 * This method is mainly composed of two helper methods: helperA (1) and helperB (2),(3). 
	 * This method performs (4) on its own.
	 * 
	 * 1) Create 3x3x3 box around the input position and label the edge bounding boxes "neighbours".
	 * 2) Scan through the neighbours list and check their validity.
	 * 3) Also check if each neighbour intersects with an object, and if that object is the Hole (goal).
	 * 4) Modify openList and closedList.
	 * 
	 * @param expandablePosition
	 */
	private void aStarStep(AStarTile expandTile) {
		HashSet<AStarTile> neighbours = expandArea(expandTile);
		addTiles(neighbours);
		closedList.add(expandTile);
		openList.remove(expandTile);
	}
	
	// Generates 3x3x3 grid and thus creates the given tile's neighbours.
	private HashSet<AStarTile> expandArea(AStarTile expandTile) {
		Vector3 expandPosition = expandTile.getPosition(); // Current coordinates.
		// TODO: see, if we still are in the feasible region ///* LIKE CHECK THIS TILE ISNT IN closedList? *///

//		System.out.println("Remaining Distance: " + VectorComputation.getInstance().getDistance(holePosition, expandTile.getPosition())); // From this tile to the goal.
		
		
		// Build a "Box" around the current position by computing the centre positions of the touching boxes.
		// The for loop is for the y positions. We build a square below the position of 9 tiles, one above and one at the same y level with 8 tiles. ///* ...WHAT? *///
		
		// Create a 3x3x3 "box" around the current position with the edge tiles the neighbouring tiles of this tile.
		// 
		HashSet<AStarTile> neighbours = new HashSet<>(); 
		for (int x = -1; x <= 1; x++) { // The limits on these loops are to mimic a 3x3x3 grid.
//			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					if (x == 0 && x == 0 && z == 0)
						continue; // Skip the centre box.
					
					Vector3 newPosition = new Vector3(); // Create new centre position for one of the surrounding tiles.
					newPosition.x = expandPosition.x + (x *stepSize);
					newPosition.y = expandPosition.y; //+ (y *stepSize);
					newPosition.z = expandPosition.z + (z *stepSize);
					AStarTile temp = new AStarTile(expandTile, newPosition, holePosition); //
					strokeHeuristic(temp);
					neighbours.add(temp);
					//System.out.println("Stepsize " + stepSize);
				}
//			}
		}
		return neighbours;
	}

	
	private void strokeHeuristic(AStarTile temp) {
		if (strokeHeuristic && temp.getParent() != null && temp.getParent().getParent() != null) {
			Vector3 grandparentPos = new Vector3(temp.getParent().getParent().getPosition());
			Vector3 parentPos = new Vector3(temp.getParent().getPosition());
			Vector3 tempPos = new Vector3(temp.getPosition());
			grandparentPos.sub(parentPos);
			parentPos.sub(tempPos);
			if (!grandparentPos.idt(parentPos)) {
				temp.setStrokes((temp.getParent().getStrokeCount()+1));
			}
		}
	}
	
	// Inspects neighbours' validity and object intersection status.
	private void addTiles(HashSet<AStarTile> neighbours) {
		for (AStarTile v : neighbours) {
			// If the current position is already in one of the two sets, just jump to the next position.
			if (isInOpenList(v) || isInClosedList(v))
				continue;
			
			// Create an obstacle representation of the current neighposition +- some leeway.
			BoundingBox boundingBox = buildBoundingBoxAroundPosition(v.getPosition());

			boolean intersectsWithObstacle = false;
			// Then check if this bounding box intersects with any other obstacles.
			for (Obstacle o : obstacleList) {
				// Only add positions, which dont intersect with an obstacle.
				if (collisionDetector.determineIntersection(boundingBox, o.getBoundingBox())) {
					if (o instanceof Hole) {// Then check if said obstacle is the hole (goal).
						hasFoundPath = true;
						lastTile = v;
						lastTile.setPosition(holePosition);
					}
					else {
						intersectsWithObstacle = true;
						break;
					}
				}
			}
			
			if (!intersectsWithObstacle) {
				openList.add(v);
			}
		}
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
		Vector3 min = new Vector3(position.x - stepSize/2, position.y - stepSize/2, position.z - stepSize/2);
		Vector3 max = new Vector3(position.x + stepSize/2, position.y + stepSize/2, position.z + stepSize/2);
		return new BoundingBox(min, max);
	}
	
	
	public void setToNextPosition() {
		if(lastTile == null)
			return;
		
		golfBall.setPosition(lastTile.getPosition());
		lastTile = lastTile.getParent();
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
		float stepSize = gameScreen.getGolfball().getRadius()*2;
		Set<Obstacle> obstacleList = gameScreen.getAllObstacles();
		Iterator<Obstacle> it = obstacleList.iterator();
	
		while(it.hasNext()) {
			Obstacle o = it.next();
			float minObstacleSize = Math.min(o.getBoundingBox().getWidth(), o.getBoundingBox().getHeight());
			minObstacleSize = Math.min(minObstacleSize, o.getBoundingBox().getDepth());
			stepSize = Math.min(minObstacleSize, stepSize);			
		}
//		stepSize = (float) (stepSize*0.99);
//		return stepSize;
		return golfBall.getRadius();
	}

	private List<AStarTile> computeStraightPathFromPosition() {
		List<AStarTile> straightPath = new ArrayList<>();
		if(pathToHole.size() != 0) {
		Vector3 temp = new Vector3(pathToHole.get(0).getPosition());
		Vector3 direction = temp.sub(pathToHole.get(1).getPosition());

		straightPath.add(pathToHole.get(0));
		straightPath.add(pathToHole.get(1));
		
		for(int i = 1; i < pathToHole.size()-1; i++) {
			temp = new Vector3(pathToHole.get(i).getPosition());
			Vector3 subDirection = temp.sub(pathToHole.get(i+1).getPosition());
			if(direction.x <= subDirection.x + 0.5 * stepSize && direction.z <= subDirection.z + 0.5 * stepSize &&
			   direction.x >= subDirection.x - 0.5 * stepSize && direction.z >= subDirection.z - 0.5 * stepSize ) {
				straightPath.add(pathToHole.get(i));
			}
			else if(i == pathToHole.size()-2) {
				straightPath.add(lastTile);
			}
			else {
				break;
			}
		}
		}
		else System.out.println("ERROR");
		return straightPath;
	}
	
	public Vector3 getDirection() { // Direction from the ball to the lastTile
		Vector3 temp = new Vector3(holePosition);
		Vector3 movement = temp.sub(gameScreen.getGolfball().getPosition());
		return movement;
	}
}
