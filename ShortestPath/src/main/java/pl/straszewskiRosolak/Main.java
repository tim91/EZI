package pl.straszewskiRosolak;

public class Main {

	public static void main(String[] args) {
		try {
			Instance ins = Instance.getInstance("data/kroA200.tsp");

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

			VertexPairAlg ver = new VertexPairAlg();
			long st = System.currentTimeMillis();
			System.out.println("Vertex Pair: " + ver.solve(ins,100) + " czas: " + (System.currentTimeMillis() - st) + " ms");
			
			EdgePairAlg edge = new EdgePairAlg();
			st = System.currentTimeMillis();
			System.out.println("Egde switch: " + edge.solve(ins,100) + " czas: " + (System.currentTimeMillis() - st) + " ms");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
