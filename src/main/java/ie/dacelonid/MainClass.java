package ie.dacelonid;

import ie.dacelonid.ontology.Ontology;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;

class MainClass {
    public static final String DEFAULT_OUTPUT_FILENAME = "Kenneth_O_Neill_14207684.owl";
    public static final String DEFAULT_INPUT_FILENAME = "Emoji_Unicodes.csv";

    public static void main(String[] args) throws IOException, URISyntaxException {
        String outputFile = DEFAULT_OUTPUT_FILENAME;
        String inputFile = DEFAULT_INPUT_FILENAME;
        if (args.length == 2) {
            outputFile = args[0];
            inputFile = args[1];
        }

        EmojiParser parser = new EmojiParser();

        Ontology owl = parser.generateOntology(inputFile);

        try (PrintWriter out = new PrintWriter(outputFile)) {
            out.println(owl);
            System.out.println("Successfully wrote " + outputFile);
        }

    }
}
