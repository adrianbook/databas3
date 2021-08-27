package se.yrgo.adrianbook;

public class DataHolderFactory {
	public static DataHolderBuilder instantiateWithCollumnNames(String... collumnHeaders) {
		return new DataHolderBuilder(collumnHeaders);
	}
	
	public static DataHolderBuilder instantiateWithGeneratedCollumnNames(int numberOfCollumns) {
		String[] collumnHeaders = new String[numberOfCollumns];
		for (int i = 0; i < numberOfCollumns; i++) {
			collumnHeaders[i] = String.format("Collumn %d", i+1);
		}
		return new DataHolderBuilder(collumnHeaders);
	}

}
