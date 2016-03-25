package ie.dacelonid;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MainClass {
    public static void main(String[] args) throws IOException {
        CsvParser csvParser = CsvParserFactory.createCsvParser();
        Owl owl = new Owl();

        List<CSVEntries> allLines = csvParser.getAllLines();
        CSVEntryToEntity converter = new CSVEntryToEntity();
        List<Entity> convertedEntities = converter.convert(allLines);
        convertedEntities.forEach(owl::addEntity);

        try (PrintWriter out = new PrintWriter("filename.owl")) {
            out.println(owl);
        }
    }
}
