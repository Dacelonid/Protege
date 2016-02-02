package ie.dacelonid;

import java.util.Arrays;
import java.util.List;

public class CsvParser {
	private static final String DELIMITER = "\t";

	public List<String> parseCsv(String testCSV) {
		return Arrays.asList(testCSV.split("\\s*"+ DELIMITER +"\\s*"));
	}

}
