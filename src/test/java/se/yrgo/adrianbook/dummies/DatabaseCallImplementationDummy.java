package se.yrgo.adrianbook.dummies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import se.yrgo.adrianbook.DbCall;

public class DatabaseCallImplementationDummy implements DbCall {
	
	public String getBooks() {
		return "books gotten";
	}

	@Override
	public String makeLoan(int i, int j) {
		return String.format("values is ok %d %d", i, j);
	}

	@Override
	public String returnLoan() {
		return "returned";
	}

	@Override
	public String getBooksForLender(int i) {
		return "lenderId "+i;
	}
}
