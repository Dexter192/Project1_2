package physics;
public class BicubicSpline {
	
	private Physics physics;
	private float[][] one = 	{{1, 0, 0, 0},
								{0, 0, 1, 0},
								{-3, 3, -2, -1},
								{2, -2, 1, 1}};
	private float[][] three = 	{{1, 0, -3, 2},
								{0, 0, 3, -2},
								{0, 1, -2, 1},
								{0, 0, -1, 1}};

	public BicubicSpline(Physics physics){
		this.physics = physics;
	}

	public float[][] calculatePara(float x0, float x1, float y0, float y1) {
		float[][] valuesF = 	{{physics.getHeightSplines(x0, y0),physics.getHeightSplines(x0, y1),physics.getPartialDerivativeY(y0),physics.getPartialDerivativeY(y1)},
								{physics.getHeightSplines(x1, y0), physics.getHeightSplines(x1, y1),physics.getPartialDerivativeY(y0),physics.getPartialDerivativeY(y1)},
								{physics.getPartialDerivativeX(x0),physics.getPartialDerivativeX(x0),0,0},
								{physics.getPartialDerivativeX(x1),physics.getPartialDerivativeX(x1),0,0}};
		float[][] tmpParameters = multiplicar(one,valuesF);
		float[][] parameters = multiplicar(tmpParameters,three);
		return parameters;
	}
	public float calculateHeight(float x, float y, float x0, float x1, float y0, float y1) {
		float[][] para = calculatePara(x0,x1,y0,y1);
		float height = 0;
		for(int i = 0;i<3;i++){
			for (int j = 0;j<3;j++){
				height = height+(float)(para[i][j]*Math.pow(x,i)*Math.pow(y,j));
			}
		}
		return height;
	}
	public float[][] multiplicar(float[][] A, float[][] B) {

		int aRows = A.length;
		int aColumns = A[0].length;
		int bRows = B.length;
		int bColumns = B[0].length;

		if (aColumns != bRows) {
			throw new IllegalArgumentException("A:Rows: " + aColumns + " did not match B:Columns " + bRows + ".");
		}

		float[][] C = new float[aRows][bColumns];
		for (int i = 0; i < aRows; i++) {
			for (int j = 0; j < bColumns; j++) {
				C[i][j] = 0.00000f;
			}
		}

		for (int i = 0; i < aRows; i++) { // aRow
			for (int j = 0; j < bColumns; j++) { // bColumn
				for (int k = 0; k < aColumns; k++) { // aColumn
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}

		return C;
	}
}