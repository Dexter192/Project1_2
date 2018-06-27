package ai;

import com.badlogic.gdx.math.Vector3;

import physics.VectorComputation;

public class AStarTile {
	private float cost, costToTile; // Smart heuristic and travelled distance.
	private Vector3 position;
	private Vector3 goalPosition;
	private AStarTile parent; // Previous tile.
	private int strokeCount = 1;

	/**
	 * Constructor for the initial position
	 * 
	 * @param position
	 */
	public AStarTile(Vector3 position, Vector3 goalPosition) {
		this.position = position;
		cost = VectorComputation.getInstance().getDistance(position, goalPosition);
		costToTile = 0;
	}

	// Constructor for intermediate tiles.
	public AStarTile(AStarTile parent, Vector3 position, Vector3 goalPosition) {
		this.parent = parent;
		this.position = position;
		this.goalPosition = goalPosition;
		this.strokeCount = parent.getStrokeCount();
		computeCost();
	}

	/**
	 * 
	 * Compute the cost, which is needed to reach the goal, by adding the distance
	 * made until the tile and the remaining distance to the goal
	 */
	private void computeCost() {
		costToTile = parent.getCostToTile()
				+ VectorComputation.getInstance().getDistance(position, parent.getPosition()); // Travelled Distance.
		float costToGoal = VectorComputation.getInstance().getDistance(position, goalPosition); // Smart heuristic.
		cost = costToTile + costToGoal; // Sum.
	}

	public AStarTile getParent() {
		return parent;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public float getCostToTile() {
		return costToTile;
	}

	public float getTotalCost() {
		if (AStar.strokeHeuristic && this.parent != null) {
			computeCost();
			if(this.getParent().getStrokeCount() != strokeCount) {
				cost = cost * strokeCount;
			}
		}
		return cost;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setStrokes(int strokeCount) {
		this.strokeCount = strokeCount;
	}
	
	public int getStrokeCount() {
		return strokeCount;
	}
}
