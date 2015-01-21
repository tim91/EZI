package pl.straszewskiRosolak;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ZachlannyALg implements Algorithm {

	Random r = new Random();

	@Override
	public int solve(Instance instance) {
		int minStartVertex = 0;
		int minLength = Integer.MAX_VALUE;
		for (int i = 0; i < instance.getData().size(); i++) {
			int length = solve(instance, i);
			if (length < minLength) {
				minStartVertex = i + 1;
				minLength = length;
			}
		}
		System.out.println("Min vertex index: " + minStartVertex);
		return minLength;
	}

	public int solve(Instance i, int startVertex) {
		Integer sum = 0;
		Set<Integer> saw = new HashSet<Integer>();
		saw.add(startVertex);
		int nn = startVertex;
		while (saw.size() != i.getData().size()) {
			int[] result = findMinV(i, nn, saw);
			int found = result[0];
			sum += result[1];
			saw.add(found);
			nn = found;
		}
		sum += i.getDistanceMatrix()[startVertex][nn];
		return sum;
	}

	protected int[] findMinV(Instance i, int v, Set<Integer> saw) {

		int min = Integer.MAX_VALUE;
		int minV = -1;
		for (int j = 0; j < i.getData().size(); j++) {

			if (saw.contains(j)) {
				continue;
			}

			int dis = i.getDistanceMatrix()[v][j];
			if (dis < min) {
				min = dis;
				minV = j;
			}
		}
		return new int[] { minV, min };
	}

	protected int findMinV(Instance i, int v) {
		Set<Integer> saw = new HashSet<Integer>();
		saw.add(v);
		return findMinV(i, v, saw)[0];
	}
}
