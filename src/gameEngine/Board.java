package gameEngine;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.math.Vector3;
public class Board {

	private Physics physics;
	private HashSet<Terrain> terrainList = new HashSet<>();
	private int height = 50, width = 50;
	
	public Board() {
		float[] a = {1,0};
		float[] b = {-1,0};
		physics = new Physics(a,b);

		for(int i = 100; i <= 900; i+=height){
			for(int j = 0;j <= 900; j+=width) {
				Terrain terrain = new Terrain(new Vector3(i,j, getHeight(i,j)), width, height);
				terrainList.add(terrain);
			}
	    }
	}
	public float getHeight(double x, double y) {
		return physics.getHeight(x, y);
	}
	public HashSet<Terrain> getTerrain() {
		return terrainList;
	}
	
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
	
}


