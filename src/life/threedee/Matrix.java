package life.threedee;

public class Matrix{
	private double[][] matrix;
	
	public Matrix(double[][] matrix){
		this.matrix = matrix;
	}
	
	public double _(int x,int y){
		try{
			return matrix[y][x];
		}
		catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			return Double.NaN;
		}
	}
	
	public Matrix submatrixMissingRowAndColumn(int row, int column) {
	    double[][] newMatrix = new double[matrix.length-1][matrix[0].length-1];
	    for (int i = 0; i < matrix.length; i++) {
	        for (int j = 0; j < matrix[0].length; j++) {
	            if (i > row) {
	                if (j > column) {
	                    newMatrix[i-1][j-1]=_(i,j);
	                } else if (j < column) {
	                    newMatrix[i-1][j]=_(i,j);
	                }
	            } else if (i < row) {
	                if (j > column) {
	                    newMatrix[i][j-1]=_(i,j);
	                } else if (j < column) {
	                    newMatrix[i][j]=_(i,j);
	                }
	            }
	        }
	    }
	    return new Matrix(newMatrix);
	}
	
	public double determinant(){
		if (matrix.length != matrix[0].length) {
		    throw new IllegalArgumentException("Not a square matrix");
		}
		if (matrix.length == 1) {
		    return _(0,0);
		} else {
		    double determinantSum = 0;
		    for (int i = 0; i < matrix.length; i++) {
		        determinantSum += _(i,0)*this.submatrixMissingRowAndColumn(i, 0).determinant()*(i%2==0?1:-1);
		    }
		    return determinantSum;
		}
	}
}
