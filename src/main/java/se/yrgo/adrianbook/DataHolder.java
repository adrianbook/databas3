package se.yrgo.adrianbook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHolder {
	private List<List<String>> dataTable;
	private String[] collumnHeaders;
	private boolean[] numericCollumns;
	private int[] longestEntryPerCollumn;
	private Map<String, Object[]> metaData;
	private int rowLength;
	
	public DataHolder(List<List<String>> dataTable, String[] collumnHeaders) {
		this.rowLength = collumnHeaders.length;
		this.dataTable = dataTable;
		this.metaData = new HashMap<>();
		
		this.collumnHeaders = collumnHeaders;
		this.longestEntryPerCollumn = findLongestEntryPerCollumn(this.dataTable);
		this.numericCollumns = determineNumericCollumns(this.dataTable);
		
	}

	private boolean[] determineNumericCollumns(List<List<String>> dataTable) {
		boolean[] numericCollumns = new boolean[this.rowLength];
		Arrays.fill(numericCollumns, true);
		for(int i = 0; i < this.rowLength; i++) {
			for (List<String> row: dataTable) {
					if(checkIfNotInteger(row.get(i))) {
						numericCollumns[i] = false;
						break;
					}
				}
			}
		return numericCollumns;
	}

	private boolean checkIfNotInteger(String stringToTest) {
		try {
			Integer.parseInt(stringToTest);
			return false;
		} catch (NumberFormatException e) {
			return true;
		}
	}

	private int[] findLongestEntryPerCollumn(List<List<String>> dataTable) {
		int[] longestEntry = new int[this.rowLength];
		for(int i = 0; i < this.rowLength; i++) {
			longestEntry[i] = this.collumnHeaders[i].length();
			for (List<String> row: dataTable) {
				if(longestEntry[i] < row.get(i).length()) {
					longestEntry[i] = row.get(i).length();
				}
			}
		}
		return longestEntry;
	}

	public List<List<String>> getDataTable() {
		return dataTable;
	}

	public Map<String, Object[]> getMetaData() {
		return metaData;
	}

	protected int[] getLongestEntryLengthPerCollumn() {
		return this.longestEntryPerCollumn;
	}

	protected boolean collumnIsNumeric(int collumnNumber) {
		return this.numericCollumns[collumnNumber-1];
	}

	
	protected String getRowToString(int index) {
		List<String> row = this.dataTable.get(index);
		return rowToString(row);
	}
	

	private String rowToString(List<String> row) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < row.size(); i++) {
			if (i == 0) {
				builder.append("|");
			}
			int collumnBreadth = this.longestEntryPerCollumn[i]+2;
			int entryLength = row.get(i).length();
			int amountOfPadding = collumnBreadth - entryLength;
			String padding = " ".repeat(amountOfPadding);
			builder.append(row.get(i)).append(padding).append("|");
		}
		builder.append("\n");
		return builder.toString();
	}

	public String getHeadersToString() {
		List<String> headersList = List.of(collumnHeaders);
		return rowToString(headersList);	
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(makePartitionString('_','_'));
		builder.append(getHeadersToString());
		builder.append(makePartitionString('=','|'));
		for (List<String> row: this.dataTable) {
			builder.append(rowToString(row));
		}
		builder.append(makePartitionString('_','|'));
		return builder.toString();
	}
	
	private Object makePartitionString(char filler, char collumnPartitioner) {
		List<String> nicePartition = new ArrayList<>();
		for (int i = 0; i < rowLength; i++) {
			nicePartition.add("");
		}
		String nicePartitionString = rowToString(nicePartition);
		nicePartitionString = nicePartitionString.replace(' ', filler);
		nicePartitionString = nicePartitionString.replace('|', collumnPartitioner);
		
		return nicePartitionString;
	}
}
