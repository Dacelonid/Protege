package ie.dacelonid;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

public class CsvParserFunctionalTest {

    @Test
    public void readValidEmojiCSV_getListOfAnnotations() throws Exception {
        Optional<URL> url = Optional.ofNullable(Thread.currentThread().getContextClassLoader().getResource("Emoji_Unicodes.csv"));
        final File file = new File(url.get().getPath());

        final List<String> list = Files.readAllLines(file.toPath(), Charset.defaultCharset());

        final CsvParser objUnderTest = new CsvParser();
        objUnderTest.parseCsv(list);
        System.out.println(objUnderTest.getAnnotations());
        System.out.println(objUnderTest.getDescriptions());
    }
}
