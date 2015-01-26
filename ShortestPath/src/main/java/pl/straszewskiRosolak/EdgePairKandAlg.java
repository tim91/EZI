package pl.straszewskiRosolak;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class EdgePairKandAlg extends EdgePairAlg {

	protected List<Integer[]> generatePairs(int size, Instance ins) {
		List<Integer[]> pairs = new ArrayList<Integer[]>();
		Set<String> p = new HashSet<String>();
		for (int i = 0; i < ins.getData().size(); i++) {
			TreeSet<VertexDistance> closest = new TreeSet<VertexDistance>();
			for (int j = 0; j < ins.getData().size(); j++) {
				if (i == j)
					continue;
				int dist = ins.getDistanceMatrix()[i][j];
				closest.add(new VertexDistance(dist, j));
			}
			Iterator<VertexDistance> it = closest.iterator();
			for (int j = 0; j < 5; j++) {
				if (!it.hasNext())
					break;
				VertexDistance vd = it.next();
				if (!p.contains(i + "" + vd.getVertexIndex())) {
					p.add(i + "" + vd.getVertexIndex());
					p.add(vd.getVertexIndex() + "" + i);
					if (i < vd.getVertexIndex())
						pairs.add(new Integer[] { i, vd.getVertexIndex() });
					else
						pairs.add(new Integer[] { vd.getVertexIndex(), i });
				}

			}
		}
		return pairs;
	}
}
