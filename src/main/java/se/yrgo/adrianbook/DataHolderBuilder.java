package se.yrgo.adrianbook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class DataHolderBuilder {
	
	private String[] activeRow;
	private List<List<String>> dataTable;
	private int[] longestEntryLengthPerCollumn;
	private String[] collumnHeaders;
	private final int rowLength;

	
	public DataHolderBuilder(String[] collumnHeaders) {
		super();
		this.rowLength = collumnHeaders.length;
		this.activeRow = new String[rowLength];
		this.dataTable = new ArrayList<>();
		this.longestEntryLengthPerCollumn = new int[rowLength];
		Arrays.fill(longestEntryLengthPerCollumn, 0);
		this.collumnHeaders = collumnHeaders;
	}
	
		
	private void loadFullRowIntoTable() {
		List<String> rowToStore = List.of(activeRow);
		activeRow = new String[rowLength];
		addToDataTable(rowToStore);	
	}

	private void addToDataTable(List<String> rowToStore) {
		dataTable.add(rowToStore);
	}

	
	public String[] getCopyOfCurrentRow() {
		return activeRow.clone();	
	}

	
	public List<List<String>> getCopyOfDataTable() {
		return List.copyOf(dataTable);
	}

	public int getRowLength() {
		return activeRow.length;
	}

	public String getDataTableToString() {
		StringBuilder builder = new StringBuilder();
		for (List<String> arr : dataTable) {
			builder.append(rowToString(arr)).append("\n");
		}
		return builder.toString();
	}

	public String getStoredRowToString(int index) {
		return rowToString(dataTable.get(index));
	}
	
	public String getActiveRowToString() {
		return rowToString(List.of(activeRow));
	}

	private String rowToString(List<String> row) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < row.size(); i++) {
			builder.append(row.get(i));
			if (i != row.size()-1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

	public int getLongestEntryLengthForCollumn(int collumnNumber) {
		return longestEntryLengthPerCollumn[collumnNumber];
	}

	public String[] getCopyOfCollumns() {
		return collumnHeaders.clone();
	}

	public void addCollumnHeaders(String... collumnHeaders) {
		this.collumnHeaders = collumnHeaders;
	}

	public void loadString(int collumnNumber, String dataString) throws DataHolderException {
			checkDataForCleanlinessAndLoadIt(collumnNumber-1, dataString);
	}

	public void loadInt(int collumnNumber, int dataInt) throws DataHolderException {
			checkDataForCleanlinessAndLoadIt(collumnNumber-1, Integer.toString(dataInt));
	}
	
	private void checkDataForCleanlinessAndLoadIt(int index, String dataString) throws DataHolderException {
		try {
			checkIfGivenCollumnIndexHolds(index);			
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new DataHolderException(String.format("Given collumn number %d is not in this DataHolder", index+1));
		}
		activeRow[index] = dataString;
		if (!Arrays.asList(activeRow).contains(null)) {
			loadFullRowIntoTable();
		}
	}

	private void checkIfGivenCollumnIndexHolds(int index) throws DataHolderException {
		if (activeRow[index] != null) {
			throw new DataHolderException(String.format("Same collumn can't be loaded twice. Collumn %d", index+1));
		}
	}

	public void loadString(String collumnName, String dataString) throws DataHolderException {
		checkCollumnNameValidityAndPassOn(collumnName, dataString);		
	}

	private void checkCollumnNameValidityAndPassOn(String collumnName, String dataString) throws DataHolderException {
		for (int i = 0; i < rowLength; i++) {
			if (collumnHeaders[i].equals(collumnName)) {
				checkDataForCleanlinessAndLoadIt(i, dataString);
				return;
			}
		}
		throw new DataHolderException(String.format("Unknown collumn name: %s", collumnName));
	}

	public void loadInt(String collumnName, int dataInt) throws DataHolderException {
		checkCollumnNameValidityAndPassOn(collumnName, Integer.toString(dataInt));
	}


}
