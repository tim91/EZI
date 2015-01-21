package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GCDrugiAlg extends ZachlannyALg implements Algorithm {

	
	Random r = new Random();
	
	@Override
	public void solve(Instance i) {

		int v = r.nextInt(i.getData().size());
		
		Set<Integer> saw = new HashSet<Integer>();
		int minV = findMinV(i, v);
		saw.add(minV);
		saw.add(v);
		
		
//		for(int iter = 0; iter < i.getData().size(); iter++){
			
			int minDist = Integer.MAX_VALUE;
			for(int f=0; f < i.getData().size(); f++){
				if(saw.contains(f)){
					continue;
				}
				
				
			}
//		}
		
		
	}
	
	private class Helper{
		
		public List<int[]> vertex = new ArrayList<int[]>();
		public int value;
		
	}

}
