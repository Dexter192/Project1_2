package physics;

public class Physics {
	private float[] array1;
	private float[] derArray1;
	private float[] array2;
	private float[] derArray2;
	private BicubicSpline bicubicSpline;
	private float diff;
	
	public Physics(float[] a, float[] b) {
		array1 = a;
		derArray1 = getDerivative(a);
		array2 = b;
		derArray2 = getDerivative(b);
		bicubicSpline = new BicubicSpline(this);
		diff = 1f;
	}

	public float[] getDerivative(float[] array) {
		float[] derivative = new float[array.length - 1];
		for (int i = 0; i < array.length - 1; i++)
			derivative[i] = array[i] * (array.length - i - 1);
		return derivative;
	}

	public float getPartialDerivativeY(float y) {
		float ans = 0;
		for (int i = 0; i < derArray2.length; i++) {
			ans += derArray2[i] * (Math.pow(y, derArray2.length - 1 - i));
		}
		return ans;
	}

	public float getPartialDerivativeX(float x) {
		float ans = 0;
		for (int i = 0; i < derArray1.length; i++) {
			ans += derArray1[i] * (Math.pow(x, derArray1.length - 1 - i));
		}
		return ans;
	}

	
	/*public float getHeight(double x, double y) {
		float height = 0;
		for (int i = 0; i < array1.length; i++) 
			height += array1[i] * (Math.pow(x, array1.length - 1 - i));
		
		for (int j = 0; j < array2.length; j++) 
			height += array2[j] * (Math.pow(y, array2.length - 1 - j));
		
		return height;
	}*/
	
	///*
	public float getHeight(double x, double y) {
		int tmp = (int) (x/diff);
		float x0 = tmp*diff;
		float x1 = (x0 + diff);
		tmp = (int) (y/diff);
		float y0 = tmp*diff;
		float y1 = y0 + diff;
		return bicubicSpline.calculateHeight((float) x, (float) y, x0, x1, y0, y1);
	}
	public float getHeightSplines(double x, double y) {
		float height = 0;
		for (int i = 0; i < array1.length; i++) 
			height += array1[i] * (Math.pow(x, array1.length - 1 - i));
		
		for (int j = 0; j < array2.length; j++) 
			height += array2[j] * (Math.pow(y, array2.length - 1 - j));
		
		return height;
	}
	//*/
}
