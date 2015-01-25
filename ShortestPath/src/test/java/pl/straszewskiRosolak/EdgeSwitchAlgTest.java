package pl.straszewskiRosolak;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class EdgeSwitchAlgTest {

	@Test
	public void pairsGenerator(){
		
		EdgeSwitchAlg esa = new EdgeSwitchAlg();
		
		List<List<Integer[]>> res = esa.generatePairsToSwap(5);
		
		for (List<Integer[]> list : res) {
			for(Integer[] ll : list){
				System.out.print(" " + Arrays.toString(ll));
			}
			System.out.println();
		}
		
	}
	
	@Test
	public void algTest(){
		
		InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream("test3.txt");
		Instance ins;
		try {
			ins = Instance.getInstance(is);
			ins.createDistanceMatrix();
			ins.printDistanceMatrix();

			EdgeSwitchAlg edge = new EdgeSwitchAlg();
			
			int circ = edge.solve(ins);
			System.out.println("Best circ: " + circ);
			edge.printBestSolution();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
