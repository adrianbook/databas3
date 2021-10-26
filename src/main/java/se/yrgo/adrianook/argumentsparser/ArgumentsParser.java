package se.yrgo.adrianook.argumentsparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;

import se.yrgo.adrianbook.exceptions.ArgumentParserException;

public class ArgumentsParser {

	private Map<String, List<Integer>> arguments;
	private Set<String> acceptedFlags;
	
	public ArgumentsParser(String AcceptableFlags) {
		super();
		this.acceptedFlags = new HashSet<>();
		this.arguments = new HashMap<>();
		char[] argsArray = AcceptableFlags.toCharArray();
		for (char c : argsArray) {
			this.acceptedFlags.add(String.valueOf(c));
		}
	}

	public void parseArguments(String... args) throws ArgumentParserException {
		int parameter;
		String currentFlag = null;
		for (String a : args) {
			if (a.charAt(0) == '-') {
				currentFlag = cleanArg(a);
				if (!acceptedFlags.contains(currentFlag)) throw new ArgumentParserException("Unallowed flag "+currentFlag);
				arguments.put(currentFlag, null);
			} else {
				parameter = Integer.parseInt(a);
				if (currentFlag != null) {
					if (arguments.get(currentFlag) == null) {
						arguments.put(currentFlag, new ArrayList<Integer>());
					}
					arguments.get(currentFlag).add(parameter);					
				}
			}
		}
	}
	
	private String cleanArg(String arg) {
		return arg.substring(1);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("ACCEPTED FLAGS:");
		for (String flag : acceptedFlags) {
			builder.append(" -").append(flag);
		}
		return builder.toString();
	}
	
	public List<String> getFlags() {
		List<String> argumentsList = new ArrayList<>();
		for (String a : arguments.keySet()) {
			argumentsList.add(a);
		}
		return argumentsList;
	}
	
	public int getNumberOfFlags() {
		return arguments.size();
	}

	public int getNumbersOfParametersForFlag(String flag) {
		return arguments.get(flag).size();
	}
	
	public int getParameterForFlag(String flag) throws ArgumentParserException {
		List<Integer> params = arguments.get(flag);
		if (params.equals(Collections.EMPTY_LIST)) {
			throw new ArgumentParserException("No params for flag: -"+flag);
		}
		return params.remove(0);
	}

	public boolean hasParameterFor(String flag) {
		List<Integer> params =  arguments.get(flag);			
		if (params == null) return false;
		return !params.equals(Collections.EMPTY_LIST);
	}
	
}
