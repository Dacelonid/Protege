package ie.dacelonid;

import ie.dacelonid.ontology.Ontology;

import java.io.IOException;
import java.io.PrintWriter;

class MainClass {
    public static void main(String[] args) throws IOException {
        String filename = "filename.owl";
        String inputFile = "Emoji_Unicodes.csv";
        if (args.length == 2) {
            filename = args[0];
            inputFile = args[1];
        }

        EmojiParser parser = new EmojiParser(inputFile);

        Ontology owl = parser.generateOntology();

        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(owl);
        }
    }
}
