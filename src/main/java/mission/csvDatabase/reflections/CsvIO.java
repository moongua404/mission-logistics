package mission.csvDatabase.reflections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class CsvIO {
    private CsvIO() {}

    /** 파일이 있으면 로드, 없으면 헤더만 있는 빈 CSV 생성 */
    public static <T> void loadOrInit(CsvSchema<T> schema, Path csvPath, CsvStore<T, Object> store) {
        try {
            if (!Files.exists(csvPath)) {
                initEmpty(schema, csvPath);
                return;
            }
            load(schema, csvPath, store);
        } catch (IOException e) {
            throw new RuntimeException("CSV 접근 실패: " + csvPath, e);
        }
    }

    /** 빈 CSV(헤더만) 생성 */
    public static <T> void initEmpty(CsvSchema<T> schema, Path csvPath) throws IOException {
        Files.createDirectories(csvPath.getParent() == null ? Path.of(".") : csvPath.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8)) {
            bw.write(String.join(",", schema.headerNames()));
            bw.newLine();
        }
    }

    /** CSV 로드 */
    public static <T> void load(CsvSchema<T> schema, Path csvPath, CsvStore<T, Object> store) {
        try (BufferedReader br = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String header = br.readLine();
            if (header == null) return;
            List<String> headerCols = CsvUtils.parseCsvLine(header);
            List<Field> ordered = schema.reorderByHeader(headerCols);

            String line;
            while ((line = br.readLine()) != null) {
                List<String> cells = CsvUtils.parseCsvLine(line);
                T instance = schema.ctor().newInstance();
                for (int i = 0; i < Math.min(ordered.size(), cells.size()); i++) {
                    Field f = ordered.get(i);
                    if (f == null) continue; // 모델에 없는 컬럼
                    Object v = TypeConverter.fromString(cells.get(i), f.getType());
                    f.set(instance, v);
                }
                Object key = schema.keyField().get(instance);
                if (key == null) throw new IllegalStateException("CSV 로드 중 키가 null");
                store.put(key, instance);
            }
        } catch (FileNotFoundException e) {
            // 무시
        } catch (Exception e) {
            throw new RuntimeException("CSV 로드 실패: " + csvPath, e);
        }
    }

    /** CSV 저장(덮어쓰기) */
    public static <T> void save(CsvSchema<T> schema, Path csvPath, CsvStore<T, Object> store) {
        try {
            Files.createDirectories(csvPath.getParent() == null ? Path.of(".") : csvPath.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8)) {
                // 헤더
                bw.write(String.join(",", schema.headerNames()));
                bw.newLine();
                // 내용(정렬됨)
                List<Field> cols = schema.columnsInDeclaredOrder();
                for (T obj : store.values()) {
                    List<String> cells = new ArrayList<>(cols.size());
                    for (Field f : cols) {
                        Object v = f.get(obj);
                        cells.add(CsvUtils.escape(TypeConverter.toString(v)));
                    }
                    bw.write(String.join(",", cells));
                    bw.newLine();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("CSV 저장 실패: " + csvPath, e);
        }
    }
}