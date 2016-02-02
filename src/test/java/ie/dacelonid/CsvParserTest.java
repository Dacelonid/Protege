package ie.dacelonid;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CsvParserTest {

	@Test
	public void verifyParsing_singleLine_getCorrectValues() {
		String testCSV="test1\ttest2\ttest3";
		CsvParser objUnderTest = new CsvParser();
		List<String> expectedValues =  Arrays.asList(testCSV.split("\\s*\t\\s*"));
		List<String> actualValues = objUnderTest.parseCsv(testCSV);
		
		assertThat(actualValues, is(expectedValues));
	}

}
