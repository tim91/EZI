package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GCDrugiAlg extends ZachlannyALg implements Algorithm {

	Random r = new Random();

	@Override
	public int solve(Instance i, int startVertex) {

		int minV = findMinV(i, startVertex);

		TempResult tr = new TempResult();
		tr.vertex.add(startVertex);
		tr.vertex.add(minV);
		tr.vertexOnList.add(startVertex);
		tr.vertexOnList.add(minV);
		tr.circuit = i.getDistanceMatrix()[startVertex][minV];
		while (tr.vertexOnList.size() != i.getData().size()) {
			int minCircuitGlobal = Integer.MAX_VALUE;
			int[] minPosGlobal = new int[3];
			for (int f = 0; f < i.getData().size(); f++) {
				// dla kazdego wierzcholka

				if (tr.vertexOnList.contains(f)) {
					continue;
				}

				// dla kazdej pary wierzcholkow wciskam pomiedzy
				int minCircuitLocal = Integer.MAX_VALUE;
				// vL, vP , i nowy dodany miedzy nimi
				int[] minPosLocal = new int[3];
				for (int vIdx = 0; vIdx < tr.vertex.size() - 1; vIdx++) {
					int circuit = tr.circuit;
					// lewy wierzcholek
					int vL = tr.vertex.get(vIdx);
					// prawy wierzcholek
					int vR = tr.vertex.get(vIdx + 1);

					// odcinam ta krawedz wiec odejmuje odleglość
					int tmp = i.getDistanceMatrix()[vL][vR];
					if (tr.vertex.size() > 2)
						circuit -= tmp;

					// dodaje odleglos tych punktów od sprawdzanego wierzcholka
					// f
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

			// znalazelm wierzcholek do doklejenia
			tr.circuit = minCircuitGlobal;
			tr.vertex.add(minPosGlobal[0], minPosGlobal[2]);
			tr.vertexOnList.add(minPosGlobal[2]);
		}

		return tr.circuit;
	}

	private boolean isFirstEdge(int startVertex, int minV, int vL, int vR) {
		if (startVertex == vL && minV == vR)
			return true;
		if (startVertex == vR && minV == vL)
			return true;
		return false;
	}

	private class TempResult {

		// indeksy wierzcholkow
		public List<Integer> vertex = new ArrayList<Integer>();

		public Set<Integer> vertexOnList = new HashSet<Integer>();

		public int circuit;

	}

}
