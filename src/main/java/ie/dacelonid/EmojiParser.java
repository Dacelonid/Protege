package ie.dacelonid;

import ie.dacelonid.CSVParser.CSVEntry;
import ie.dacelonid.CSVParser.CSVEntryToEntity;
import ie.dacelonid.CSVParser.CsvParser;
import ie.dacelonid.CSVParser.CsvParserFactory;
import ie.dacelonid.ontology.Ontology;
import ie.dacelonid.ontology.RdfElement;

import java.io.IOException;
import java.util.List;

public class EmojiParser {
    private String inputFile;

    public EmojiParser(String inputFile) {
        this.inputFile = inputFile;
    }

    public Ontology generateOntology() throws IOException {
        Ontology owl = new Ontology();
        CsvParser csvParser = CsvParserFactory.createCsvParser(inputFile);
        List<CSVEntry> allLines = csvParser.getAllLines();
        CSVEntryToEntity converter = new CSVEntryToEntity();
        converter.convert(allLines);
        List<RdfElement> properties = converter.getProperties();
        properties.forEach(owl::addProperty);
        return owl;
    }
}
