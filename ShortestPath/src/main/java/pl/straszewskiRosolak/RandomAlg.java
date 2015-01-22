package pl.straszewskiRosolak;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomAlg extends ZachlannyALg implements Algorithm {

	Random r = new Random();

	@Override
	public int solve(Instance ins, int startVertex) {

		int sum = 0;
		Set<Integer> saw = new HashSet<Integer>();
		int prev = startVertex;
		for (int i = 1; i < ins.getData().size(); i++) {
			int next = randVertex(ins, saw);
			sum += ins.getDistanceMatrix()[prev][next];
			prev = next;
		}
		return sum;
	}

	private int randVertex(Instance ins, Set<Integer> saw) {
		int rr = -1;
		do {
			rr = r.nextInt(ins.getData().size());

		} while (saw.contains(rr));
		return rr;
	}
}
