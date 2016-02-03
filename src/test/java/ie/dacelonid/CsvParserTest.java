package ie.dacelonid;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CsvParserTest {


	@Test
	public void parseCsv_multiLine_getCorrectValues() {
		String testCSV="test1\ttest2\ttest3";
		List<String> inputs = new ArrayList<>();
		CsvParser objUnderTest = new CsvParser();
		List<List<String>> expectedValues = new ArrayList<>();
		for(int x = 0;x < 10;x++){
			inputs.add(testCSV);
			expectedValues.add(Arrays.asList(testCSV.split("\\s*\t\\s*")));
		}
		List<List<String>> actualValues = objUnderTest.parseCsv(inputs);
		
		assertThat(actualValues, is(expectedValues));
	}
	
}
