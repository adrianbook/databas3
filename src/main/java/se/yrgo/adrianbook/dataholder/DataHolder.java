package se.yrgo.adrianbook.dataholder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.yrgo.adrianbook.dataholder.comparators.RowComparator;
import se.yrgo.adrianbook.exceptions.DataHolderException;

public class DataHolder {

	private List<Row> dataTable;
	private String[] columnHeaders;
	private boolean[] numericColumns;
	private int[] longestEntryPerColumn;
	private int numberOfColumns;
	private Object[] metaData;
	
	protected DataHolder(List<List<String>> dataTable, String[] columnHeaders) {
		this.numberOfColumns = columnHeaders.length;
		this.dataTable = new ArrayList<Row>();
		
		for (List<String> row : dataTable) {
			this.dataTable.add(new Row(row));
		}
		
		this.columnHeaders = columnHeaders;
		this.numericColumns = determineNumericColumns(this.dataTable);		
		this.longestEntryPerColumn = findLongestEntryPerColumn(this.dataTable);
	}
	
	protected DataHolder(List<List<String>> dataTable, String[] columnHeaders, Object... metaData) {
		this(dataTable, columnHeaders);
		this.metaData = metaData;
	}

	private boolean[] determineNumericColumns(List<Row> dataTable) {
		boolean[] numericColumns = new boolean[this.numberOfColumns];
		Arrays.fill(numericColumns, true);
		for(int i = 0; i < this.numberOfColumns; i++) {
			for (Row row: dataTable) {
					if(checkIfNotInteger(row.getDataByIndex(i))) {
						numericColumns[i] = false;
						break;
					}
				}
			}
		return numericColumns;
	}

	private boolean checkIfNotInteger(String stringToTest) {
		if (stringToTest.matches("\\d+")) return false;
		return true;
	}

	private int[] findLongestEntryPerColumn(List<Row> dataTable) {
		int[] longestEntry = new int[this.numberOfColumns];
		for(int i = 0; i < this.numberOfColumns; i++) {
			longestEntry[i] = this.columnHeaders[i].length();
			for (Row row: dataTable) {
				if(longestEntry[i] < row.getDataByIndex(i).length()) {
					longestEntry[i] = row.getDataByIndex(i).length();
				}
			}
		}
		return longestEntry;
	}
	

	public List<Row> getDataTable() {
		return Collections.unmodifiableList(dataTable);
	}

	protected int[] getLongestEntryLengthPerColumn() {
		return this.longestEntryPerColumn.clone();
	}

	protected boolean columnIsNumeric(int columnNumber) {
		return this.numericColumns[columnNumber-1];
	}

	protected String getRowToString(int index) {
		Row row = this.dataTable.get(index);
		return rowToString(row.getRowOfData());
	}
	
	private String rowToString(List<String> row) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < row.size(); i++) {
			if (i == 0) {
				builder.append("|");
			}
			int columnBreadth = this.longestEntryPerColumn[i]+2;
			int entryLength = row.get(i).length();
			int amountOfPadding = columnBreadth - entryLength;
			String padding = " ".repeat(amountOfPadding);
			builder.append(row.get(i)).append(padding).append("|");
		}
		builder.append("\n");
		return builder.toString();
	}

	public String getColumnHeadersToString() {
		List<String> headersList = List.of(columnHeaders);
		return rowToString(headersList);	
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("");
		if (metaData != null) {
			builder.append(getMetaDataString());
		}
		if (dataTable.equals(Collections.EMPTY_LIST)) {
			return builder.toString();
		}
		builder.append(makePartitionString('_','.'));
		builder.append(getColumnHeadersToString());
		builder.append(makePartitionString('=','|'));
		for (Row row: this.dataTable) {
			builder.append(rowToString(row.getRowOfData()));
		}
		builder.append(makePartitionString('=' , '+'));
		return builder.toString();
	}
	
	private String makePartitionString(char filler, char columnPartitioner) {
		List<String> nicePartition = new ArrayList<>();
		for (int i = 0; i < numberOfColumns; i++) {
			nicePartition.add("");
		}
		String nicePartitionString = rowToString(nicePartition);
		nicePartitionString = nicePartitionString.replace(' ', filler);
		nicePartitionString = nicePartitionString.replace('|', columnPartitioner);
		
		return nicePartitionString;
	}
	
	public void sortOnColumn(String columnName) {
		sortOnColumn(columnName, 1);
	}
	
	public void sortOnColumn(int columnNumber) {
		sortOnColumn(columnNumber, 1);
	}
	
	public void sortOnColumn(String columnName, int modifier) {
		int index = List.of(this.columnHeaders).indexOf(columnName);
		
		sortOnColumnIndex(index, modifier);
	}

	public void sortOnColumn(int columnNumber, int modifier) {
		int index = columnNumber - 1;
		
		sortOnColumnIndex(index, modifier);		
	}
	
	private void sortOnColumnIndex(int index, int modifier) {
		for (Row row : this.dataTable) {
			row.setComparisonColumn(index, this.numericColumns);
		}
		
		Comparator<Row> comparator = new RowComparator(modifier);
		this.dataTable.sort(comparator);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(columnHeaders);
		result = prime * result + ((dataTable == null) ? 0 : dataTable.hashCode());
		result = prime * result + Arrays.hashCode(numericColumns);
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
		DataHolder other = (DataHolder) obj;
		if (!Arrays.equals(columnHeaders, other.columnHeaders))
			return false;
		if (this.dataTable == null) {
			if (other.dataTable != null)
				return false;
		} else if (!this.dataTable.equals(other.dataTable))
			return false;
		if (!Arrays.equals(this.numericColumns, other.numericColumns))
			return false;
		return true;
	}

	public String getMetaDataString() {
		StringBuilder builder = new StringBuilder();
		for (Object o : metaData) {
			builder
				.append(o.toString())
				.append("\n");
		}
		return builder.toString();
	}
	
}
