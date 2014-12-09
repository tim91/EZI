package put.cs.idss.dw.weka;

public class CountHelper {

	public int attributeIdx;
	public double classValue;
	public double value;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + attributeIdx;
		long temp;
		temp = Double.doubleToLongBits(classValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(value);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CountHelper other = (CountHelper) obj;
		if (attributeIdx != other.attributeIdx)
			return false;
		if (Double.doubleToLongBits(classValue) != Double
				.doubleToLongBits(other.classValue))
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}
	
	
	
}
