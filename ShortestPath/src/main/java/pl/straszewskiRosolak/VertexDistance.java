package pl.straszewskiRosolak;

public class VertexDistance implements Comparable<VertexDistance> {

	private int distance;
	private int vertexIndex;

	public VertexDistance(int distance, int vertexIndex) {
		super();
		this.distance = distance;
		this.vertexIndex = vertexIndex;
	}

	public int getVertexIndex() {
		return vertexIndex;
	}

	public void setVertexIndex(int vertexIndex) {
		this.vertexIndex = vertexIndex;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(VertexDistance o) {
		if (this.distance > o.getDistance())
			return 1;
		else if (this.distance < o.getDistance())
			return -1;
		return 0;
	}
}
