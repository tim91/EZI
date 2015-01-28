package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PopulationAlg{
	
	private Random r = new Random();
	
	private Population mainPopulation;
	
	public SolutionCandidate getBestSolution(){
		return this.mainPopulation.first();
	}
	
	public int solve(Instance ins, int populationSize, int iterations) {
		
		
		//wygeneruje dwadzieścia losowych rozwiązań i umieśc je na liscie
		Population population = generatePopulation(ins, populationSize);
		EdgePairAlg edgePairAlg = new EdgePairAlg();
		
		for(int iter = 0; iter < iterations; iter ++){
			//wylosuj dwa rozwiązania
			int firstSolIdx = r.nextInt(populationSize);
			int secondSolIdx = firstSolIdx;
			
			do{
				secondSolIdx = r.nextInt(populationSize);
			}while(firstSolIdx == secondSolIdx);
			
			//znajdz czesc wspolna dla rozwiazan
			SolutionCandidate mergedSolution = mergeSolutionsAndCreateNew(getSolution(population,firstSolIdx), getSolution(population, secondSolIdx), ins);
			
			//optymalizuj
			SolutionCandidate optimizedSolution = edgePairAlg.solve(ins, mergedSolution);
			//usun najgorsze rozwiazanie z populacji
			population.pollLast();
			population.add(optimizedSolution);
		}
		
		this.mainPopulation = population;
		return population.first().circuit;
		
	}
	
	private SolutionCandidate getSolution(Population sols, int idx){
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
		
		//FIXME policz obwod, na etapie znajdywania wspolnych podcciagow powinna byc trzymana dotychczasowa dlugosc
		if(sc.solution.size() > 100){
			System.out.println();
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
//		System.out.println("----------------------------");
//		for(int i=0; i< s1.solution.size(); i++){
//			System.out.print(" " + s1.solution.get(i));
//		}
//		System.out.println();
//		for(int i=0; i< s2.solution.size(); i++){
//			System.out.print(" " + s2.solution.get(i));
//		}
//		System.out.println();
		
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
				int tempCurr = currVertex;
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
//						if(elem.get(elems.size()-1) != j){
							elem.add(j);
							saw.add(j);
							currVertex = j;
							notFound = false;
//						}
						
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

	private boolean theSamePair(int l1, int r1, int l2, int r2){
		if(l1 == l2 && r1 == r2)
			return true;
		
		if(l1 == r2 && r1 == l2)
			return true;
		
		return false;
	}
	
	public Population generatePopulation(Instance ins, int size){
		Population population = new Population();
		for(int i=0; i<size; i++ ){
			population.add(new SolutionCandidate(ins));
		}
		return population;
	}

}
