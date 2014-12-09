package put.cs.idss.dw.weka;

public class CountHelper {

	public double classValue;
	public double attributeIdx;
	public double value;
	public double getClassValue() {
		return classValue;
	}
	public void setClassValue(double classValue) {
		this.classValue = classValue;
	}
	public double getAttributeIdx() {
		return attributeIdx;
	}
	public void setAttributeIdx(double attributeIdx) {
		this.attributeIdx = attributeIdx;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(attributeIdx);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (Double.doubleToLongBits(attributeIdx) != Double
				.doubleToLongBits(other.attributeIdx))
			return false;
		if (Double.doubleToLongBits(classValue) != Double
				.doubleToLongBits(other.classValue))
			return false;
		if (Double.doubleToLongBits(value) != Double
				.doubleToLongBits(other.value))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CountHelper [classValue=" + classValue + ", attributeIdx="
				+ attributeIdx + ", value=" + value + "]";
	}
	
	
	
}
