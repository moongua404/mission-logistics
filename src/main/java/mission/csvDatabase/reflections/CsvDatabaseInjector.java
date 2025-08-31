package mission.csvDatabase.reflections;

import java.nio.file.Path;
import mission.csvDatabase.annotations.Table;

public class CsvDatabaseInjector {
    private CsvDatabaseInjector() {}

    public static <T, K> CsvDatabase<T, K> create(Class<T> modelClass) {
        return create(modelClass, Path.of("src/main/resources"));
    }

    public static <T, K> CsvDatabase<T, K> create(Class<T> modelClass, Path baseDir) {
        Table table = modelClass.getAnnotation(Table.class);
        if (table == null) {
            throw new IllegalStateException("@Table 이 없습니다: " + modelClass.getName());
        }

        CsvSchema<T> schema = CsvSchema.of(modelClass);

        Path csvPath = baseDir.resolve(table.name() + ".csv").normalize();

        CsvStore<T, Object> store = new CsvStore<>(schema.keyField().getType());

        CsvIO.loadOrInit(schema, csvPath, store);

        return new CsvDatabaseImpl<>(schema, csvPath, store);
    }
}
