package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EdgeSwitchAlgorithm extends VertexSwitchAlgorithm {

	@Override
	public int solve(Instance ins, int iterations) {

		int sum = 0;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		int sumTime = 0;
		for (int it = 0; it < iterations; it++) {
			long st = System.currentTimeMillis();
			int startVertex = r.nextInt(ins.getData().size());
			int circut = 0;
			int prev = startVertex;
			List<Integer> solution = new ArrayList<Integer>();
			solution.add(startVertex);
			for (int i = 1; i < ins.getData().size(); i++) {
				int next =  Utils.getRandomVertex(ins, solution,r);
				solution.add(next);
				circut += ins.getDistanceMatrix()[prev][next];
				prev = next;
			}
			circut += ins.getDistanceMatrix()[prev][startVertex];

			int size = ins.getData().size();

			// Indeksy par w liście solution(nie zawiera indeksów wierzchołków)
			List<Integer[]> pairs = generatePairs(size);
			List<Integer> bestSolution = solution;
			int bestCircut = circut;
			do {
				circut = bestCircut;
				Object[] result = solve(circut, bestSolution, pairs, ins);
				bestSolution = (List<Integer>) result[0];
				bestCircut = (int) result[1];
			} while (bestCircut < circut);
			sum += bestCircut;
			if (bestCircut < min) {
				min = bestCircut;
			}

			if (bestCircut > max) {
				max = bestCircut;
			}
			sumTime += (System.currentTimeMillis() - st);
		}
		System.out.println("Min: " + min);
		System.out.println("Max: " + max);
		System.out.println("Avg: " + sum / iterations);
		System.out.println("Time Avg: " + sumTime / iterations);
		return min;
	}


	
	Object[] solve(int circut, List<Integer> solution, List<Integer[]> pairs,
			Instance ins) {
		List<Integer> bestSolution = solution;
		int bestCircut = circut;
		for (Integer[] pair : pairs) {
			int prevVertex = getPrevVertex(pair[0], solution);
			int nextVertex = getNextVertex(pair[1], solution);
			int leftVertexIdx = solution.get(pair[0]);
			int rightVertexIdx = solution.get(pair[1]);
			if (pairOfFirstAndLast(ins, pair)) {
				prevVertex = getPrevVertex(pair[1], solution);
				nextVertex = getNextVertex(pair[0], solution);
				int tmp = leftVertexIdx;
				leftVertexIdx = rightVertexIdx;
				rightVertexIdx = tmp;
			}
			int oldPrevEdge = ins.getDistanceMatrix()[prevVertex][leftVertexIdx];
			int oldNextEdge = ins.getDistanceMatrix()[rightVertexIdx][nextVertex];

			int newPrevEdge = ins.getDistanceMatrix()[prevVertex][rightVertexIdx];
			int newNextEdge = ins.getDistanceMatrix()[leftVertexIdx][nextVertex];

			int newCircut = circut - oldPrevEdge - oldNextEdge + newPrevEdge
					+ newNextEdge;

			if (newCircut < bestCircut) {
				bestCircut = newCircut;
				bestSolution = new ArrayList<Integer>(solution);
				if (!pairOfFirstAndLast(ins, pair))
					Utils.revert(bestSolution, pair[0], pair[1]);
				Collections.swap(bestSolution, pair[0], pair[1]);

				// polepszam
				solution = bestSolution;
				return new Object[] { bestSolution, bestCircut };
			}
		}
		return new Object[] { bestSolution, bestCircut };

	}

	@Override
	protected List<Integer[]> generatePairs(int size) {
		List<Integer[]> pairs = new ArrayList<Integer[]>();
		for (int i = 0; i < size; i++) {
			for (int j = i; j < size; j++) {

				if (j == size - 1 && i == 0) {
					continue;
				}

				pairs.add(new Integer[] { i, j });
			}
		}
		return pairs;
	}

}
