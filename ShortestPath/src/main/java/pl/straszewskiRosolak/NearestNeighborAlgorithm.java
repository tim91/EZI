package pl.straszewskiRosolak;

import java.util.HashSet;
import java.util.Set;

public class NearestNeighborAlgorithm extends Algorithm
{
	@Override
	public int solve(Instance i, int startVertex) {
		Integer sum = 0;
		Set<Integer> saw = new HashSet<Integer>();
		saw.add(startVertex);
		int nn = startVertex;
		while (saw.size() != i.getData().size()) {
			int[] result = Utils.findClosestVertex(i, nn, saw);
			int found = result[0];
			sum += result[1];
			saw.add(found);
			nn = found;
		}
		sum += i.getDistanceMatrix()[startVertex][nn];
		return sum;
	}


}
