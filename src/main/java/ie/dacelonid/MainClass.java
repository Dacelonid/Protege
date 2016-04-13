package ie.dacelonid;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

class MainClass {
    public static void main(String[] args) throws IOException {
        String filename = "filename.owl";
        String inputFile = "Emoji_Unicodes.csv";
        if (args.length == 2) {
            filename = args[0];
            inputFile = args[1];
        }
        CsvParser csvParser = CsvParserFactory.createCsvParser(inputFile);
        Owl owl = new Owl();

        List<CSVEntry> allLines = csvParser.getAllLines();
        CSVEntryToEntity converter = new CSVEntryToEntity();
        converter.convert(allLines);
        List<Entity> convertedEntities = converter.getEntities();
        convertedEntities.forEach(owl::addEntity);

        List<RdfElement> disjointClasses = converter.getDisjointClasses();
        disjointClasses.forEach(owl::addDisjointClasses);

        List<RdfElement> properties = converter.getProperties();
        properties.forEach(owl::addProperty);



        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(owl);
        }
    }
}
