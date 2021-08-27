package se.yrgo.adrianbook;

import java.sql.Connection;

public class DbCallImplementation implements DbCall {
	Connection connection;
	
	

	public DbCallImplementation() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getBooks() {
		// TODO Auto-generated method stub
		return "test";
	}

	@Override
	public String makeLoan(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String returnLoan() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBooksForLender(int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
