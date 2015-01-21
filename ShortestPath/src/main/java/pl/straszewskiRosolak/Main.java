package pl.straszewskiRosolak;

public class Main {

	public static void main(String[] args) {
		try {
			Instance ins = Instance.getInstance("data/kroA100.tsp.txt");
			
			ins.createDistanceMatrix();
			
//			ins.printDistanceMatrix();
			
			Algorithm alg = new ZachlannyALg();
			alg.solve(ins);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
