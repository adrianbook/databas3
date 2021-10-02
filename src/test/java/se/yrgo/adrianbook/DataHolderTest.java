package se.yrgo.adrianbook;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataHolderTest {

	int rowLength = 4;
	DataHolderBuilder builder;
	DataHolder dataHolder;
	
	private void loadDataHolderRowWithNRowsOfData(int numberOfRows) throws DataHolderBuilderException {
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < rowLength; j++) {
				int random = ThreadLocalRandom.current().nextInt(100);
				builder.loadData(j + 1, random + j);
			}
		}
	}

	@BeforeEach
	void setUp() throws Exception {
		builder = DataHolderFactory.instantiateWithGeneratedCollumnNames(rowLength);
		builder.loadData(1, "loooong text");
		builder.loadData(2, 1);
		builder.loadData(3, 4500);
		builder.loadData(4, "short text");
		builder.loadData(1, "plopp");
		builder.loadData(2, 5);
		builder.loadData(3, 77);
		builder.loadData(4, "plupp");
		
		dataHolder = builder.createDataHolder();
	}

	@AfterEach
	void tearDown() throws Exception {
		builder = null;
		dataHolder = null;
	}

	@Test
	void generateLongestEntryLengthPerCollumn() {
		int[] result = dataHolder.getLongestEntryLengthPerCollumn();
		assertEquals(12, result[0]);
		assertEquals(9, result[1]);
		assertEquals(9, result[2]);
		assertEquals(10, result[3]);
	}

	@Test
	void testCollumnIsNumeric() {
		assertFalse(dataHolder.collumnIsNumeric(1));
		assertTrue(dataHolder.collumnIsNumeric(2));
		assertTrue(dataHolder.collumnIsNumeric(3));
		assertFalse(dataHolder.collumnIsNumeric(4));
	}
	
	@Test
	void testRowToString() {
		String expectedString = "|loooong text  |1          |4500       |short text  |\n";
		String testString = dataHolder.getRowToString(0);
		assertEquals(expectedString, testString);
	}
	
	@Test
	void testHeadersToString() {
		String expectedString = "|Collumn 1     |Collumn 2  |Collumn 3  |Collumn 4   |\n";
		String testString = dataHolder.getHeadersToString();
		assertEquals(expectedString, testString);
	}
	
	@Test
	void testToString() {
		String expectedString = "_____________________________________________________\n"+
								"|Collumn 1     |Collumn 2  |Collumn 3  |Collumn 4   |\n"+
								"|==============|===========|===========|============|\n"+
				                "|loooong text  |1          |4500       |short text  |\n"+
				                "|plopp         |5          |77         |plupp       |\n"+
								"|______________|___________|___________|____________|\n";
		String testString = dataHolder.toString();
		System.out.println(testString);
		assertEquals(expectedString, testString);
	}
	

}
