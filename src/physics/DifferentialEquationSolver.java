package physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class DifferentialEquationSolver {
		private float mass;
		private float g = 9.81f;
		private Physics boardFunction;
		private float frictionConstant = 1; // TODO implement proper friction
		private Vector3 position;
		private DifferentialEquationSolver ode;
		
		
		public DifferentialEquationSolver(Physics physics, float mass) {
			boardFunction = physics;
			this.mass = mass;
		}
		
		
		public Vector3[] rungeKutterMethod(Vector3[] initial, Vector3 pos) {

			position = pos;
			float deltaTime = 0.21f;
			Vector3[] k1 =  calc(0,initial);
			scl(k1,deltaTime);
			
			float newt = (deltaTime/3);

			Vector3[] neww = {add(initial[0],(k1[0].scl(1/3))),add(initial[1],(k1[1].scl(1/3)))};

			Vector3[] k2 = 	calc(newt, neww );
			scl(k2,deltaTime);
			
			newt = (2*deltaTime)/3;
			neww[0] = add((sub(initial[0],(k1[0].scl(1/3f)))),k2[0]);
			neww[1] = add((sub(initial[1],(k1[1].scl(1/3f)))),k2[1]);
			Vector3[] k3 = calc(newt, neww);
			scl(k3,deltaTime);
			
			newt = deltaTime;
			neww[0] = add(sub((add(initial[0],k1[0])),k2[0]),k3[0]);
			neww[1] = add(sub((add(initial[1],k1[1])),k2[1]),k3[1]);
			Vector3[] k4 = calc(newt, neww);
			scl(k4,deltaTime);
			
			Vector3[] k = {add(add(add(k1[0],(k2[0].scl(3f))),(k3[0].scl(3f))),k4[0]),add(add(add(k1[1],(k2[1].scl(3f))),(k3[1].scl(3f))),k4[1])};
			Vector3[] result = new Vector3[2];
			result[0] = add(initial[0],k[0].scl(1/8f));
			result[1] = add(initial[1],k[1].scl(1/8f));
			
			
			System.out.println(" end acceleration x: " + result[1].toString());
			System.out.println(" end velocoty x " + result[0].toString());
			return result;
		}
		
		private Vector3[] calc(float time, Vector3[] veloAccel) {
			Vector3[] tempva = {new Vector3(), new Vector3()}; 
			
			float gravitationalForce_x = -mass*g*boardFunction.getPartialDerivativeX(position.x);
			float frictionalForce_x= (float) ((-frictionConstant*g*veloAccel[0].x)/ Math.sqrt((veloAccel[0].x * veloAccel[0].x) + (veloAccel[0].z *veloAccel[0].z))); 
			tempva[1].x = (gravitationalForce_x + frictionalForce_x)/mass;

			float gravitationalForce_z = -mass*g*boardFunction.getPartialDerivativeY(position.z);
			float frictionalForce_z= (float) ((-frictionConstant*g*veloAccel[0].z)/ Math.sqrt((veloAccel[0].x * veloAccel[0].x) + (veloAccel[0].z *veloAccel[0].z))); 
			tempva[1].z = (gravitationalForce_z + frictionalForce_z)/mass;

			tempva[0].x = time * tempva[1].x;
			tempva[0].z = time * tempva[1].z;

			return tempva;
		}
		
		private Vector3 add (Vector3 a, Vector3 b) {
			Vector3  c = new Vector3(0,0,0);
			c.x = a.x + b.x;
			c.y = a.y + b.y;
			c.z = a.z + b.z;
			return c;
		}
		private Vector3 sub (Vector3 a, Vector3 b) {
			Vector3  c = new Vector3(0,0,0);
			c.x = a.x - b.x;
			c.y = a.y - b.y;
			c.z = a.z - b.z;
			return c;
		}
		private Vector3[] scl (Vector3[] matrix, float scalar) {
			for(int i = 0; i < matrix.length; i++) {
				matrix[i].scl(scalar);
			}
			return matrix;
		}
}