package ie.dacelonid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvParser {
	private static final String DELIMITER = "\t";

	public List<List<String>> parseCsv(List<String> testCSV) {
		List<List<String>> parsedStrings = new ArrayList<>();
		for(String line: testCSV){
			parsedStrings.add(Arrays.asList(line.split("\\s*"+ DELIMITER +"\\s*")));
		}
		return parsedStrings;
	}

}
