import com.badlogic.gdx.math.Vector3;

public class AABB // Axis Aligned Bounding Boxes.
{
	// Properties.
	private Vector3 low;
	private Vector3 high;
	
	
	public AABB()
	{
		low = new Vector3();
		high = new Vector3();
	}
	
	
	public AABB(Vector3 l, Vector3 h)
	{
		low = l;
		high = h;
	}
	
	
	public AABB(int lx, int ly, int lz, int hx, int hy, int hz)
	{
		low = new Vector3((float) lx, (float) ly, (float) lz);
		high = new Vector3((float) hx, (float) hy, (float) hz);
	}
	
	
	// Public get() and set().
	public Vector3 getLow()	{	return low;	}
	
	
	public Vector3 getHigh()	{	return high;	}
	
	
	// Intersection Methods.
	public boolean intersectingAABB(AABB other)
	{
		//float distance1 = this.getLow().dst(other.getHigh());
		boolean aladeenHere = false;
		
		Vector3 distance1 = new Vector3();
		distance1.add(this.getLow().sub(other.getHigh()));
		
		Vector3 distance2 = new Vector3();
		distance2.add(other.getLow().sub(this.getHigh()));
		
		Vector3 checking = new Vector3();
		
		if(distance1.x > distance2.x)
			checking.x = distance1.x;
		else checking.x = distance2.x;
		
		if(distance1.y > distance2.y)
			checking.y = distance1.y;
		else checking.y = distance2.y;
		
		if(distance1.z > distance2.z)
			checking.z = distance1.z;
		else checking.z = distance2.z;
		
		System.out.println("checking has the values: " + checking.x + ", " + checking.y + ", " + checking.z + ".");
		
		if(checking.x < 0 || checking.y < 0 || checking.z < 0)
			aladeenHere = true;
		return aladeenHere; 
	}
	
	
	public boolean intersectingBS(BoundingSphere other)
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
