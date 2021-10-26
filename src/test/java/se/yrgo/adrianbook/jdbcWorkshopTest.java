package se.yrgo.adrianbook;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@Disabled
class jdbcWorkshopTest {
	
	boolean testBool;
	
	String connectionUrl = "jdbc:sqlserver://localhost:1433;database=AdrianBook;" +
	        "encrypt=false;trustServerCertificate=false;loginTimeout=30;";
	Connection connection; 
	
	public jdbcWorkshopTest() throws SQLException {
		connection = DriverManager.getConnection(connectionUrl, "LibraryApp", "Analphab3tic");
	}

	@Disabled("test for dbuser 'Librarian'")
	@Test
	public void testSelectQuery() throws SQLException {
		PreparedStatement statement = connection.prepareStatement("SELECT Title, Borrower FROM OverdueBook30PlusDays");
		
		Map<String, List<String>> borrowedBooks = new HashMap<>();
		ResultSet results = statement.executeQuery();
		while(results.next()) {
			String borrower = results.getString(2);
			String title = results.getString(1);
			
			if(borrowedBooks.containsKey(borrower)) {
				List<String> titles = borrowedBooks.get(borrower);
				titles.add(title);
				borrowedBooks.put(borrower, titles);
				continue;
			}
			List<String> titles = new ArrayList<>();
			titles.add(title);
			borrowedBooks.put(borrower, titles); 
		}
		for(String borrower : borrowedBooks.keySet()) {
			System.out.printf("Books borrowed by %s:%n", borrower);
			for(String title : borrowedBooks.get(borrower)) {
				System.out.printf("         %s%n", title);
			}
		}
		
	}
	
	@Test
	public void testStoredProcedureExec() throws SQLException {
		ResultSet results = executeQuery("SELECT [Book_ID], [Title], [Author], [Genre] FROM listAvailableBooks");
		
		while(results.next()) {
			System.out.printf("title: %s author: %s%n",results.getString(2),results.getString(3));
		}
	}
	
	@Test
	public void testGettingHeaders() throws SQLException {
		ResultSet results = executeQuery("SELECT [Book_ID], [Title], [Author], [Genre] FROM listAvailableBooks");
		
		ResultSetMetaData meta = results.getMetaData();
		
		for (int i = 1; i <= meta.getColumnCount(); i++ ) 
			System.out.println(meta.getColumnLabel(i));
	}
	
	private ResultSet executeQuery(String query) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(query);
		return statement.executeQuery();
	}
	
	@Test
	void skoj() {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < 10 ; i++) list.add(i);
		
		System.out.println("list contents");
		for (Integer l: list) System.out.println(l);
		
		for (ListIterator<Integer> iter= list.listIterator(); iter.hasNext();) {
			int f = iter.next();
			f+=(Integer)f*2;
			iter.set(f);
		}
		
		for (Integer l: list) System.out.println(l);
	}
	
	private void varargsTest(String... args) {
		System.out.println(args[0]);
	}
	
	private void varargsTest(int... args) {
		System.out.println(args[3]);
	}
	
	private String testObjectToString(Object o) {
		
		return String.valueOf(o);
	}
	
	private void giveStringArgumentInt(String s) {
		String[] arr = new String[3];
		arr[0]=String.valueOf(s);
		System.out.println(s);
	}
	
	@Test
	void testList() {
		String a = "10";
		String b = "2";
		System.out.println("JÄMFÖR  "+a.compareTo(b));
	}
	
}
