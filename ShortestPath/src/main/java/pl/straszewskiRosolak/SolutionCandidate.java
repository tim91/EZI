package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SolutionCandidate implements Comparable<SolutionCandidate>{
	
	public int circuit = 0;
	public List<Integer> solution;
	public int[][] connArray;
	
	public SolutionCandidate() {
	}
	
	public void print(){
		for(int i=0; i< this.solution.size()-1; i++){
			System.out.print(" " + this.solution.get(i));
		}
	}
	
	public SolutionCandidate(Instance ins) {
		
		solution = new ArrayList<Integer>();
		connArray = new int[ins.getData().size()][ins.getData().size()];
		Random r = new Random();
		int startVertex = r.nextInt(ins.getData().size());
		solution.add(startVertex);
		int prev = startVertex;
		for (int i = 1; i < ins.getData().size(); i++) {
			int next = Utils.getRandomVertex(ins, solution,r);
			solution.add(next);
			circuit += ins.getDistanceMatrix()[prev][next];
			prev = next;
		}
		circuit += ins.getDistanceMatrix()[prev][startVertex];
		createConnArray();
		
	}

	public void createConnArray(){
		connArray = new int[solution.size()][solution.size()];
		for(int i=0; i< this.solution.size()-1; i++){
			connArray[this.solution.get(i)][this.solution.get(i+1)]=1;
			connArray[this.solution.get(i+1)][this.solution.get(i)]=1;
		}
		connArray[this.solution.get(0)][this.solution.get(this.solution.size()-1)]=1;
		connArray[this.solution.get(this.solution.size()-1)][this.solution.get(0)]=1;
	}

	@Override
	public int compareTo(SolutionCandidate o) {
		
		if(this.circuit < o.circuit){
			return -1;
		}else{
			return 1;
		}
	}
}
