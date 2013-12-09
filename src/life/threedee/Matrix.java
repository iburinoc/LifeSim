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
	
	public double determinant(){
		if(!(matrix.length == 2 && matrix[0].length == 2)){
			throw new IllegalArgumentException("Not a 2x2 matrix");
		}
		
		return _(0,0) * _(1,1) - _(1,0) * _(0,1);
	}
}
