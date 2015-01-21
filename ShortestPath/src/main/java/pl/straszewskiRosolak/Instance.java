package pl.straszewskiRosolak;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Instance {

	List<int[]> data;

	public Instance(List<int[]> data) {
		super();
		this.data = data;
	}

	public List<int[]> getData() {
		return data;
	}

	int[][] distanceMatrix;

	public int[][] getDistanceMatrix() {
		return distanceMatrix;
	}

	public void createDistanceMatrix() {
		distanceMatrix = new int[this.data.size()][this.data.size()];

		for (int i = 0; i < this.data.size(); i++) {
			for (int j = i; j < this.data.size(); j++) {
				if (i == j) {
					distanceMatrix[i][j] = 0;
				} else {
					int[] a = data.get(i);
					int[] b = data.get(j);
					distanceMatrix[i][j] = countEuclidianDistance(a, b);
					distanceMatrix[j][i] = distanceMatrix[i][j];
				}
			}
		}
	}

	public void printDistanceMatrix() {
		for (int i = 0; i < this.distanceMatrix.length; i++) {
			System.out.println(Arrays.toString(this.distanceMatrix[i]));
		}

	}

	private int countEuclidianDistance(int[] a, int[] b) {
		int aa = a[0] - b[0];
		int bb = a[1] - b[1];

		double s = aa * aa + bb * bb;
		s = Math.sqrt(s);
		return Math.round((float) s);
	}

	public static Instance getInstance(String path) throws Exception {
		InputStream fis = new FileInputStream(path);
		return getInstance(fis);
	}

	public static Instance getInstance(InputStream is) throws Exception {
		String line;
		List<int[]> listOfArr = new ArrayList<int[]>();

		InputStreamReader isr = new InputStreamReader(is,
				Charset.forName("UTF-8"));
		BufferedReader br = new BufferedReader(isr);
		int lineNr = 0;

		while ((line = br.readLine()) != null) {
			if (line.equals("EOF")) {
				break;
			}
			if (lineNr > 5) {
				String[] ll = line.split(" ");
				int[] a = new int[2];
				a[0] = Integer.parseInt(ll[1]);
				a[1] = Integer.parseInt(ll[2]);
				listOfArr.add(a);
			}
			lineNr++;
		}
		return new Instance(listOfArr);
	}
}
