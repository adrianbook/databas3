package se.yrgo.adrianbook.databasecalls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import se.yrgo.adrianbook.dataholder.DataHolder;
import se.yrgo.adrianbook.dataholder.DataHolderBuilder;
import se.yrgo.adrianbook.dataholder.DataHolderBuilderFactory;
import se.yrgo.adrianbook.exceptions.DataHolderException;


public class DbCallImplementation implements DbCall {
	
	Connection connection;
	
	public DbCallImplementation(String db, String userName, String password) throws Exception {
		super();
		String connectionUrl = String.format(
				"jdbc:sqlserver://localhost:1433;database=%s;encrypt=false;trustServerCertificate=false;loginTimeout=30;", db);
		connection = DriverManager.getConnection(connectionUrl, userName, password);
	}
	
	public DbCallImplementation(Connection connection) {
		this.connection = connection;
	}

	@Override
	public DataHolder getBooks() throws DataHolderException {
		
		try (Statement statement = connection.createStatement()){ 
		statement.execute("SELECT * FROM listAvailableBooks");
		ResultSet rs = statement.getResultSet();
		ResultSetMetaData md = rs.getMetaData();
		String[] headers = new String[md.getColumnCount()];
		for (int i = 0; i < headers.length; i++) {
			headers[i] = md.getColumnLabel(i+1);
		}
		DataHolderBuilder builder = DataHolderBuilderFactory.instantiateWithColumnNames(headers);
		
		while (rs.next()) {
			for (String s : headers) {
				s = s.trim();
				builder.loadData(s, rs.getString(s));
			}
		}
		return builder.createDataHolder("Available books:");		
		} catch (SQLException e) {
			e.printStackTrace();
			return DataHolderBuilderFactory.instantiateWithGeneratedColumnNames(0).createDataHolder("Error looking up books:"+e.getMessage());
		}
	}

	@Override
	public DataHolder makeLoan(int book, int borrower) {
		DataHolderBuilder builder = DataHolderBuilderFactory.instantiateWithGeneratedColumnNames(0);
		try (PreparedStatement statement = connection.prepareStatement("EXECUTE lendBook ?, ?")){
			statement.setInt(1, book);
			statement.setInt(2, borrower);
			int result = statement.executeUpdate();
			
			switch(result) {
				case 0:
					return builder.createDataHolder("Loan not executed");
				case 1:
					return builder.createDataHolder("Loan executed");
				default:
					return builder.createDataHolder(String.format(
							"Problem executing operation. %d rows affected in database. Contact database administrator", result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return builder.createDataHolder(String.format("Error occured excecuting operation: %s", e.getMessage()));
		}
	}

	
	@Override
	public DataHolder returnLoan(int book) {
		DataHolderBuilder builder = DataHolderBuilderFactory.instantiateWithGeneratedColumnNames(0);
		try (PreparedStatement statement = connection.prepareStatement("EXECUTE returnBook ?")){
			statement.setInt(1, book);
			int result = statement.executeUpdate();
			
			switch(result) {
				case 0:
					return builder.createDataHolder("Return not executed");
				case 1:
					return builder.createDataHolder("Return executed");
				default:
					return builder.createDataHolder(String.format(
							"Problem executing operation. %d rows affected in database. Contact database administrator", result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return builder.createDataHolder(String.format("Error occured excecuting operation: %s", e.getMessage()));
		}
	}

	@Override
	public DataHolder getBooksForBorrower(int borrower) throws DataHolderException {
		String queryString = "SELECT [Borrower_name], [Borrower_email], [Book_id], [Title], [Author]"+
				"FROM listBorrowedBooksByBorrower WHERE [Borrower_id] = ?";
		try (PreparedStatement statement = connection.prepareStatement(queryString)) {
			statement.setInt(1, borrower);
			ResultSet rs = statement.executeQuery();
			
			DataHolderBuilder metaHolderBuilder = DataHolderBuilderFactory.instantiateWithColumnNames("Name","Email");
			DataHolderBuilder dataHolderBuilder = DataHolderBuilderFactory.instantiateWithColumnNames("Book ID","Title", "Author");
			
			rs.next();
			DataHolder metaHolder = metaHolderBuilder
				.loadData("Name", rs.getString("Borrower_name"))
				.loadData("Email", rs.getString("Borrower_Email"))
				.createDataHolder();
			dataHolderBuilder
				.loadData("Book ID", rs.getString("Book_id"))
				.loadData("Title", rs.getString("Title"))
				.loadData("Author", rs.getString("Author"));
			
			while (rs.next()) {
				dataHolderBuilder
					.loadData("Book ID", rs.getString("Book_id"))
					.loadData("Title", rs.getString("Title"))
					.loadData("Author", rs.getString("Author"));
			}
			
			DataHolder holder = dataHolderBuilder.createDataHolder("Borrower:", metaHolder, "Books borrowed:");
			return holder;
		} catch (SQLException e) {
			return DataHolderBuilderFactory
					.instantiateWithGeneratedColumnNames(0)
					.createDataHolder("Error occured listing loans :"+e.getMessage());
		}
	}

}
