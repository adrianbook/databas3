package se.yrgo.adrianbook.databasecalls;

import se.yrgo.adrianbook.dataholder.DataHolder;
import se.yrgo.adrianbook.exceptions.DataHolderException;

public interface DbCall {
	
	DataHolder getBooks() throws DataHolderException;

	DataHolder makeLoan(int bookId, int borrowerId);

	DataHolder getBooksForBorrower(int borrowerId) throws DataHolderException;

	DataHolder returnLoan(int bookId);

}
