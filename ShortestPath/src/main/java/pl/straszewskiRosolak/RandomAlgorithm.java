package pl.straszewskiRosolak;

import java.util.HashSet;
import java.util.Set;

public class RandomAlgorithm extends Algorithm {

	@Override
	public int solve(Instance ins, int startVertex) {

		int sum = 0;
		Set<Integer> saw = new HashSet<Integer>();
		int prev = startVertex;
		for (int i = 1; i < ins.getData().size(); i++) {
			int next = Utils.getRandomVertex(ins, saw,r);
			sum += ins.getDistanceMatrix()[prev][next];
			prev = next;
		}
		return sum;
	}

}
