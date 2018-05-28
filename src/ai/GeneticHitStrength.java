package ai;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import com.badlogic.gdx.math.Vector3;

import physics.VectorComputation;

public class GeneticHitStrength {
	
	private static String path = System.getProperty("user.dir")+"\\src\\ai\\hitStrength.txt";
	private float strengthPerUnit = 0; 
	
	public GeneticHitStrength() {
		
	}
	
	public Vector3 getHitStrength(Vector3 startPosition, Vector3 goalPosition) {
		strengthPerUnit = readStrengthPerUnit();
		Vector3 temp = new Vector3(goalPosition);
		Vector3 direction = new Vector3(temp.sub(startPosition));
		direction.nor();
		float distance = VectorComputation.getInstance().getDistanceXZ(startPosition, goalPosition);
		direction.scl(strengthPerUnit*distance);
		return direction;
	}
	
	private float readStrengthPerUnit()  {
		File f = new File(path);
		if(!f.exists()) {
			return (float) 0.001;
		}
		Scanner s;
		try {
			s = new Scanner(f);
			String strength = s.nextLine();
			s.close();
			return Float.parseFloat(strength);
		} catch (FileNotFoundException e) {
			return (float) 0.001;
		}
	}

	private void writeStrengthPerUnit() throws IOException {
		File f = new File(path);
		if(!f.exists()) {
			f.createNewFile();
		}
		FileWriter w = new FileWriter(f);
		w.write("" + strengthPerUnit);
		w.close();
	}

	public void updateStrengthPerUnit(Vector3 startPosition, Vector3 goalPosition, Vector3 actualPosition) {

		float supposedDist = VectorComputation.getInstance().getDistanceXZ(startPosition, goalPosition);
		float actualDist = VectorComputation.getInstance().getDistanceXZ(startPosition, actualPosition);
		
		
		//TODO: This is wrong. It has to include the direction which we were going. 
		
		if(supposedDist < actualDist) {
			strengthPerUnit = (float) (strengthPerUnit * 0.9);
		}
		else if (supposedDist > actualDist) {
			strengthPerUnit = (float) (strengthPerUnit * 1.1);
		}
		try {
			writeStrengthPerUnit();
		} catch (IOException e) {
			return;
		}
	}
	
}
