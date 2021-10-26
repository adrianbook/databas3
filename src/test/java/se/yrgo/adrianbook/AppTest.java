package se.yrgo.adrianbook;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import se.yrgo.adrianbook.databasecalls.DbCall;
import se.yrgo.adrianbook.databasecalls.DbCallImplementation;
import se.yrgo.adrianbook.dataholder.DataHolder;
import se.yrgo.adrianbook.dummies.DatabaseCallImplementationDummy;
import se.yrgo.adrianbook.exceptions.DataHolderException;


class AppTest {
	
	private DbCall db;
	private static final String USERNAME = "LibraryApp";
	private static final String PASSWORD = "Analphab3tic";
	private static final String DB = "AdrianBook";
	 
	AppTest() {
		db = new DatabaseCallImplementationDummy();
	}

	@Test
	void testQueryBooks() throws DataHolderException {		
		DataHolder holder = db.getBooks();
		assertEquals(1, holder.getDataTable().size());
	}
	
	@Test
	@Order(1)
	void testQueryLender() throws Exception {
		db= new DbCallImplementation(DB, USERNAME, PASSWORD);
		String controllString = "Loan executed\n";
		DataHolder holder = db.makeLoan(5, 1);
		System.out.println(holder);
		assertEquals(controllString, holder.toString());
	}
	
	@Test
	@Order(2)
	void testReturnBook() throws Exception {
		db= new DbCallImplementation(DB, USERNAME, PASSWORD);
		String controllString = "Return executed\n";
		DataHolder holder = db.returnLoan(5);
		System.out.println(holder);
		assertEquals(controllString, holder.toString());
	}
	
	@Test
	void testListBooksByLender() throws Exception {
		db= new DbCallImplementation(DB, USERNAME, PASSWORD);
		String controllString = "Return executed\n";
		DataHolder holder = db.getBooksForBorrower(2);
		System.out.println(holder);
	}

}
