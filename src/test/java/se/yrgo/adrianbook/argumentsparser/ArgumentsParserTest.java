package se.yrgo.adrianbook.argumentsparser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.yrgo.adrianbook.exceptions.ArgumentParserException;
import se.yrgo.adrianook.argumentsparser.ArgumentsParser;

class ArgumentsParserTest {
	
	ArgumentsParser parser;
	
	@BeforeEach
	void setUp() throws Exception {
		parser = new ArgumentsParser("lubr");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testInitiatingArgumentsParser() throws ArgumentParserException {
		parser.parseArguments("-l");
		
		assertTrue(parser instanceof ArgumentsParser);
	}
	
	@Test
	void testGetFlags() throws ArgumentParserException {
		parser.parseArguments("-l","-b");
		List<String> returnedArgs = parser.getFlags();
		
		assertEquals(List.of("b","l"), returnedArgs);
	}

	@Test
	void testGetParameterForFlag() throws ArgumentParserException {
		String parameter = "3";
		String flag = "-u";
		parser.parseArguments(flag, parameter);
		
		assertEquals(3, parser.getParameterForFlag("u"));
	}
	
	@Test
	void testRejectUnannouncedFlag() {
		assertThrows(ArgumentParserException.class, () -> {
			parser.parseArguments("-k");
		});
	}
	
	@Test
	void testBigArgsArray() throws ArgumentParserException {
		String[] args = new String[] {"-l", "6","7", "-u", "4", "-b", "2"};
		parser.parseArguments(args);
		
		assertEquals(4, parser.getParameterForFlag("u"));
		assertEquals(2, parser.getParameterForFlag("b"));
		assertEquals(6, parser.getParameterForFlag("l"));
		assertEquals(7, parser.getParameterForFlag("l"));
	}
	
	@Test
	void testToString() {
		assertEquals("ACCEPTED FLAGS: -b -r -u -l", parser.toString());
	}
	
	@Test
	void testHasParameter() throws ArgumentParserException {
		String[] args = new String[] {"-l", "5","-u","-b","4","-r"};
		parser.parseArguments(args);
		
		assertEquals(4, parser.getNumberOfFlags());
		assertFalse(parser.hasParameterFor("u"));
		assertFalse(parser.hasParameterFor("r"));
		assertTrue(parser.hasParameterFor("l"));
		assertTrue(parser.hasParameterFor("b"));
	}
}
