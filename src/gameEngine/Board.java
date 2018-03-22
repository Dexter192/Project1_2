package gameEngine;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector3;
public class Board {

	private Physics physics;
	private HashSet<Terrain> terrainList = new HashSet<>();
	private int height = 50, width = 50;
	private float highestHeight ;
	
	public Board() {

		float[] a = {0.2f,0};
		float[] b = {-2,0};
		physics = new Physics(a,b);
		highestHeight = getHighestHeight();

		//frictionConstant = 10
		
		for(int i = 100; i <= 850; i+=height){
			for(int j = 0;j <= 1000; j+=width) {
				Terrain terrain = new Terrain(new Vector3(i,j, getHeight(i,j)), width, height, highestHeight);
				terrainList.add(terrain);
			}
	    }

	}

	public float getHighestHeight() {
		float best = 0;
		for(int i = 100; i <= 850; i+=height){
			for(int j = 0;j <= 1000; j+=width) {
				if(getHeight(i,j) > best) best = getHeight(i,j);
			}
	    }
		return best;
	}
	public float getHeight(double x, double y) {
		return physics.getHeight(x, y);
	}
	public HashSet<Terrain> getTerrain() {
		return terrainList;
	}
	/*public float getHeight(float x, float y) {
		Vector3 a = new Vector3(x,y,0);
		Terrain current = getTileOn(a);
		return current.getPosition().z;	
	}*/
	public Terrain getTileOn(Vector3 position) {
		Terrain tileOn = null; 
		for(Terrain t : terrainList) { 
			if(t.getPosition().x <= position.x &&
			   t.getPosition().x + t.getWidth() >= position.x &&
			   t.getPosition().y <= position.y &&
			   t.getPosition().y + t.getHeight() >= position.y) {
				tileOn = t;
				break;
			}
		}
		return tileOn;
	}
	public Physics getPhysics() {
		return physics;
	}
}


