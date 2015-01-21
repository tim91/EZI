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

//		int randV = r.nextInt(i.getData().size());
		int randV = 0;
		int minV = findMinV(i, randV);
		
		TempResult tr = new TempResult();
		tr.vertex.add(randV);
		tr.vertex.add(minV);
		tr.vertexOnList.add(randV);
		tr.vertexOnList.add(minV);
		tr.circuit = i.getDistanceMatrix()[randV][minV];
		
		while(tr.vertexOnList.size() != i.getData().size()){
			int minDist = Integer.MAX_VALUE;
			int minCircuitGlobal = Integer.MAX_VALUE;
			int[] minPosGlobal = new int[3];
			for(int f=0; f < i.getData().size(); f++){
				//dla kazdego wierzcholka
				
				if(tr.vertexOnList.contains(f)){
					continue;
				}
				
				//dla kazdej pary wierzcholkow wciskam pomiedzy
				int minCircuitLocal = Integer.MAX_VALUE;
				// vL, vP , i nowy dodany miedzy nimi
				int[] minPosLocal = new int[3];
				for(int vIdx=0; vIdx<tr.vertex.size()-1;vIdx++){
					int circuit = tr.circuit;
					//lewy wierzcholek
					int vL = tr.vertex.get(vIdx);
					//prawy wierzcholek
					int vR = tr.vertex.get(vIdx+1);
					
					//odcinam ta krawedz wiec odejmuje odleglość
					circuit -= i.getDistanceMatrix()[vL][vR];
					
					//dodaje odleglos tych punktów od sprawdzanego wierzcholka f
					circuit += i.getDistanceMatrix()[vL][f];
					circuit += i.getDistanceMatrix()[vR][f];
					
					if(circuit < minCircuitLocal){
						minCircuitLocal = circuit;
						//odkladam indeksy wierzcholkow na liscie
						minPosLocal[0] = vIdx;
						minPosLocal[1] = vIdx+1;
						minPosLocal[2] = f;
					}
				}
				 
				if(minCircuitLocal < minCircuitGlobal ){
					minCircuitGlobal = minCircuitLocal;
					minPosGlobal = minPosLocal;
				}
				
			}
			
			//znalazelm wierzcholek do doklejenia
			tr.circuit = minCircuitGlobal;
			tr.vertex.add(minPosGlobal[0], minPosGlobal[2]);
			tr.vertexOnList.add(minPosGlobal[2]);
		}
		
		System.out.println("Start: "+randV + " wynik: " + tr.circuit);
		
	}
	
	private class TempResult{
		
		//indeksy wierzcholkow
		public List<Integer> vertex = new ArrayList<Integer>();
		public Set<Integer> vertexOnList = new HashSet<Integer>();
		public int circuit;
		
	}

}
