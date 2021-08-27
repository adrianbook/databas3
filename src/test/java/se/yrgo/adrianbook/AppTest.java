package se.yrgo.adrianbook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import se.yrgo.adrianbook.dummies.DatabaseCallImplementationDummy;

class AppTest {
	
	private static DbCall db;
	private static String user;
	private static String password;
	 
	@BeforeAll
	private static void buildTestObjects() {
		db = new DatabaseCallImplementationDummy();
	}

	@Test
	void testQueryBooks() {		
		String controllString = "books gotten";
		String testString = db.getBooks();
		assertEquals(controllString, testString);
	}
	
	@Test
	void testQueryLender() {	
		String controllString = "values is ok 2 4";
		String testString = db.makeLoan(2, 4);
		assertEquals(controllString, testString);
	}
	
	@Test
	void testReturnBook() {
		String controllString = "returned";
		String testString = db.returnLoan();
		assertEquals(controllString, testString);
	}
	
	@Test
	void testListBooksByLender() {
		String controllString = "lenderId 1";
		String testString = db.getBooksForLender(1);	
		assertEquals(controllString, testString);
	}

}
