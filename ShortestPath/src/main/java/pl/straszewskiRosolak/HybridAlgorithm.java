package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class HybridAlgorithm extends EdgeSwitchAlgorithm{
	
	private PopulationTreeSet mainPopulation;
	
	private int populationSize;
	private int iterations;
	
	public SolutionCandidate getBestSolution(){
		return this.mainPopulation.first();
	}
	
	@Override
	public int solve(Instance ins, int iterationsToTest) {
		int minLength = Integer.MAX_VALUE;
		int maxLength = Integer.MIN_VALUE;
		int sum = 0;
		for (int i = 0; i < iterationsToTest; i++) {
			int length = solve(ins, this.populationSize, this.iterations);
			if (length < minLength) {
				minLength = length;
			}
			if (length > maxLength) {
				maxLength = length;
			}
			sum += length;
		}
		double avg = (double) sum / (double) iterationsToTest;
		System.out.println("Min: " + minLength);
		System.out.println("Max: " + maxLength);
		System.out.println("Avg: " + avg);
		return minLength;
	}
	
	public int solve(Instance ins, int populationSize, int iterations) {
		
		//wygeneruj populationSize losowych rozwiązań i umieść je na liscie
		PopulationTreeSet population = generatePopulation(ins, populationSize);
		
		for(int iter = 0; iter < iterations; iter ++){
			//wylosuj dwa rozwiązania
			int firstSolIdx = r.nextInt(populationSize);
			int secondSolIdx = firstSolIdx;
			
			do{
				secondSolIdx = r.nextInt(populationSize);
			}while(firstSolIdx == secondSolIdx);
			
			//znajdź część wspólną dla rozwiązań
			SolutionCandidate mergedSolution = mergeSolutionsAndCreateNew(getSolution(population,firstSolIdx), getSolution(population, secondSolIdx), ins);
			
			//optymalizuj
			SolutionCandidate optimizedSolution = solve(ins, mergedSolution);
			//usuń najgorsze rozwiazanie z populacji
			population.pollLast();
			//dodaj nowo wygenerowane rozwiązanie do populacji
			population.add(optimizedSolution);
		}
		
		this.mainPopulation = population;
		return population.first().circuit;
		
	}
	
	@SuppressWarnings("unchecked")
	public SolutionCandidate solve(Instance ins, SolutionCandidate startSolution){
		int size = ins.getData().size();
		List<Integer[]> pairs = generatePairs(size);
		List<Integer> bestSolution = startSolution.solution;
		int bestCircut = startSolution.circuit;
		int circut = 0;
		do {
			circut = bestCircut;
			Object[] result = solve(circut, bestSolution, pairs, ins);
			bestSolution = (List<Integer>) result[0];
			bestCircut = (int) result[1];
		} while (bestCircut < circut);
		
		SolutionCandidate sc = new SolutionCandidate();
		sc.circuit = bestCircut;
		sc.solution = bestSolution;
		sc.createConnArray();
		return sc;
		
	}
	
	private SolutionCandidate getSolution(PopulationTreeSet sols, int idx){
		if(idx == 0)
			return sols.first();
		
		if(idx == sols.size() -1 ){
			return sols.last();
		}
		
		Iterator<SolutionCandidate> it = sols.iterator();
		int i = 0;
		while(it.hasNext()){
			if(i == idx){
				return it.next();
			}
			it.next();
			i++;
		}
		return null;
	}
	
	public SolutionCandidate mergeSolutionsAndCreateNew(SolutionCandidate s1, SolutionCandidate s2,Instance ins){
		
		SolutionCandidate sc = new SolutionCandidate();
		sc.circuit = 0;
		
		List<List<Integer>> elems = findCommonConnections(s1,s2);
		
		Collections.shuffle(elems);
		
		sc.solution = new ArrayList<Integer>(ins.getData().size());
		for (List<Integer> list : elems) {
			sc.solution.addAll(list);
		}
		
		if(sc.solution.size() > 100){
			sc.solution.remove(0);
		}
		for(int i=0; i< sc.solution.size()-1; i++){
			sc.circuit += ins.getDistanceMatrix()[sc.solution.get(i)][sc.solution.get(i+1)];
		}
		sc.circuit += ins.getDistanceMatrix()[sc.solution.get(0)][sc.solution.get(sc.solution.size()-1)];
		sc.createConnArray();
		return sc;
		
	}
	
	public List<List<Integer>> findCommonConnections(SolutionCandidate s1,
			SolutionCandidate s2) {
		
		List<List<Integer>> elems = new ArrayList<List<Integer>>();
		Set<Integer> saw = new HashSet<Integer>();
		int currVertexIdx = 0;
		int currVertex = s1.solution.get(currVertexIdx);
		List<Integer> elem;
		do{
		
			for(int ite=0; ite< s1.solution.size(); ite++){
				if(!saw.contains(s1.solution.get(ite))){
					currVertexIdx = ite;
					break;
				}
			}
			
			elem = new ArrayList<Integer>();
			currVertex = s1.solution.get(currVertexIdx);
			elem.add(currVertex);
			saw.add(currVertex);
			GLOBAL_LOOP: while(true){
				boolean notFound = true;

				LOCAL_LOOP: for(int j=0; j<s1.connArray[currVertex].length;j++){
					
					//najpierw sprawdz pierwszy z ostatnim
					if(currVertexIdx == 0 && j==0){
						if(s1.connArray[currVertex][s1.solution.get(s1.solution.size()-1)] == 1 && s2.connArray[currVertex][s1.solution.get(s1.solution.size()-1)] == 1){
							elem.add(0,s1.solution.get(s1.solution.size()-1));
							saw.add(s1.solution.get(s1.solution.size()-1));
						}
					}
					
					if(j == currVertex || saw.contains(j)) continue;
					
					if(s1.connArray[currVertex][j] == 1 && s2.connArray[currVertex][j] == 1){
							elem.add(j);
							saw.add(j);
							currVertex = j;
							notFound = false;
						
						break LOCAL_LOOP;
					}
				}
				
				if(notFound){
					//nie znaleziono
					break GLOBAL_LOOP;
				}
			}
			elems.add(elem);
			
		}while(saw.size() != s1.solution.size());
		
		return elems;
	}
	
	public PopulationTreeSet generatePopulation(Instance ins, int size){
		PopulationTreeSet population = new PopulationTreeSet();
		for(int i=0; i<size; i++ ){
			population.add(new SolutionCandidate(ins));
		}
		return population;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	
}
