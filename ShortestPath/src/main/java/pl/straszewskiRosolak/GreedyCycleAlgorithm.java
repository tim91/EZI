package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GreedyCycleAlgorithm extends Algorithm {

	public int solve(Instance i, int startVertex) {

		int minV = Utils.findClosestVertex(i, startVertex);

		TempResult tr = new TempResult();
		tr.vertex.add(startVertex);
		tr.vertex.add(minV);
		tr.vertexOnList.add(startVertex);
		tr.vertexOnList.add(minV);
		tr.circuit = i.getDistanceMatrix()[startVertex][minV];
		
		//dopóki nie umieszcze wszystkich wierzchołków na liscie
		while (tr.vertexOnList.size() != i.getData().size()) {
			
			int minCircuitGlobal = Integer.MAX_VALUE;
			int[] minPosGlobal = new int[3];
			
			// dla kazdego wierzcholka
			for (int f = 0; f < i.getData().size(); f++) {
				
				if (tr.vertexOnList.contains(f)) {
					continue;
				}

				// dla kazdej pary wierzcholkow dcinam krawędź
				int minCircuitLocal = Integer.MAX_VALUE;
				
				int[] minPosLocal = new int[3];
				for (int vIdx = 0; vIdx < tr.vertex.size() ; vIdx++) {
					
					int circuit = tr.circuit;
					int vL;
					int vR;
					if(vIdx == tr.vertex.size()-1 && vIdx > 0){
						// lewy wierzcholek
						vL = tr.vertex.get(vIdx);
						// prawy wierzcholek
						vR = tr.vertex.get(0);
					}else if(vIdx == tr.vertex.size()-1){
						continue;
					}else{
						// lewy wierzcholek
						vL = tr.vertex.get(vIdx);
						// prawy wierzcholek
						vR = tr.vertex.get(vIdx + 1);
					}
					

					// odcinam ta krawedz wiec odejmuje odleglość
					int tmp = i.getDistanceMatrix()[vL][vR];
					if (tr.vertex.size() > 2)
						circuit -= tmp;

					// dodaje odleglość tych punktów od sprawdzanego wierzcholka
					circuit += i.getDistanceMatrix()[vL][f];
					circuit += i.getDistanceMatrix()[vR][f];

					if (circuit < minCircuitLocal) {
						minCircuitLocal = circuit;
						// odkladam indeksy wierzcholkow na liscie
						minPosLocal[0] = vIdx;
						minPosLocal[1] = vIdx + 1;
						minPosLocal[2] = f;
					}
				}

				if (minCircuitLocal < minCircuitGlobal) {
					minCircuitGlobal = minCircuitLocal;
					minPosGlobal = minPosLocal;
				}

			}

			// znalazlem wierzcholek do doklejenia
			tr.circuit = minCircuitGlobal;
			tr.vertex.add(minPosGlobal[1], minPosGlobal[2]);
			tr.vertexOnList.add(minPosGlobal[2]);
		}

		return tr.circuit;
	}

	private class TempResult {

		// indeksy wierzcholkow
		public List<Integer> vertex = new ArrayList<Integer>();

		public Set<Integer> vertexOnList = new HashSet<Integer>();

		public int circuit;

	}

	@Override
	public int solve(Instance i) {
		// TODO Auto-generated method stub
		return 0;
	}

}
