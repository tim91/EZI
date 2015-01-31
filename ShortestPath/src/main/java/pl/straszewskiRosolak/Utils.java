package pl.straszewskiRosolak;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Utils {

	public static int[] findClosestVertex(Instance i, int v, Set<Integer> saw) {

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

	public static int findClosestVertex(Instance i, int v) {
		Set<Integer> saw = new HashSet<Integer>();
		saw.add(v);
		return findClosestVertex(i, v, saw)[0];
	}
	
	public static int getRandomVertex(Instance ins, Set<Integer> saw, Random r) {
		
		int rr = -1;
		do {
			rr = r.nextInt(ins.getData().size());

		} while (saw.contains(rr));
		return rr;
	}
	
	public static int getRandomVertex(Instance ins, List<Integer> saw, Random r) {
		return getRandomVertex(ins, new HashSet<Integer>(saw), r);
	}
	
	public static List<Integer> revert(List<Integer> bestSolution, int start, int end) {
		start++;
		end--;
		for (int i = start; i < end; i++, end--) {
			Collections.swap(bestSolution, i, end);
		}
		return bestSolution;
	}
}
