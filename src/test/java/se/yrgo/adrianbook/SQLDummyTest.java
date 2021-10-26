package se.yrgo.adrianbook;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.yrgo.adrianbook.dataholder.DataHolder;
import se.yrgo.adrianbook.dataholder.DataHolderBuilder;
import se.yrgo.adrianbook.dataholder.DataHolderBuilderFactory;
import se.yrgo.adrianbook.dummies.ResultSetDummy;
import se.yrgo.adrianbook.exceptions.DataHolderException;

class SQLDummyTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() throws SQLException, DataHolderException {
		ResultSet rs = new ResultSetDummy("ni talar bra latin");
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
		DataHolder holder = builder.createDataHolder();
		
		holder.sortOnColumn(2,1);
		
		System.out.println(holder);
	}
	
	@Test
	void testy() {
		List<String> l1 = new ArrayList<>();
		List<String> l2 = new ArrayList<>();
		l1.add("hej");
		l2.add("hej");
		l1.add("4");
		l2.add("4");
		
		assertTrue(l1.equals(l2));
	}

}
