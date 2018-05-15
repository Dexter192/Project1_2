package physics;

public class Physics {
	private float[] array1;
	private float[] array2;

	public Physics(float[] a, float[] b) {
		array1 = a;
		array2 = b;
	}

	public float[] getDerivative(float[] array) {
		float[] derivative = new float[array.length - 1];
		for (int i = 0; i < array.length - 1; i++)
			derivative[i] = array[i] * (array.length - i - 1);
		return derivative;
	}

	public float getPartialDerivativeY(float y) {
		float ans = 0;
		float[] array = getDerivative(array2);
		for (int i = 0; i < array.length; i++) {
			ans += array[i] * (Math.pow(y, array.length - 1 - i));
		}
		return ans;
	}

	public float getPartialDerivativeX(float x) {
		float ans = 0;
		float[] array = getDerivative(array1);
		for (int i = 0; i < array.length; i++) {
			ans += array1[i] * (Math.pow(x, array.length - 1 - i));
		}
		return ans;
	}

	public float getHeight(double x, double y) {
		float height = 0;
		for (int i = 0; i < array1.length; i++) 
			height += array1[i] * (Math.pow(x, array1.length - 1 - i));
		
		for (int j = 0; j < array2.length; j++) 
			height += array1[j] * (Math.pow(y, array2.length - 1 - j));
		
		return height;
	}

}
