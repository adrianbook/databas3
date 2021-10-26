package se.yrgo.adrianbook.dataholder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.yrgo.adrianbook.exceptions.DataHolderException;


public class DataHolderBuilder {
	
	private String[] activeRow;
	private List<List<String>> dataTable;
	private String[] columnHeaders;
	private final int rowLength;
	
	protected DataHolderBuilder(String[] columnHeaders) {
		super();
		this.rowLength = columnHeaders.length;
		this.activeRow = new String[rowLength];
		this.dataTable = new ArrayList<>();
		this.columnHeaders = columnHeaders;
	}
	
	//Methods to create DataHolder object
	
	public DataHolderBuilder loadData(int columnNumber, int dataInt) throws DataHolderException {
		loadData(columnNumber, Integer.toString(dataInt));
		return this;
	}
	
	public DataHolderBuilder loadData(int columnNumber, String dataString) throws DataHolderException {
		checkIndexAndStoreData(columnNumber-1, dataString);
		return this;
	}
	
	public DataHolderBuilder loadData(String columnName, int dataInt) throws DataHolderException {
		loadData(columnName, Integer.toString(dataInt));
		return this;
	}
		
	public DataHolderBuilder loadData(String columnName, String dataString) throws DataHolderException {
		int index = getIndexForColumnName(columnName);
		checkIndexAndStoreData(index, dataString);
		return this;
	}
	
	private int getIndexForColumnName(String columnName) throws DataHolderException {
		for (int i = 0; i < rowLength; i++) {
			if (columnHeaders[i].equals(columnName)) {
				return i;
			}
		}
		throw new DataHolderException(String.format("Unknown column name: %s", columnName));
	}
	
	private void checkIndexAndStoreData(int index, String dataString) throws DataHolderException {
		checkRowIndex(index);
		addDataToActiveRowAndStoreRowIfFull(index, dataString);
	}

	private void checkRowIndex(int index) throws DataHolderException {
		try {
			checkIfGivenColumnIndexAllreadyLoaded(index);			
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new DataHolderException(String.format("Given column number %d is not in this DataHolder", index+1));
		}
	}

	private void checkIfGivenColumnIndexAllreadyLoaded(int index) throws DataHolderException {
		if (activeRow[index] != null) {
			throw new DataHolderException(String.format("Same column can't be loaded twice. Column %d", index+1));
		}
	}
	
	private void addDataToActiveRowAndStoreRowIfFull(int index, String dataString) {
		activeRow[index] = dataString.trim();
		if (!Arrays.asList(activeRow).contains(null)) {
			loadFullRowIntoTable();
		}
	}
	
	private void loadFullRowIntoTable() {
		List<String> rowToStore = List.of(activeRow);
		activeRow = new String[rowLength];
		addToDataTable(rowToStore);	
	}
	
	// Method for storing uncompleted rows if necessary
	public void storeActiveRow() {
		for(int i = 0; i < rowLength; i++) {
			if(activeRow[i] == null) {
				activeRow[i] = "";
			}
		}
		loadFullRowIntoTable();
	}
	
	private void addToDataTable(List<String> rowToStore) {
		dataTable.add(rowToStore);
	}

	public DataHolder createDataHolder() {
		return new DataHolder(this.dataTable, this.columnHeaders);
	}
	
	public DataHolder createDataHolder(Object... metaData) {
		return new DataHolder(this.dataTable, this.columnHeaders, metaData);
	}
	
	// Getters for testing purposes
	
	protected String[] getCopyOfCurrentRow() {
		return activeRow.clone();	
	}

	
	protected List<List<String>> getCopyOfDataTable() {
		return List.copyOf(dataTable);
	}

	protected int getRowLength() {
		return activeRow.length;
	}

	protected String getDataTableToString() {
		StringBuilder builder = new StringBuilder();
		for (List<String> arr : dataTable) {
			builder.append(rowToString(arr)).append("\n");
		}
		return builder.toString();
	}

	protected String getStoredRowToString(int index) {
		return rowToString(dataTable.get(index));
	}
	
	protected String getActiveRowToString() {
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

	protected String[] getCopyOfColumnHeaders() {
		return columnHeaders.clone();
	}
	

}
