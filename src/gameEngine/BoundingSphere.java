package gameEngine;
import com.badlogic.gdx.math.Vector3;

public class BoundingSphere 
{
	// Properties
	private Vector3 centre;
	private int radius;
	
	
	public BoundingSphere(int r)
	{
		radius = r;
		centre = new Vector3();
	}
	
	
	public BoundingSphere(int r, Vector3 c)
	{
		radius = r;
		centre = c;
	}
	
	
	public BoundingSphere(int r, int x, int y, int z)
	{
		radius = r;
		centre = new Vector3((float) x, (float) y, (float) z);
	}
	
	
	// Public get() and set().
	public int getRadius()	{	return radius;	}
	
	
	public Vector3 getCentre()	{	return centre;	}
	
	
	// Intersection Methods.
	public boolean intersectingBS(BoundingSphere other)
	{
		int radiusTotal = this.getRadius() + other.getRadius();
		float centreDistance = this.getCentre().dst(other.getCentre());
		
		if (centreDistance < radiusTotal) return true;
		else return false;
	}
	
	
	public boolean intersectingAABB(AABB other)
	{
		boolean aladeenHere = false;
		float distance1 = this.getCentre().dst(other.getHigh());
		float distance2 = this.getCentre().dst(other.getLow());
		
		System.out.println("distance1 has the value: " + distance1 + ", and distance2 has the value:" + distance2 + ".");
		
		if(distance1 < this.getRadius() || distance2 < this.getRadius())
			aladeenHere = true;
		return aladeenHere;
	}
}
