package se.yrgo.adrianbook;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class DataRetrievingObjectTest {
	
	private DataHolderBuilder dataHolderBuilder;
	private DataHolderBuilder dataHolder2;
	
	private int rowLength = 3;
	
	private void loadDataHolderRowWithNdataPoints(int numberOfDataPoints) throws DataHolderException {
		for(int i = 0; i < numberOfDataPoints; i++) {
			int random = ThreadLocalRandom.current().nextInt(100);
			dataHolderBuilder.loadInt(i+1, random+i);
		}
	}
	
	@BeforeEach
	void setUp() throws Exception {
		dataHolderBuilder	= DataHolderFactory.instantiateWithGeneratedCollumnNames(rowLength);
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
		dataHolderBuilder.loadString(1, "test1");
		String[] result = dataHolderBuilder.getCopyOfCurrentRow();
		assertEquals("test1", result[0]);
	}
	
	@Test
	void loadIntegerAndReturnAsString() throws DataHolderException {
		dataHolderBuilder.loadInt(1, 22);
		String[] result = dataHolderBuilder.getCopyOfCurrentRow();
		assertEquals("22", result[0]);
	}
	
	@Test
	void loadMultipleDataPoints() throws DataHolderException {
		dataHolderBuilder.loadInt(1, 33);
		dataHolderBuilder.loadString(2,"hej");
		
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
		dataHolderBuilder.loadString(1,"a");
		dataHolderBuilder.loadString(2,"b");
		dataHolderBuilder.loadString(3,"c");
		
		String firstRow = "a, b, c";
		
		dataHolderBuilder.loadInt(1,1);
		dataHolderBuilder.loadInt(2,2);
		dataHolderBuilder.loadInt(3,3);
		
		String secondRow = "1, 2, 3";
		
		loadDataHolderRowWithNdataPoints(rowLength);
		
		assertEquals(firstRow, dataHolderBuilder.getStoredRowToString(0));
		assertEquals(secondRow, dataHolderBuilder.getStoredRowToString(1));
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
	void storeCollumnNamesSeperately() {
		assertEquals("Collumn 1", dataHolderBuilder.getCopyOfCollumns()[0]);
	}
	
	
	@Test
	void loadDataWithSpecifiedIndex() throws DataHolderException {
		dataHolderBuilder.loadString(1, "test");
		dataHolderBuilder.loadInt(2, 3);
		
		assertEquals("test", dataHolderBuilder.getCopyOfCurrentRow()[0]);
		assertEquals("3", dataHolderBuilder.getCopyOfCurrentRow()[1]);
	}
	
	@Test
	void throwDataHolderExceptionIfIndexOutOfBounds() {
		String message = "";
		try {
			dataHolderBuilder.loadString(5, "test");
		}
		catch (DataHolderException e) {
			message = e.getLocalizedMessage();
		}
		assertEquals("Given collumn number 5 is not in this DataHolder", message);
	}
	
	@Test
	void throwExceptionIfAttemptIsMadeToLoadSameIndexTwice() throws DataHolderException {
		String message = "";
		try {
			dataHolderBuilder.loadString(1, "test");
			dataHolderBuilder.loadString(2, "3");
			dataHolderBuilder.loadString(2, "hej");
		}
		catch (DataHolderException e) {
			message = e.getLocalizedMessage();
		}
		assertEquals("Same collumn can't be loaded twice. Collumn 2", message);
	}
	
	@Test
	void leadTwoParameterLoadFunctionsIntoDataTable() throws DataHolderException {
		dataHolderBuilder.loadInt(1, 1);
		dataHolderBuilder.loadString(2,"hej");
		dataHolderBuilder.loadString(3,"hå");
		
		assertEquals("1, hej, hå", dataHolderBuilder.getStoredRowToString(0));
	}
	
	@Test
	void loadDataWithSpecifiedCollumnName() throws DataHolderException {
		dataHolderBuilder.loadString("Collumn 1", "test");
		dataHolderBuilder.loadInt("Collumn 2", 3);
		
		assertEquals("test", dataHolderBuilder.getCopyOfCurrentRow()[0]);
		assertEquals("3", dataHolderBuilder.getCopyOfCurrentRow()[1]);
	}
	
	@Test
	void throwDataHolderExceptionIfCollumnNameNonsense() {
		assertThrows(DataHolderException.class, ()-> {
			dataHolderBuilder.loadInt("bajs",3);
		});
	}
}
