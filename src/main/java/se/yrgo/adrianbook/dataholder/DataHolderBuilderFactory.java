package se.yrgo.adrianbook.dataholder;

public class DataHolderBuilderFactory {
	public static DataHolderBuilder instantiateWithColumnNames(String... columnHeaders) {
		return new DataHolderBuilder(columnHeaders);
	}
	
	public static DataHolderBuilder instantiateWithGeneratedColumnNames(int numberOfColumns) {
		String[] columnHeaders = new String[numberOfColumns];
		for (int i = 0; i < numberOfColumns; i++) {
			columnHeaders[i] = String.format("Column %d", i+1);
		}
		return new DataHolderBuilder(columnHeaders);
	}

}
