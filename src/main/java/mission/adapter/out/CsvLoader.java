package mission.adapter.out;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import mission.application.domain.exception.CorruptedFileException;
import mission.utility.Parsable;

public class CsvLoader<T extends Parsable<T>> {
    public List<T> readCsv(String fileName, Function<List<String>, T> factory) {
        return readLines(fileName).stream()
                .map(line -> factory.apply(List.of(line.split(","))))
                .toList();
    }

    private static List<String> readLines(String fileName){
        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = CsvLoader.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception e) {
            throw new CorruptedFileException(e.getMessage());
        }
        return lines;
    }
}
