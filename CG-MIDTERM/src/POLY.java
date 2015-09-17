import java.util.ArrayList;
import java.util.List;
import Jama.Matrix;

class Poly {

	private float[] x;
	private float[] y;
	private int n;

	/* Constructor */
	public Poly(float[] x, float[] y, int n) {
		this.x = x.clone();
		this.y = y.clone();
		this.n = n;
	}

	public float[] x() {
		return x;
	}

	public float[] y() {
		return y;
	}

	public int n() {
		return n;
	}

	public int[][] transform(Matrix transformation) {
		// Apply transforation to all vertices
		List<Matrix> matrices = new ArrayList<Matrix>();
		for (int i = 0; i < n; i++) {
			Matrix matrix = new Matrix(new double[][]{ { x[i] },
													   { y[i] },
													   {  1   }});
			//matrix.print(3, 3);
			//transformation.print(3, 3);
			matrices.add(transformation.times(matrix));
		}
		return matricesToPoly(matrices);
	}

	public int[][] matricesToPoly(List<Matrix> matrices) {
		// Round to integer values
        int n = matrices.size();
        int[] nx = new int[x.length];
        int[] ny = new int[y.length];
        for (int i = 0; i < n; i++) {
        	//matrices.get(i).print(3, 3);
            nx[i] = (int) Math.round(matrices.get(i).get(0, 0));
            ny[i] = (int) Math.round(matrices.get(i).get(1, 0));
        }
        return new int[][]{new int[]{n}, nx, ny};
	}

}