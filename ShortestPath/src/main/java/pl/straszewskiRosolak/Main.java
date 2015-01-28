package pl.straszewskiRosolak;

public class Main {

	public static void main(String[] args) {
		try {
			Instance ins = Instance.getInstance("data/kroA100.tsp.txt");

			ins.createDistanceMatrix();

			// ZachlannyALg alg = new ZachlannyALg();
			// System.out.println("NN: ");
			// alg.solve(ins);
			//
			// GCDrugiAlg dr = new GCDrugiAlg();
			// System.out.println("GC: ");
			// dr.solve(ins);
			//
			// ins = Instance.getInstance("data/kroA200.tsp");
			//
			// ins.createDistanceMatrix();
			//
			// alg = new ZachlannyALg();
			// System.out.println("NN: ");
			// alg.solve(ins);
			//
			// dr = new GCDrugiAlg();
			// System.out.println("GC: ");
			// dr.solve(ins);
			//
			// Algorithm ran = new RandomAlg();
			// System.out.println("Random: ");
			// ran.solve(ins);

			// VertexPairAlg ver = new VertexPairAlg();
			// long st = System.currentTimeMillis();
			// System.out.println("Vertex Pair: " + ver.solve(ins, 100)
			// + " czas: " + (System.currentTimeMillis() - st) + " ms");
			//
			// EdgePairAlg edge = new EdgePairAlg();
			// long st = System.currentTimeMillis();
			// System.out.println("Egde switch: " + edge.solve(ins, 100)
			// + " czas: " + (System.currentTimeMillis() - st) + " ms");

//			EdgePairKandAlg edgeKand = new EdgePairKandAlg();
//			long st = System.currentTimeMillis();
//			System.out.println("Egde switch: " + edgeKand.solve(ins, 100)
//					+ " czas: " + (System.currentTimeMillis() - st) + " ms");
			
			
			
			long st = System.currentTimeMillis();
			PopulationAlg pa = new PopulationAlg();
			System.out.println("Population min: "+pa.solve(ins, 20,1000)+ " czas: " + (System.currentTimeMillis() - st) + " ms");
			//sprawdzenie
			int circuit = 0;
			SolutionCandidate sc = pa.getBestSolution();
			for(int i=0; i< sc.solution.size()-1; i++){
				circuit += ins.getDistanceMatrix()[sc.solution.get(i)][sc.solution.get(i+1)];
			}
			circuit += ins.getDistanceMatrix()[sc.solution.get(0)][sc.solution.get(pa.getBestSolution().solution.size()-1)];
			System.out.println("Dlugość: " + circuit);
			sc.print();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
