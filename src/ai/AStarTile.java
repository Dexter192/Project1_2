package ai;

import com.badlogic.gdx.math.Vector3;

import physics.VectorComputation;

public class AStarTile {

	private float cost, costToTile;
	private Vector3 position;
	private Vector3 goalPosition;
	private AStarTile parent;

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

	public AStarTile(AStarTile parent, Vector3 position, Vector3 goalPosition) {
		this.parent = parent;
		this.position = position;
		this.goalPosition = goalPosition;
		computeCost();
	}

	public AStarTile getParent() {
		return parent;
	}

	public float getCostToTile() {
		return costToTile;
	}
	
	public float getTotalCost() {
		return cost;
	}

	public Vector3 getPosition() {
		return position;
	}

	/**
	 * Compute the cost, which is needed to reach the goal, by adding the distance
	 * made until the tile and the remaining distance to the goal
	 */
	private void computeCost() {
		costToTile = parent.getCostToTile();
		float costToGoal = VectorComputation.getInstance().getDistance(position, goalPosition);
		cost = costToTile + costToGoal;
	}

}
