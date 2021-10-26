package se.yrgo.adrianbook.dataholder.comparators;

import java.util.Comparator;

import se.yrgo.adrianbook.dataholder.Row;

public class RowComparator implements Comparator<Row> {
	
	private int modifier;
	
	public RowComparator(int modifier) {
		if (modifier < 0) this.modifier = -1;
		else this.modifier = 1;
	}
	
	public int compare(Row row1, Row row2) {
		if (row1.isNumeric() && row2.isNumeric()) {
			Integer compareVal1 = Integer.valueOf(row1.getComparingColumnValue());
			Integer compareVal2 = Integer.valueOf(row2.getComparingColumnValue());
			
			return modifier * compareVal1.compareTo(compareVal2);
		} else {
			String compareVal1 = row1.getComparingColumnValue();
			String compareVal2 = row2.getComparingColumnValue();

			return modifier * compareVal1.compareTo(compareVal2);
		}
	}
}
