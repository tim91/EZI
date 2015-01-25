package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EdgeSwitchAlg extends VertexPairAlg implements Algorithm {
	
	Random r = new Random();
	
	
	List<Integer> currSolution;
	@Override
	public int solve(Instance ins) {
		
		int startVertex = r.nextInt(ins.getData().size());
		int currCircut = 0;
		int prev = startVertex;
		currSolution = new ArrayList<Integer>();
		currSolution.add(startVertex);
		for (int i = 1; i < ins.getData().size(); i++) {
			int next = randVertex(ins, currSolution);
			currSolution.add(next);
			currCircut += ins.getDistanceMatrix()[prev][next];
			prev = next;
		}
		currCircut += ins.getDistanceMatrix()[prev][startVertex];
//		int currCircut = 105;
//		currSolution = Arrays.asList(new Integer[]{1,3,0,4,2});
		
		
		System.out.println("Random: " + currCircut);
		printList(currSolution);
		
		System.out.println("\n");
		
		List<List<Integer[]>> pairs = generatePairsToSwap(ins.getData().size());
		int localBestCirc = currCircut;
		do{
			for (List<Integer[]> list : pairs) {
				for(Integer[] pToSwap : list){
					
					//odejmij odleglosci
					int tempCirc = localBestCirc;
					int firstNeightbour  = (pToSwap[0] == 0) ? ins.getData().size()-1 : pToSwap[0] - 1;
					int secondNeightbour = (pToSwap[1] == ins.getData().size()-1) ? 0 : pToSwap[1] + 1;
					
					tempCirc -= ins.getDistanceMatrix()[pToSwap[0]][firstNeightbour] + ins.getDistanceMatrix()[pToSwap[1]][secondNeightbour];
					tempCirc += ins.getDistanceMatrix()[pToSwap[1]][firstNeightbour] + ins.getDistanceMatrix()[pToSwap[0]][secondNeightbour];
					System.out.println("Temp circ: " + tempCirc);
					if(tempCirc < localBestCirc){
						//better
						currSolution = switchEdges(currSolution,pToSwap[0],pToSwap[1]);
						localBestCirc = tempCirc;
						System.out.println("Curr sol, replaced indexes: " + Arrays.toString(pToSwap) + " circ: " + localBestCirc);
						printList(currSolution);
						System.out.println();
					}
					
				}
			}
		}while(localBestCirc  >= currCircut );
		
		
		return currCircut;
	}
	
	public void printBestSolution(){
		printList(currSolution);
	}
	
	private void printList(List<Integer> l ){
		System.out.println(Arrays.toString(l.toArray(new Integer[currSolution.size()])));
	}
	
	private List<Integer> switchEdges(List<Integer> arr, int a, int b){
		List<Integer> ta = new ArrayList<Integer>(arr.subList(0, a));
		List<Integer> tb;
		List<Integer> tc = new ArrayList<Integer>();
		if(b  < arr.size() - 1){
			tc = arr.subList(b+1, arr.size());
		}
		
		tb = arr.subList(a, b+1);
		Collections.reverse(tb);
		
		ta.addAll(tb);
		ta.addAll(tc);
		return ta;
	}
	
	/**
	 * Tablica z parami indeksów
	 * @param size
	 * @return
	 */
	public List<List<Integer[]>> generatePairsToSwap(int size){
		List<List<Integer[]>> p = new ArrayList<List<Integer[]>>();
		for(int i=0; i<size; i++){
			List<Integer[]> l = new ArrayList<Integer[]>();
			for(int j=i+2; j<size; j++){
				
				if(j == size-1 && i == 0){
					continue;
				}
				
				l.add(new Integer[]{i,j});
			}
			p.add(l);
		}
		return p;
	}
	
}
