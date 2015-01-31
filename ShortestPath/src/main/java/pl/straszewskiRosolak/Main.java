package pl.straszewskiRosolak;

public class Main {

	public static void main(String[] args) {
		try {
//			 Instance ins = Instance.getInstance("data/kroA100.tsp.txt");
			 Instance ins = Instance.getInstance("data/kroA200.tsp");

			 ins.createDistanceMatrix();

//			 Algorithm nnAlg = new NearestNeighborAlgorithm();
//			 System.out.println("NN: ");
//			 nnAlg.solve(ins);
//			
//			 Algorithm gcAlg = new GreedyCycleAlgorithm();
//			 System.out.println("GC: ");
//			 gcAlg.solve(ins);
//
//			 Algorithm randomAlg = new RandomAlgorithm();
//			 System.out.println("Random: ");
//			 randomAlg.solve(ins);
//
//			 VertexSwitchAlgorithm ver = new VertexSwitchAlgorithm();
//			 long st = System.currentTimeMillis();
//			 System.out.println("Vertex Pair: " + ver.solve(ins, 100)
//			 + " czas: " + (System.currentTimeMillis() - st) + " ms");
//			
//			 EdgeSwitchAlgorithm edge = new EdgeSwitchAlgorithm();
//			 st = System.currentTimeMillis();
//			 System.out.println("Egde switch: " + edge.solve(ins, 100)
//			 + " czas: " + (System.currentTimeMillis() - st) + " ms");
//
//			EdgeSwitchWithCandidateMovesAlgorithm edgeCand = new EdgeSwitchWithCandidateMovesAlgorithm();
//			st = System.currentTimeMillis();
//			System.out.println("Egde switch with candidates move: " + edgeCand.solve(ins, 100)
//					+ " czas: " + (System.currentTimeMillis() - st) + " ms");
			
			
			
			long st = System.currentTimeMillis();
			HybridAlgorithm ha = new HybridAlgorithm();
			ha.setIterations(1000);
			ha.setPopulationSize(20);
			ha.solve(ins,1);
			System.out.println("HybridAlgorithm, czas: " + (System.currentTimeMillis() - st) + " ms");
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
