package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class VertexPairAlg extends ZachlannyALg implements Algorithm {

	Random r = new Random();

	@Override
	public int solve(Instance ins) {

		int startVertex = r.nextInt(ins.getData().size());
		int circut = 0;
		int prev = startVertex;
		List<Integer> solution = new ArrayList<Integer>();
		solution.add(startVertex);
		for (int i = 1; i < ins.getData().size(); i++) {
			int next = randVertex(ins, solution);
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
		return bestCircut;
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
					revert(bestSolution, pair[0], pair[1]);
				Collections.swap(bestSolution, pair[0], pair[1]);
				// od razu polepszam
				solution = bestSolution;
				// bez tej linii jest kosmos wynik
				return new Object[] { bestSolution, bestCircut };
			}
		}
		return new Object[] { bestSolution, bestCircut };

	}

	private boolean pairOfFirstAndLast(Instance ins, Integer[] pair) {
		return (pair[0] == 0 && pair[1] == ins.getData().size() - 1);
	}

	List<Integer> revert(List<Integer> bestSolution, int start, int end) {
		start++;
		end--;
		for (int i = start; i < end; i++, end--) {
			Collections.swap(bestSolution, i, end);
		}
		return bestSolution;
	}

	private int getNextVertex(Integer vertex, List<Integer> solution) {
		if (vertex == solution.size() - 1)
			return solution.get(0);
		else
			return solution.get(vertex + 1);
	}

	int getPrevVertex(Integer vertex, List<Integer> solution) {
		if (vertex == 0)
			return solution.get(solution.size() - 1);
		else
			return solution.get(vertex - 1);
	}

	List<Integer[]> generatePairs(int size) {
		List<Integer[]> pairs = new ArrayList<Integer[]>();
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				pairs.add(new Integer[] { i, j });
			}
		}
		return pairs;
	}

	protected int randVertex(Instance ins, List<Integer> saw) {
		int rr = -1;
		do {
			rr = r.nextInt(ins.getData().size());

		} while (saw.contains(rr));
		return rr;
	}
}
