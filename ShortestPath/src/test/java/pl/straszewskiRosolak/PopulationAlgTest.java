package pl.straszewskiRosolak;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class PopulationAlgTest {

	@Test
	public void generatePopulationTest(){
		Instance ins;
		try {
			InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("test4.txt");
			ins = Instance.getInstance(is);
			ins.createDistanceMatrix();
			
			PopulationAlg p = new PopulationAlg();
			Population ss = p.generatePopulation(ins, 5);
			
			Assert.assertEquals(5, ss.size());
			
			ss = p.generatePopulation(ins, 10);
			Assert.assertEquals(10, ss.size());
			
//			for (SolutionCandidate sc : ss) {
//				System.out.println(sc.circuit + " " + Arrays.toString(sc.solution.toArray(new Integer[sc.solution.size()])));
//			}
//			
//			System.out.println(ss);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void commonPathsTest(){
//		InputStream is = this.getClass().getClassLoader()
//				.getResourceAsStream("test4.txt");
//		Instance ins;
		try {
			Instance ins = Instance.getInstance("data/kroA100.tsp.txt");
			ins.createDistanceMatrix();
			
			PopulationAlg p = new PopulationAlg();
			Population ss = p.generatePopulation(ins, 20);
			
			SolutionCandidate p1 = ss.first();
			SolutionCandidate p2 = ss.last();
			
			p1 = new SolutionCandidate();
			p1.solution = new ArrayList<Integer>();
			p1.solution.add(0);
			p1.solution.add(1);
			p1.solution.add(2);
			p1.solution.add(3);
			p1.solution.add(4);
			p1.solution.add(5);
			p1.solution.add(6);
			p1.solution.add(7);
			p1.solution.add(8);
			p1.solution.add(9);
			p1.createConnArray();
			
			p2 = new SolutionCandidate();
			p2.solution = new ArrayList<Integer>();
			p2.solution.add(0);
			p2.solution.add(1);
			p2.solution.add(2);
			p2.solution.add(3);
			p2.solution.add(5);
			p2.solution.add(4);
			p2.solution.add(8);
			p2.solution.add(7);
			p2.solution.add(6);
			p2.solution.add(9);
			p2.createConnArray();
			
			for(int i=0; i< p1.solution.size(); i++){
				System.out.print(" " + p1.solution.get(i));
			}
			System.out.println();
			for(int i=0; i< p2.solution.size(); i++){
				System.out.print(" " + p2.solution.get(i));
			}
			System.out.println();
			
			List<List<Integer>> res = p.findCommonConnections(p1, p2);
			
			for (List<Integer> list : res) {
				for (Integer in : list) {
					System.out.print(" " + in);
				}
				System.out.println();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
