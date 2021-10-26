package se.yrgo.adrianbook.dataholder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import se.yrgo.adrianbook.dataholder.DataHolder;
import se.yrgo.adrianbook.exceptions.DataHolderException;

class DataHolderBuilderTest {
	
	private DataHolderBuilder dataHolderBuilder;
	private DataHolderBuilder dataHolder2;
	
	private int rowLength = 3;
	
	private void loadDataHolderRowWithNdataPoints(int numberOfDataPoints) throws DataHolderException {
		for(int i = 0; i < numberOfDataPoints; i++) {
			int random = ThreadLocalRandom.current().nextInt(100);
			dataHolderBuilder.loadData(i+1, random+i);
		}
	}
	
	@BeforeEach
	void setUp() throws Exception {
		dataHolderBuilder	= DataHolderBuilderFactory.instantiateWithGeneratedColumnNames(rowLength);
	}

	@AfterEach
	void tearDown() throws Exception {
		dataHolderBuilder = null;
	}

	@Test
	void getSingleDataPoint() {
		String[] result = dataHolderBuilder.getCopyOfCurrentRow();
		assertEquals(null , result[0]);
	}
	
	@Test
	void loadAndRetrieveSingleDataPoint() throws DataHolderException {
		dataHolderBuilder.loadData(1, "test1");
		String[] result = dataHolderBuilder.getCopyOfCurrentRow();
		assertEquals("test1", result[0]);
	}
	
	@Test
	void loadIntegerAndReturnAsString() throws DataHolderException {
		dataHolderBuilder.loadData(1, 22);
		String[] result = dataHolderBuilder.getCopyOfCurrentRow();
		assertEquals("22", result[0]);
	}
	
	@Test
	void loadMultipleDataPoints() throws DataHolderException {
		dataHolderBuilder.loadData(1, 33);
		dataHolderBuilder.loadData(2,"hej");
		
		String[] results = dataHolderBuilder.getCopyOfCurrentRow();
		String result1 = results[0];
		String result2 = results[1];
		
		assertEquals("33",result1);
		assertEquals("hej",result2);
	}
	
	
	@Test
	void setRowLength() {
		assertTrue(dataHolderBuilder.getRowLength() == 3);
	}
	
	

	@Test
	void saveFullRows() throws DataHolderException {
		dataHolderBuilder.loadData(1,"a");
		dataHolderBuilder.loadData(2,"b");
		dataHolderBuilder.loadData(3,"c");
		
		String firstRow = "a, b, c";
		
		dataHolderBuilder.loadData(1,1);
		dataHolderBuilder.loadData(2,2);
		dataHolderBuilder.loadData(3,3);
		
		String secondRow = "1, 2, 3";
		
		loadDataHolderRowWithNdataPoints(rowLength);
		
		assertEquals(firstRow, dataHolderBuilder.getStoredRowToString(0));
		assertEquals(secondRow, dataHolderBuilder.getStoredRowToString(1));
	}
	
	@Test
	void saveUncompletedRowsIfPrompted() throws DataHolderException {
		dataHolderBuilder.loadData(1, "hej");
		dataHolderBuilder.loadData(3, "hopp");
		
		dataHolderBuilder.storeActiveRow();
		
		String result = dataHolderBuilder.getStoredRowToString(0);
		
		assertEquals("hej, , hopp", result);
	}
	
	@Test
	void newRowsAddedToDataTable() throws DataHolderException {
		loadDataHolderRowWithNdataPoints(rowLength);
		assertEquals(1, dataHolderBuilder.getCopyOfDataTable().size());
		
		loadDataHolderRowWithNdataPoints(rowLength);
		loadDataHolderRowWithNdataPoints(rowLength);
		assertEquals(3, dataHolderBuilder.getCopyOfDataTable().size());
	}
	
	@Test
	void storeColumnNamesSeperately() {
		assertEquals("Column 1", dataHolderBuilder.getCopyOfColumnHeaders()[0]);
	}
	
	
	@Test
	void loadDataWithSpecifiedIndex() throws DataHolderException {
		dataHolderBuilder.loadData(1, "test");
		dataHolderBuilder.loadData(2, 3);
		
		assertEquals("test", dataHolderBuilder.getCopyOfCurrentRow()[0]);
		assertEquals("3", dataHolderBuilder.getCopyOfCurrentRow()[1]);
	}
	
	@Test
	void throwDataHolderExceptionIfIndexOutOfBounds() {
		String message = "";
		try {
			dataHolderBuilder.loadData(5, "test");
		}
		catch (DataHolderException e) {
			message = e.getLocalizedMessage();
		}
		assertEquals("Given column number 5 is not in this DataHolder", message);
	}
	
	@Test
	void throwExceptionIfAttemptIsMadeToLoadSameIndexTwice() throws DataHolderException {
		String message = "";
		try {
			dataHolderBuilder.loadData(1, "test");
			dataHolderBuilder.loadData(2, "3");
			dataHolderBuilder.loadData(2, "hej");
		}
		catch (DataHolderException e) {
			message = e.getLocalizedMessage();
		}
		assertEquals("Same column can't be loaded twice. Column 2", message);
	}
	
	@Test
	void leadTwoParameterLoadFunctionsIntoDataTable() throws DataHolderException {
		dataHolderBuilder.loadData(1, 1);
		dataHolderBuilder.loadData(2,"hej");
		dataHolderBuilder.loadData(3,"hå");
		
		assertEquals("1, hej, hå", dataHolderBuilder.getStoredRowToString(0));
	}
	
	@Test
	void loadDataWithSpecifiedColumnName() throws DataHolderException {
		dataHolderBuilder.loadData("Column 1", "test");
		dataHolderBuilder.loadData("Column 2", 3);
		
		assertEquals("test", dataHolderBuilder.getCopyOfCurrentRow()[0]);
		assertEquals("3", dataHolderBuilder.getCopyOfCurrentRow()[1]);
	}
	
	@Test
	void throwDataHolderExceptionIfColumnNameNonsense() {
		assertThrows(DataHolderException.class, ()-> {
			dataHolderBuilder.loadData("bajs",3);
		});
	}
	
	@Test
	void makeDataHolder() throws DataHolderException {
		dataHolderBuilder.loadData(1, "hej");
		dataHolderBuilder.loadData(2, "då");
		dataHolderBuilder.loadData(3, "aj");
		
		loadDataHolderRowWithNdataPoints(rowLength);
		DataHolder dh = dataHolderBuilder.createDataHolder();
		String result = dh.getRowToString(0);
		
		assertEquals("|hej       |då        |aj        |\n", result);
	}
	
		
}
