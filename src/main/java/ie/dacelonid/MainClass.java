package ie.dacelonid;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class MainClass {
    public static void main(String[] args) throws IOException {
        String filename = "filename.owl";
        if (args.length == 1) {
            filename = args[0];
        }
        CsvParser csvParser = CsvParserFactory.createCsvParser();
        Owl owl = new Owl();

        List<CSVEntry> allLines = csvParser.getAllLines();
        CSVEntryToEntity converter = new CSVEntryToEntity();
        List<Entity> convertedEntities = converter.convert(allLines);
        convertedEntities.forEach(owl::addEntity);

        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(owl);
        }
    }
}
