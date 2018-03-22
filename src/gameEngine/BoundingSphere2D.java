package gameEngine;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

public class BoundingSphere2D 
{
	// Properties
	private Vector2 centre;
	private int radius;
	static private ArrayList<BoundingSphere2D> sphereList = new ArrayList<BoundingSphere2D>();
	static private int counter = 0;
	private int myNumber;
	
	
	public BoundingSphere2D(int r)
	{
		radius = r;
		centre = new Vector2();
		sphereList.add(this);
		myNumber = counter;
		counter++;
	}
	
	
	public BoundingSphere2D(int r, Vector2 c)
	{
		radius = r;
		centre = c;
		sphereList.add(this);
		myNumber = counter;
		counter++;
	}
	
	
	public BoundingSphere2D(int r, int x, int y)
	{
		radius = r;
		centre = new Vector2((float) x, (float) y);
		sphereList.add(this);
		myNumber = counter;
		counter++;
	}
	
	
	// Public get() and set().
	public int getRadius()	{	return radius;	}
	
	
	public Vector2 getCentre()	{	return centre;	}
	
	
	public ArrayList<BoundingSphere2D> getSphereList()	{	return sphereList;	}
	
	
	public int getMyNumber()	{	return myNumber;	}
	
	
	// Intersection Methods.
	public boolean intersectingBS(BoundingSphere2D other)
	{
		int radiusTotal = this.getRadius() + other.getRadius();
		float centreDistance = this.getCentre().dst(other.getCentre());
		
		if (centreDistance < radiusTotal) return true;
		else return false;
	}
	
	
	public boolean intersectingAABB(AABB2D other)
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
