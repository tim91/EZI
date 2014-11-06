package pl.tstraszewski;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import pl.tstraszewski.util.MatrixElement;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class LSILab4 {
	Matrix M;
	Matrix Q;

	public static void main(String[] args) {
		LSILab4 lsi = new LSILab4();
		lsi.go();
	}

	private void go() {
		// init the matrix and the query
		M = readMatrix("data/data-test.txt");
		Q = readMatrix("data/query-test.txt");

		// print
		System.out.println("Matrix:");
		M.print(3, 2);

		// print the dimensions of the matrix
		System.out.println("M: " + dim(M) + "\n");
		// print the query
		System.out.println("Query:");
		Q.print(3, 2);
		System.out.println("Q: " + dim(Q) + "\n");

		// do svd
		svd();
	}

	private void svd() {
		
		SingularValueDecomposition svd = new SingularValueDecomposition(M);
		// get K, S, and D

		Matrix sMatrix = svd.getS();
		Matrix kMatrix = svd.getU();
		Matrix dMatrixT = svd.getV().transpose();
		
		System.out.println("K: " + dim(kMatrix));
		System.out.println("S: " + dim(sMatrix));
		System.out.println("D: " + dim(dMatrixT) + "\n");
		
		// set number of largest singular values to be considered
		int s = 4;

		System.out.println("s: " + s + "\n");
		
		//get dimension of S matrix
		int size = sMatrix.getColumnDimension();
		List<MatrixElement> dimensions = new ArrayList<MatrixElement>(size);
		for(int i=0; i< size; i++){
			dimensions.add(new MatrixElement(sMatrix.get(i,i), i, i));
		}
		
		Arrays.sort(dimensions.toArray());
		
		//in K matrix cut columns, in D matrix cut rows
		
		if(dimensions.size() > s){
			
			// cut off appropriate columns and rows from K, S, and D
			int[] rows = new int[s];
			int[] cols = new int[s];
			for(int j = 0; j<s; j++){
				rows[j] = dimensions.get(j).row;
				cols[j] = dimensions.get(j).col;
			}
			Matrix sSubM = sMatrix.getMatrix(rows, cols);
			
			
			
			int[] kMatrixRowsTable = new int[kMatrix.getRowDimension()];
			for(int r = 0;r < kMatrix.getRowDimension();r++){
				kMatrixRowsTable[r] = r;
			}
			
			Matrix kSubM = kMatrix.getMatrix(kMatrixRowsTable, cols);
			
			int[] dMatrixColsTable = new int[kMatrix.getColumnDimension()];
			for(int c = 0;c < kMatrix.getColumnDimension();c++){
				dMatrixColsTable[c] =c;
			}
			
			Matrix dSubMT = dMatrixT.getMatrix(rows, dMatrixColsTable);
			
			System.out.print("KS: " + dim(kSubM));
			kSubM.print(3, 2);
			System.out.print("SS: " + dim(sSubM));
			sSubM.print(3, 2);
			System.out.print("DST: " + dim(dSubMT));
			dSubMT.print(3, 2);
			
			
			// transform the query vector
			Matrix qTransformed = Q.transpose().times(kSubM);
			
			
			Matrix sSubMInverted = sSubM.inverse();
			
			qTransformed = qTransformed.times(sSubMInverted);
			
			System.out.print("QS: " + dim(qTransformed));
			qTransformed.print(3, 2);
			

			// compute similaraty of the query and each of the documents, using
			Matrix dSubM = dSubMT.transpose();
			Matrix lengthVectorOfDocs = new Matrix(dSubM.getRowDimension(), 1);
			for(int r=0;r < dSubM.getRowDimension();r++){
				double length = 0;
				for(int c=0; c< dSubM.getColumnDimension(); c++){
					length += Math.pow(dSubM.get(r, c),2.0); 
				}
				lengthVectorOfDocs.set(r, 0, Math.sqrt(length));
			}
			
			double temp = 0;
			for(int c=0;c<qTransformed.getColumnDimension();c++){
				temp += Math.pow(qTransformed.get(0, c),2);
			}
			double lengthOfQuery = Math.sqrt(temp);
			
			Matrix multipleSumVector = new Matrix(dSubM.getRowDimension(), 1);
			
			for(int r=0;r < dSubM.getRowDimension();r++){
				double tempSum=0;
				for(int c=0; c< dSubM.getColumnDimension(); c++){
					tempSum+= dSubM.get(r, c) * qTransformed.get(0, c);
				}
				multipleSumVector.set(r, 0, tempSum);
			}
			
			System.out.println("Similarities:");
			Matrix similarity = new Matrix(multipleSumVector.getRowDimension(),1);
			//similatiry
			for(int r=0; r< multipleSumVector.getRowDimension(); r++){
				double val = multipleSumVector.get(r, 0) / (lengthOfQuery*lengthVectorOfDocs.get(r, 0));
				similarity.set(r, 0, val);
				System.out.println("Doc " + (r+1) + ": " + val);
			}
			
		}
		else{
			//nothing to do
		}
		
		

	}

	// returns the dimensions of a matrix
	private String dim(Matrix M) {
		return M.getRowDimension() + "x" + M.getColumnDimension();
	}

	// reads a matrix from a file
	private Matrix readMatrix(String filename) {
		Vector<Vector<Double>> m = new Vector<Vector<Double>>();
		int colums = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while (br.ready()) {
				Vector<Double> row = new Vector<Double>();
				m.add(row);
				String line = br.readLine().trim();
				StringTokenizer st = new StringTokenizer(line, ", ");
				colums = 0;
				while (st.hasMoreTokens()) {
					row.add(Double.parseDouble(st.nextToken()));
					colums++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int rows = m.size();
		Matrix M = new Matrix(rows, colums);
		int rowI = 0;
		for (Vector<Double> vector : m) {
			int colI = 0;
			for (Double d : vector) {
				M.set(rowI, colI, d);
				colI++;
			}
			rowI++;
		}
		return M;
	}
}