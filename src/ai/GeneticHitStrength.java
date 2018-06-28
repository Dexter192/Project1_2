package ai;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.math.Vector3;

import physics.DifferentialEquationSolver;
import physics.VectorComputation;

public class GeneticHitStrength {
	
	private static String path = System.getProperty("user.dir")+"\\src\\ai\\hitStrength.txt";
	private float strengthPerUnit = 0; 
	private DifferentialEquationSolver ode;
	
	public GeneticHitStrength(DifferentialEquationSolver ode) {
		this.ode = ode;
	}
	
	public Vector3 getHitStrength(Vector3 startPosition, Vector3 goalPosition) {
		strengthPerUnit = 1f;//readStrengthPerUnit();
		Vector3 temp = new Vector3(goalPosition);
		Vector3 direction = new Vector3(temp.sub(startPosition)).scl(0.1f);
		System.out.println("start " + direction);
		//direction.nor();
		direction.y = 0;
		float distance = VectorComputation.getInstance().getDistanceXZ(startPosition, goalPosition);
		/*direction.y = 0;
		System.out.println( "after nor" +direction);
//		direction.scl(distance);
		direction.scl(strengthPerUnit*distance);
		System.out.println(" scaled" + direction);*/
		Vector3 simPos = simulateShot(startPosition, goalPosition, direction);
		float dis = VectorComputation.getInstance().getDistanceXZ(simPos, goalPosition);
		while(dis > 0.5f) {
			updateStrengthPerUnit(startPosition, goalPosition, simPos);
			direction.scl(strengthPerUnit);
			simPos = simulateShot(startPosition, goalPosition, direction);
			dis = VectorComputation.getInstance().getDistanceXZ(simPos, goalPosition);
//			System.out.println(dis);
		}
	
		
		return direction;
	}
	public Vector3 simulateShot(Vector3 start, Vector3 goal, Vector3 direct) {
		Vector3 pos = new Vector3(start);
		Vector3[] veloAccel = {direct, new Vector3(0,0,0)};
		while(!veloAccel[0].isZero()) {
		veloAccel = ode.rungeKutterMethod(veloAccel, pos);
		pos= ode.nextPos(veloAccel, pos);
		veloAccel = ignoreMinimalVelocity(veloAccel);
//		System.out.println(veloAccel[0]);
		float actualDist =VectorComputation.getInstance().getDistanceXZ(pos, start);
		float intendedDist =VectorComputation.getInstance().getDistanceXZ(goal, start);
		if(actualDist > intendedDist+100) {
//			break;
		}
		}
		return (pos);
		
	}
	
	private Vector3[] ignoreMinimalVelocity(Vector3[] veloAccel) {
		if (Math.abs(veloAccel[0].x) <= 0.01) {
			veloAccel[0].x = 0;
			veloAccel[1].x = 0;
		}
		if (Math.abs(veloAccel[0].z) <= 0.01) {
			veloAccel[0].z = 0;
			veloAccel[1].z = 0;
		}
		if(Math.abs(veloAccel[0].y) <= 0.01) {
			veloAccel[1].y = 0;
			veloAccel[0].y = 0;
		}
		return veloAccel;
	}
	
	private float readStrengthPerUnit()  {
		File f = new File(path);
		if(!f.exists()) {
			return (float) 0.01f;
		}
		Scanner s;
		try {
			s = new Scanner(f);
			String strength = s.nextLine();
			s.close(); //
			return Float.parseFloat(strength);
		} catch (FileNotFoundException e) {
			return (float) 0.01;
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
		
		if(supposedDist < actualDist) {
			strengthPerUnit = (float) (strengthPerUnit*0.999);
		}
		else if (supposedDist > actualDist) {
			strengthPerUnit = (float) (strengthPerUnit*1.001);
		}
		try {
			writeStrengthPerUnit();
		} catch (IOException e) {
			return;
		}
	}
	
}
