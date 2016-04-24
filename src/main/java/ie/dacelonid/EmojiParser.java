package ie.dacelonid;

import ie.dacelonid.CSVParser.CSVEntry;
import ie.dacelonid.CSVParser.CSVEntryToEntity;
import ie.dacelonid.CSVParser.CsvParserFactory;
import ie.dacelonid.ontology.Ontology;
import ie.dacelonid.ontology.RdfElement;

import java.io.IOException;
import java.util.List;

class EmojiParser {

    Ontology generateOntology(String inputFile) throws IOException {
        List<CSVEntry> allLines = CsvParserFactory.createCsvParser(inputFile).getAllLines();

        CSVEntryToEntity converter = new CSVEntryToEntity();
        converter.convert(allLines);
        List<RdfElement> rdfElements = converter.getElements();

        Ontology owl = new Ontology();
        rdfElements.forEach(owl::addProperty);
        return owl;
    }
}
