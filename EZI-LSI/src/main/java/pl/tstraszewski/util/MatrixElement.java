package pl.tstraszewski.util;

public class MatrixElement implements Comparable<MatrixElement> {

	public double value;
	public int row;
	public int col;
	
	public MatrixElement(double value, int rowIdx, int colIdx) {
		this.value = value;
		this.col = colIdx;
		this.row = rowIdx;
	}
	
	public int compareTo(MatrixElement o) {

		return (int)(value - o.value);
		
	}

	@Override
	public String toString() {
		return "MatrixElement [value=" + value + ", row=" + row + ", col="
				+ col + "]";
	}
	
}
