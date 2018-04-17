package Physiks;

import com.badlogic.gdx.math.Vector3;

public class VectorComputation {

    private static final VectorComputation instance = new VectorComputation();
    
    /**
     * Private constructor to create a initiate the singleton instance 
     * of the vector computation class.
     * We can negitiate the fact, that creating a singleton in this way
     * will always create an instance of it, since the application
     * will always use vector computation and because it is thread safe
     */
    private VectorComputation(){}

    public static VectorComputation getInstance(){
        return instance;
    }
    
    public float getDistance(Vector3 pointA, Vector3 pointB) {
    	double x = Math.pow(pointA.x-pointB.x, 2);
    	double y = Math.pow(pointA.y-pointB.y, 2);
    	double z = Math.pow(pointA.z-pointB.z, 2);
    	    	
    	double distance = Math.sqrt(x+y+z);
    	
    	return (float)distance;
    }
    
    /**
     * Computes the euclidian distance between two points (in a 3 dimensional space)<br>
     * <b>CAREFUL!</b> This method ignores the y vector (i.e. the height)
     * @param pointA 
     * @param pointB
     * @return the euclidian Distance between the points
     */
    public float getDistanceXZ(Vector3 pointA, Vector3 pointB) {
    	double x = Math.pow(pointA.x-pointB.x, 2);
    	double z = Math.pow(pointA.z-pointB.z, 2);
    	    	
    	double distance = Math.sqrt(x+z);
    	
    	return (float)distance;
    }
	
}
