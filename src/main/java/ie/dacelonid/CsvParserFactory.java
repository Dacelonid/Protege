package ie.dacelonid;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

class CsvParserFactory {
    private CsvParserFactory() {
    }

    static CsvParser createCsvParser() throws IOException {
        Optional<URL> url = Optional.ofNullable(Thread.currentThread().getContextClassLoader().getResource("Emoji_Unicodes.csv"));
        if (!url.isPresent()) {
            throw new IllegalArgumentException("Could not find Emoji File");
        }
        final File file = new File(url.get().getPath());

        final List<String> list = Files.readAllLines(file.toPath(), Charset.defaultCharset());
        final CsvParser objUnderTest = new CsvParser();
        objUnderTest.parseCsv(list);
        return objUnderTest;
    }
}
