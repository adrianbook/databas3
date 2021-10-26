package se.yrgo.adrianbook.dummies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import se.yrgo.adrianbook.databasecalls.DbCall;
import se.yrgo.adrianbook.dataholder.DataHolder;
import se.yrgo.adrianbook.dataholder.DataHolderBuilderFactory;
import se.yrgo.adrianbook.exceptions.DataHolderException;

public class DatabaseCallImplementationDummy implements DbCall {
	
	public DataHolder getBooks() {
		try {
			return DataHolderBuilderFactory
					.instantiateWithColumnNames("Books", "Gotten")
					.loadData(1, "book1")
					.loadData(2, "book2")
					.createDataHolder();
		} catch (DataHolderException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public DataHolder makeLoan(int i, int j) {
		DataHolder dh = DataHolderBuilderFactory
				.instantiateWithGeneratedColumnNames(i+j)
				.createDataHolder();
		
		return dh;
	}

	@Override
	public DataHolder returnLoan(int i) {
		return DataHolderBuilderFactory
				.instantiateWithColumnNames("Loan Returned")
				.createDataHolder();
	}

	@Override
	public DataHolder getBooksForBorrower(int i) {
		return DataHolderBuilderFactory
				.instantiateWithGeneratedColumnNames(i)
				.createDataHolder();
	}
}
