package se.yrgo.adrianbook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DataHolderBuilder {
	
	private String[] activeRow;
	private List<List<String>> dataTable;
	private String[] collumnHeaders;
	private final int rowLength;
	
	public DataHolderBuilder(String[] collumnHeaders) {
		super();
		this.rowLength = collumnHeaders.length;
		this.activeRow = new String[rowLength];
		this.dataTable = new ArrayList<>();
		this.collumnHeaders = collumnHeaders;
	}
	
	public void loadData(int collumnNumber, int dataInt) throws DataHolderBuilderException {
		loadData(collumnNumber, Integer.toString(dataInt));
	}
	
	public void loadData(int collumnNumber, String dataString) throws DataHolderBuilderException {
		checkDataForCleanlinessAndLoadIt(collumnNumber-1, dataString);
	}
	
	public void loadData(String collumnName, int dataInt) throws DataHolderBuilderException {
		loadData(collumnName, Integer.toString(dataInt));
	}
		
	public void loadData(String collumnName, String dataString) throws DataHolderBuilderException {
		int index = getIndexForCollumnName(collumnName);
		checkDataForCleanlinessAndLoadIt(index, dataString);
	}
	
	private int getIndexForCollumnName(String collumnName) throws DataHolderBuilderException {
		for (int i = 0; i < rowLength; i++) {
			if (collumnHeaders[i].equals(collumnName)) {
				return i;
			}
		}
		throw new DataHolderBuilderException(String.format("Unknown collumn name: %s", collumnName));
	}
	
	private void checkDataForCleanlinessAndLoadIt(int index, String dataString) throws DataHolderBuilderException {
		checkRowIndex(index);
		addDataToActiveRowAndStoreRowIfFull(index, dataString);
	}

	private void checkRowIndex(int index) throws DataHolderBuilderException {
		try {
			checkIfGivenCollumnIndexAllreadyLoaded(index);			
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new DataHolderBuilderException(String.format("Given collumn number %d is not in this DataHolder", index+1));
		}
	}

	private void addDataToActiveRowAndStoreRowIfFull(int index, String dataString) {
		activeRow[index] = dataString;
		if (!Arrays.asList(activeRow).contains(null)) {
			loadFullRowIntoTable();
		}
	}
	
	private void checkIfGivenCollumnIndexAllreadyLoaded(int index) throws DataHolderBuilderException {
		if (activeRow[index] != null) {
			throw new DataHolderBuilderException(String.format("Same collumn can't be loaded twice. Collumn %d", index+1));
		}
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

	public String[] getCopyOfCollumns() {
		return collumnHeaders.clone();
	}
	
	public DataHolder createDataHolder() {
		return new DataHolder(this.dataTable, this.collumnHeaders);
	}

	public void storeActiveRow() {
		for(int i = 0; i < rowLength; i++) {
			if(activeRow[i] == null) {
				activeRow[i] = "";
			}
		}
		loadFullRowIntoTable();
	}

}
