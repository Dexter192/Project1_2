import com.badlogic.gdx.math.Vector2;

public class AABB2D // Axis Aligned Bounding Boxes.
{
	// Properties.
	private Vector2 low;
	private Vector2 high;
	public int terrain;
	
	public AABB2D()
	{
		low = new Vector2();
		high = new Vector2();
		terrain = 0; // Different integers represent different terrains.
	}
	
	
	public AABB2D(Vector2 l, Vector2 h)
	{
		low = l;
		high = h;
		terrain = 0;
	}
	
	
	public AABB2D(int lx, int ly, int hx, int hy)
	{
		low = new Vector2((float) lx, (float) ly);
		high = new Vector2((float) hx, (float) hy);
		terrain = 0;
	}
	
	
	// Public get() and set().
	public Vector2 getLow()	{	return low;	}
	
	
	public Vector2 getHigh()	{	return high;	}
	
	
	public void setGrass()	{	terrain = 0;	}
	
	
	public void setSand()	{	terrain = 1;	}
	
	
	public void setWater()	{	terrain = 2;	}
	
	
	public void setObstacle()	{	terrain = 3;	}
	
	
	// Intersection Methods.
	public boolean intersectingAABB(AABB2D other)
	{
		boolean aladeenHere = false;
		
		Vector2 distance1 = new Vector2();
		distance1.add(this.getLow().sub(other.getHigh()));
		
		Vector2 distance2 = new Vector2();
		distance2.add(other.getLow().sub(this.getHigh()));
		
		Vector2 checking = new Vector2();
		
		if(distance1.x > distance2.x)
			checking.x = distance1.x;
		else checking.x = distance2.x;
		
		if(distance1.y > distance2.y)
			checking.y = distance1.y;
		else checking.y = distance2.y;
		
		System.out.println("checking has the values: " + checking.x + ", " + checking.y + ".");
		
		if(checking.x < 0 || checking.y < 0)
			aladeenHere = true;
		return aladeenHere; 
	}
	
	
	public boolean intersectingBS(BoundingSphere2D other)
	{
		boolean aladeenHere = false;
		float distance1 = other.getCentre().dst(this.getHigh());
		
		float distance2 = other.getCentre().dst(this.getLow());
		
		System.out.println("distance1 has the value: " + distance1 + ", and distance2 has the value:" + distance2 + ".");
		
		if(distance1 < other.getRadius() || distance2 < other.getRadius())
			aladeenHere = true;
		return aladeenHere;
	}
}
