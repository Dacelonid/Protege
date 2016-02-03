package ie.dacelonid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvParser {
	private static final int ANNOTATION_COLUMN = 2;
	private static final String DELIMITER = "\t";
	private List<List<String>> parsedStrings;

	public void parseCsv(List<String> allLines) {
		parsedStrings = new ArrayList<>();
		for(String line: allLines){
			parsedStrings.add(Arrays.asList(line.split("\\s*"+ DELIMITER +"\\s*")));
		}
	}

	public List<String> getAnnotations() {
		List<String> annotations = new ArrayList<>();
		for(List<String> line:parsedStrings){
			annotations.add(line.get(ANNOTATION_COLUMN));
		}
		return annotations;
	}

	public List<List<String>> getAllLines() {
		return parsedStrings;
	}

}
