package se.yrgo.adrianbook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;

import se.yrgo.adrianbook.databasecalls.DbCall;
import se.yrgo.adrianbook.databasecalls.DbCallImplementation;
import se.yrgo.adrianbook.dataholder.DataHolder;
import se.yrgo.adrianbook.exceptions.ArgumentParserException;
import se.yrgo.adrianbook.exceptions.DataHolderException;
import se.yrgo.adrianook.argumentsparser.ArgumentsParser;

public class App {
	private static final String USERNAME = "LibraryApp";
	private static final String PASSWORD = "Analphab3tic";
	private static final String DB = "AdrianBook";
	private static	DbCall call = null;
	private static ArgumentsParser argParser = new ArgumentsParser("lubr");
	
	public static void main(String[] args) throws InterruptedException {
		try {
			call = new DbCallImplementation(DB, USERNAME, PASSWORD);
			argParser.parseArguments(args);
			List<String> flags = argParser.getFlags();
			
			if (flags.contains("l")) {
				listBooks();
			} 
			else if (flags.contains("b")) {
				if (!flags.contains("u")) {
					printInstructions();
					return;
				}
				makeLoan();
			}
			else if (flags.contains("r")) {
				returnBook();
			}
			else if (flags.contains("u")) {
				listLoansForBorrower();
			}
			else {
				printInstructions();
			}
		} catch (ArgumentParserException e) {
			System.out.println(e.getLocalizedMessage());
			printInstructions();
		} catch (DataHolderException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private static void listBooks() throws DataHolderException, ArgumentParserException {
		DataHolder holder = call.getBooks();
		if (argParser.hasParameterFor("l")) {
			int sortingCollumn = argParser.getParameterForFlag("l");
			holder.sortOnColumn(sortingCollumn);
		}
		System.out.println(holder);
	}
	
	private static void makeLoan() throws ArgumentParserException {
		if (!(argParser.getNumbersOfParametersForFlag("u")==1)) {
			throw new ArgumentParserException("Can only process loans for one borrower at a time!");
		}
		DataHolder holder;
		int borrower = argParser.getParameterForFlag("u");
		do {
			int book = argParser.getParameterForFlag("b");
			holder = call.makeLoan(book, borrower);
			System.out.println(holder);
		} while (argParser.hasParameterFor("b"));	
	}

	private static void returnBook() throws ArgumentParserException {
		DataHolder holder;
		do {
			int book = argParser.getParameterForFlag("r");
			holder = call.returnLoan(book);
			System.out.println(holder);
		} while (argParser.hasParameterFor("r"));	
	}
	
	private static void listLoansForBorrower() throws ArgumentParserException, DataHolderException {
		DataHolder holder;
		do {
			int borrower = argParser.getParameterForFlag("u");
			holder = call.getBooksForBorrower(borrower);
			System.out.println(holder);
		} while (argParser.hasParameterFor("u"));		
	}
	
	private static void printInstructions() {
		String usage = "\n\nUSAGE:\n\n"
				+"java -jar libraryapp.jar + one of the options below\n\n"
				+ "-l\t\t\tList available books. Supply option to sort books on"
				+"<1> BookID <2> Title <3> Author <4> Genre\n"
				+"-b <bookID> -u <userID>\tMake loan. You may supply multiple books, but only execute loans for one borrower at a time\n"
				+"-r <bookID>\t\tReturn book. You may return mutiple books at the same time.\n"
				+"-u <userID>\t\tList loans for user. You may list loans for multiple borrowers.\n";
		System.out.println(usage);	
	}
}

