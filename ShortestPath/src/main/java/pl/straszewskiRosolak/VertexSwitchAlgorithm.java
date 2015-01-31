package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VertexSwitchAlgorithm extends Algorithm {

	@Override
	public int solve(Instance ins, int iterations) {

		int sum = 0;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int it=0; it< iterations; it++){
			int startVertex = r.nextInt(ins.getData().size());
			int circut = 0;
			int prev = startVertex;
			List<Integer> solution = new ArrayList<Integer>();
			solution.add(startVertex);
			for (int i = 1; i < ins.getData().size(); i++) {
				int next = Utils.getRandomVertex(ins, solution,r);
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
			if(bestCircut < min){
				min = bestCircut;
			}
			
			if(bestCircut > max){
				max = bestCircut;
			}
		}
		
		System.out.println("Min: " + min);
		System.out.println("Max: " + max);
		System.out.println("Avg: " + sum/iterations);
		
		return min;
		
	}

	private Object[] solve(int circut, List<Integer> solution, List<Integer[]> pairs,
			Instance ins) {
		List<Integer> bestSolution = solution;
		int bestCircut = circut;
		for (Integer[] pair : pairs) {
			
			int prevVertex_0 = getPrevVertex(pair[0], solution);
			int nextVertex_0 = getNextVertex(pair[0], solution);
			int prevVertex_1 = getPrevVertex(pair[1], solution);
			int nextVertex_1 = getNextVertex(pair[1], solution);
			
			int leftVertexIdx = solution.get(pair[0]);
			int rightVertexIdx = solution.get(pair[1]);
			
			int newCircut = 0;
			
			if (pairOfFirstAndLast(ins, pair)) {
				newCircut = circut - ins.getDistanceMatrix()[leftVertexIdx][nextVertex_0] - ins.getDistanceMatrix()[prevVertex_1][rightVertexIdx];
				newCircut = newCircut + ins.getDistanceMatrix()[nextVertex_0][rightVertexIdx] + ins.getDistanceMatrix()[prevVertex_1][leftVertexIdx];
			}
			else if(neightrour(pair)){
				newCircut = circut - ins.getDistanceMatrix()[prevVertex_0][leftVertexIdx] - ins.getDistanceMatrix()[nextVertex_1][rightVertexIdx];
				newCircut = newCircut + ins.getDistanceMatrix()[prevVertex_0][rightVertexIdx] + ins.getDistanceMatrix()[nextVertex_1][leftVertexIdx];
			}else{
				int oldPrevEdge_0 = ins.getDistanceMatrix()[prevVertex_0][leftVertexIdx];
				int oldNextEdge_0 = ins.getDistanceMatrix()[leftVertexIdx][nextVertex_0];
				
				int oldPrevEdge_1 = ins.getDistanceMatrix()[prevVertex_1][rightVertexIdx];
				int oldNextEdge_1 = ins.getDistanceMatrix()[rightVertexIdx][nextVertex_1];

				int newPrevEdge_0 = ins.getDistanceMatrix()[prevVertex_0][rightVertexIdx];
				int newNextEdge_0 = ins.getDistanceMatrix()[rightVertexIdx][nextVertex_0];
				
				int newPrevEdge_1 = ins.getDistanceMatrix()[prevVertex_1][leftVertexIdx];
				int newNextEdge_1 = ins.getDistanceMatrix()[leftVertexIdx][nextVertex_1];

				newCircut = circut - oldPrevEdge_0 - oldNextEdge_0 - oldPrevEdge_1 - oldNextEdge_1
						+ newPrevEdge_0 + newNextEdge_0 + newPrevEdge_1 + newNextEdge_1;
			}
			
			
			if (newCircut < bestCircut) {
				bestCircut = newCircut;
				bestSolution = new ArrayList<Integer>(solution);
				
				Collections.swap(bestSolution, pair[0], pair[1]);
				// od razu polepszam
				solution = bestSolution;
				// bez tej linii jest kosmos wynik
				return new Object[] { bestSolution, bestCircut };
			}
		}
		return new Object[] { bestSolution, bestCircut };

	}

	protected boolean pairOfFirstAndLast(Instance ins, Integer[] pair) {
		return (pair[0] == 0 && pair[1] == ins.getData().size() - 1);
	}
	
	private boolean neightrour(Integer [] pair){
		return pair[0] + 1 == pair[1];
	}

	protected int getNextVertex(Integer vertex, List<Integer> solution) {
		if (vertex == solution.size() - 1)
			return solution.get(0);
		else
			return solution.get(vertex + 1);
	}

	protected int getPrevVertex(Integer vertex, List<Integer> solution) {
		if (vertex == 0)
			return solution.get(solution.size() - 1);
		else
			return solution.get(vertex - 1);
	}

	protected List<Integer[]> generatePairs(int size) {
		List<Integer[]> pairs = new ArrayList<Integer[]>();
		for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				pairs.add(new Integer[] { i, j });
			}
		}
		return pairs;
	}

}
