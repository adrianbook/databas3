package se.yrgo.adrianbook.dataholder;

import java.util.List;

public class Row {
	private List<String> data;
	private int comparingIndex;
	private boolean numeric;
	
	public Row(List<String> data) {
		this.data = data;
		this.comparingIndex = 0;
	}
	
	
	public void setComparisonColumn(int index, boolean[] numericColumns) {
		if (index >= data.size()) {
			throw new IndexOutOfBoundsException("provided sorting column number out of bounds for this table");
		}
		this.numeric = numericColumns[index];
		this.comparingIndex = index;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + comparingIndex;
		result = prime * result + (numeric ? 1231 : 1237);
		result = prime * result + getComparingColumnValue().hashCode();
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
		Row other = (Row) obj;
		if (comparingIndex != other.comparingIndex)
			return false;
		if (getComparingColumnValue() != other.getComparingColumnValue())
			return false;
		if (numeric != other.numeric)
			return false;
		return true;
	}


	public List<String> getRowOfData() {
		return this.data;
	}
	
	public String getDataByIndex(int index) {
		return this.data.get(index);
	}
	
	public String getComparingColumnValue() {
		return this.data.get(comparingIndex);
	}
	
	public boolean isNumeric() {
		return this.numeric;
	}

}
