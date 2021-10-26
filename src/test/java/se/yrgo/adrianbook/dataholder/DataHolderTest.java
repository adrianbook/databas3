package se.yrgo.adrianbook.dataholder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.yrgo.adrianbook.dataholder.DataHolder;
import se.yrgo.adrianbook.exceptions.DataHolderException;

class DataHolderTest {

	int rowLength;
	DataHolderBuilder builder;
	DataHolder dataHolder;

	private void loadDataHolderRowWithNRowsOfData(int numberOfRows) throws DataHolderException {
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < rowLength; j++) {
				int random = ThreadLocalRandom.current().nextInt(100);
				builder.loadData(j + 1, random + j);
			}
		}
	}

	@BeforeEach
	void setUp() throws Exception {
		rowLength = 4;
		builder = DataHolderBuilderFactory.instantiateWithGeneratedColumnNames(rowLength);
		builder.loadData(1, "loooong text");
		builder.loadData(2, 1);
		builder.loadData(3, "0jdå");
		builder.loadData(4, "short text");
		builder.loadData(1, "plopp");
		builder.loadData(2, 5);
		builder.loadData(3, 77);
		builder.loadData(4, "plupp");
		builder.loadData(1, "adrian");
		builder.loadData(2, 4);
		builder.loadData(3, 567);
		builder.loadData(4, 666);

		dataHolder = builder.createDataHolder();
	}

	@AfterEach
	void tearDown() throws Exception {
		builder = null;
		dataHolder = null;
	}

	@Test
	void generateLongestEntryLengthPerColumn() {
		int[] result = dataHolder.getLongestEntryLengthPerColumn();
		assertEquals(12, result[0]);
		assertEquals(8, result[1]);
		assertEquals(8, result[2]);
		assertEquals(10, result[3]);
	}

	@Test
	void testColumnIsNumeric() {
		assertFalse(dataHolder.columnIsNumeric(1));
		assertTrue(dataHolder.columnIsNumeric(2));
		assertFalse(dataHolder.columnIsNumeric(3));
		assertFalse(dataHolder.columnIsNumeric(4));
	}

	@Test
	void testRowToString() {
		String expectedString = "|loooong text  |1         |0jdå      |short text  |\n";
		String testString = dataHolder.getRowToString(0);
		assertEquals(expectedString, testString);
	}

	@Test
	void testHeadersToString() {
		String expectedString = "|Column 1      |Column 2  |Column 3  |Column 4    |\n";
		String testString = dataHolder.getColumnHeadersToString();
		assertEquals(expectedString, testString);
	}

	@Test
	void testToString() {
		String expectedString = ".______________.__________.__________.____________.\n"
				+ "|Column 1      |Column 2  |Column 3  |Column 4    |\n"
				+ "|==============|==========|==========|============|\n"
				+ "|loooong text  |1         |0jdå      |short text  |\n"
				+ "|plopp         |5         |77        |plupp       |\n"
				+ "|adrian        |4         |567       |666         |\n"
				+ "+==============+==========+==========+============+\n";
		String testString = dataHolder.toString();
		assertEquals(expectedString, testString);
	}

	@Test
	void testSortingRowsWithColumnName() {
		String expectedString = ".______________.__________.__________.____________.\n"
				+ "|Column 1      |Column 2  |Column 3  |Column 4    |\n"
				+ "|==============|==========|==========|============|\n"
				+ "|adrian        |4         |567       |666         |\n"
				+ "|plopp         |5         |77        |plupp       |\n"
				+ "|loooong text  |1         |0jdå      |short text  |\n"
				+ "+==============+==========+==========+============+\n";
		dataHolder.sortOnColumn("Column 4");

		String testString = dataHolder.toString();
		assertEquals(expectedString, testString);
	}

	@Test
	void testSortingRowsWithColumnNumber() {
		String expectedString = ".______________.__________.__________.____________.\n"
				+ "|Column 1      |Column 2  |Column 3  |Column 4    |\n"
				+ "|==============|==========|==========|============|\n"
				+ "|loooong text  |1         |0jdå      |short text  |\n"
				+ "|adrian        |4         |567       |666         |\n"
				+ "|plopp         |5         |77        |plupp       |\n"
				+ "+==============+==========+==========+============+\n";
		dataHolder.sortOnColumn(2);

		String testString = dataHolder.toString();
		assertEquals(expectedString, testString);
	}
	
	@Test
	void testReverseSortingOrder() {
		String expectedString = ".______________.__________.__________.____________.\n"
				+ "|Column 1      |Column 2  |Column 3  |Column 4    |\n"
				+ "|==============|==========|==========|============|\n"
				+ "|plopp         |5         |77        |plupp       |\n"
				+ "|adrian        |4         |567       |666         |\n"
				+ "|loooong text  |1         |0jdå      |short text  |\n"
				+ "+==============+==========+==========+============+\n";
		dataHolder.sortOnColumn(2, -1);
		
		String testString = dataHolder.toString();
		assertEquals(expectedString, testString);
	}

	@Test
	void testEqualsHolds() throws DataHolderException {
		builder = DataHolderBuilderFactory.instantiateWithColumnNames("hej", "hopp");
		rowLength = 2;
		builder.loadData(1, 450).loadData(2, "hej");
		builder.loadData(1, 123).loadData(2, "då");
		DataHolder holder1 = builder.createDataHolder();

		builder = DataHolderBuilderFactory.instantiateWithColumnNames("hej", "hopp");
		builder.loadData(1, 2).loadData(2, "hej");
		builder.loadData(1, 34675).loadData(2, "då");
		DataHolder holder2 = builder.createDataHolder();

		holder1.sortOnColumn(2, 1);
		holder2.sortOnColumn(2, 1);

		// Row.equals returns true as long as comparing column values equals
		assertTrue(holder1.equals(holder2));
		assertEquals(holder1.hashCode(), holder2.hashCode());
	}

	@Test
	void testEqualsFails() throws DataHolderException {
		builder = DataHolderBuilderFactory.instantiateWithColumnNames("hej", "hopp");
		rowLength = 2;
		builder.loadData(1, "sträng").loadData(2, "hej");
		builder.loadData(1, "bokstav").loadData(2, "då");
		DataHolder holder1 = builder.createDataHolder();

		builder = DataHolderBuilderFactory.instantiateWithColumnNames("hej", "hopp");
		builder.loadData(1, 45).loadData(2, "hej");
		builder.loadData(1, 34).loadData(2, "då");
		DataHolder holder2 = builder.createDataHolder();

		holder1.sortOnColumn(2, 1);
		holder2.sortOnColumn(2, 1);

		assertFalse(holder1.equals(holder2));
		assertNotEquals(holder1.hashCode(), holder2.hashCode());
	}
	
	@Test
	void testEmptyDataHolder() {
		dataHolder = DataHolderBuilderFactory
				.instantiateWithGeneratedColumnNames(0)
				.createDataHolder();
		
		assertEquals("", dataHolder.toString());
	}
	
	@Test
	void testMetaData() throws DataHolderException {
		builder = DataHolderBuilderFactory.instantiateWithColumnNames("META","DATA");
		builder.loadData(1, "this is").loadData(2, "meta data");
		DataHolder metaHolder = builder.createDataHolder();
		
		builder = DataHolderBuilderFactory.instantiateWithGeneratedColumnNames(rowLength);
		loadDataHolderRowWithNRowsOfData(6);
		
		dataHolder = builder.createDataHolder("meta data:",metaHolder,"data:");
		
		assertEquals("meta data:\n"+metaHolder.toString()+"\ndata:\n", dataHolder.getMetaDataString());
	}

}
