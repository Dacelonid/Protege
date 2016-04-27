package ie.dacelonid.CSVParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvParserFactory {
    private CsvParserFactory() {
    }

    public static CsvParser createCsvParser(String fileName) throws IOException {
        InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);

        final List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        }
        final CsvParser csvParser = new CsvParser();
        csvParser.parseCsv(list);
        return csvParser;
    }
}
