package ie.dacelonid;

import ie.dacelonid.ontology.Ontology;

import java.io.IOException;
import java.io.PrintWriter;

class MainClass {
    public static void main(String[] args) throws IOException {
        String defaultOutputFilename = "filename.owl";
        String defaultInputFilename = "Emoji_Unicodes.csv";

        if (args.length == 2) {
            defaultOutputFilename = args[0];
            defaultInputFilename = args[1];
        }

        EmojiParser parser = new EmojiParser();

        Ontology owl = parser.generateOntology(defaultInputFilename);

        try (PrintWriter out = new PrintWriter(defaultOutputFilename)) {
            out.println(owl);
        }
    }
}
