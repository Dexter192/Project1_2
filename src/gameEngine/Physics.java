package gameEngine;

public class Physics {
	private float[] array1;
	private float[] array2;
	
	public Physics(float[] a, float[] b) {
		array1 = a;
		array2 = b;
	}
	
	public float getHeight(double x, double y) {
		float height = 0;
		for(int i = 0; i < array1.length; i++) {
			height += array1[i]*(Math.pow(x, array1.length-1-i));	
		}
		for(int j = 0; j < array2.length; j++) {
			height += array1[j]*(Math.pow(y, array2.length-1-j));
		}

		return height;
	}
}
