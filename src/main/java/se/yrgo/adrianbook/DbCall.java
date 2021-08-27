package se.yrgo.adrianbook;

public interface DbCall {
	
	String getBooks();

	String makeLoan(int i, int j);

	String returnLoan();

	String getBooksForLender(int i);

}
