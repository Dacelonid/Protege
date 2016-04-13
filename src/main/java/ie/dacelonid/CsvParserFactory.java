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

    static CsvParser createCsvParser(String fileName) throws IOException {
        Optional<URL> url = Optional.ofNullable(Thread.currentThread().getContextClassLoader().getResource(fileName));
        if (url.isPresent()) {
            final File file = new File(url.get().getPath());

            final List<String> list = Files.readAllLines(file.toPath(), Charset.defaultCharset());
            final CsvParser csvParser = new CsvParser();
            csvParser.parseCsv(list);
            return csvParser;
        }
        throw new IllegalArgumentException("Could not find Emoji File");
    }
}
