package se.yrgo.adrianbook.dataholder;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.yrgo.adrianbook.dataholder.Row;

class RowTest {
	Row row;

	@BeforeEach
	void setUp() throws Exception {
		row = makeRow("hej", "hopp", "2");
	}
	
	@AfterEach
	void tearDown() throws Exception {
		row = null;
	}
	
	@Test
	void testComparingIndexDefaultValue() {
		String comparingColumnDefaultNill = row.getComparingColumnValue();
		
		assertEquals("hej", comparingColumnDefaultNill);
	}
	
	@Test
	void testIsNumericDefaultValue() {
		assertFalse(row.isNumeric());
	}
	
	@Test
	void testAssigningComparasonColumn() {
		boolean[] numericColumns = new boolean[] {false, false, true};
				
		row.setComparisonColumn(2, numericColumns);
		
		String comparingValue = row.getComparingColumnValue();
		
		assertEquals("2", comparingValue);
		assertTrue(row.isNumeric());
	}
	
	@Test
	void testNonExistingComparisonIndexThrowsException() {
		boolean[] numericColumns = new boolean[] {false, false, true};
		assertThrows(IndexOutOfBoundsException.class, ()-> {
			row.setComparisonColumn(6, numericColumns);
		});
	}
	
	@Test
	void testEqualsEquals() {
		Row row1 = makeRow("hej", "2", "pang");
		Row row2 = makeRow("hoj", "3", "pang");
		
		row1.setComparisonColumn(2, new boolean[] {false, true, false});
		row2.setComparisonColumn(2, new boolean[] {false, true, false});
		
		assertTrue(row1.equals(row2));
		assertEquals(row1.hashCode(), row2.hashCode());
	}
	
	@Test
	void testEqualsNotEqualsOnNumericColumns() {
		Row row1 = makeRow("aj", "4", "kanske");
		Row row2 = makeRow("aj", "4", "kanske");
		
		row1.setComparisonColumn(0, new boolean[] {true, true, false});
		row2.setComparisonColumn(0, new boolean[] {false, true, false});
		
		assertFalse(row1.equals(row2));
		assertNotEquals(row1.hashCode(), row2.hashCode());
		
	}
	
	@Test
	void testEqualsNotEqualsOnComparingColumn() {
		Row row1 = makeRow("aj", "4", "kanske");
		Row row2 = makeRow("aj", "4", "kanske");
		
		row1.setComparisonColumn(1, new boolean[] {false, true, false});
		row2.setComparisonColumn(0, new boolean[] {false, true, false});
		
		assertFalse(row1.equals(row2));
		assertNotEquals(row1.hashCode(), row2.hashCode());
	}
	
	@Test
	void testEqualsNotEqualsOnComparisonValue() {
		Row row1 = makeRow("aj", "4", "kanske");
		Row row2 = makeRow("aj", "3", "kanske");
		
		row1.setComparisonColumn(1, new boolean[] {false, true, false});
		row2.setComparisonColumn(1, new boolean[] {false, true, false});
		
		assertFalse(row1.equals(row2));
		assertNotEquals(row1.hashCode(), row2.hashCode());
	}

	private Row makeRow(String...strings) {
		List<String> list = new ArrayList<>();
		for (String s : strings) {
			list.add(s);
		}
		return new Row(list);
	}

}
