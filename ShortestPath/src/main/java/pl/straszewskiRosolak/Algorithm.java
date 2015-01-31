package pl.straszewskiRosolak;

import java.util.Random;

public abstract class Algorithm {

	protected Random r = new Random();
	
	public int solve(Instance instance){
		
		int minLength = Integer.MAX_VALUE;
		int maxLength = Integer.MIN_VALUE;
		int sum = 0;
		for (int i = 0; i < instance.getData().size(); i++) {
			int length = solve(instance, i);
			if (length < minLength) {
				minLength = length;
			}
			if (length > maxLength) {
				maxLength = length;
			}
			sum += length;
		}
		double avg = (double) sum / (double) instance.getData().size();
		System.out.println("Min: " + minLength);
		System.out.println("Max: " + maxLength);
		System.out.println("Avg: " + avg);
		return minLength;
	}

	public abstract int solve(Instance i, int startVertex);
	
}
