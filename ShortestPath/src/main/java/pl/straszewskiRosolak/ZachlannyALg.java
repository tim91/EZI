package pl.straszewskiRosolak;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ZachlannyALg implements Algorithm {

	Random r = new Random();
	private int sum = 0;
	private Set<Integer> saw = new HashSet<Integer>();

	public void solve(Instance i) {
		
//		int v = r.nextInt(i.getData().size());
		int v = 0;
		
		int[] vertex = i.getData().get(v);
		
		
		saw.add(v);
		int last = -1;
		while(saw.size() != i.getData().size()){

			int found = findMinV(i, v);
			saw.add(found);
			last = v;
			v = found;
			
		}
		sum += i.getDistanceMatrix()[v][last];
		System.out.println("Suma: " + sum);
	}

	protected int findMinV(Instance i, int v) {
		
		int min = Integer.MAX_VALUE;
		int minV = -1;
		for(int j=0 ; j< i.getData().size(); j++){
			
			if(saw.contains(j)){
				continue;
			}
			
			int dis = i.getDistanceMatrix()[v][j];
			if(dis < min){
				min = dis;
				minV = j;
			}
		}
		sum += min;
		return minV;
	}

	
	
}
