package mission.csvDatabase.reflections;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CsvDatabaseImpl<T, K> implements CsvDatabase<T, K> {
    private final CsvSchema<T> schema;
    private final Path csvPath;
    private final CsvStore<T, Object> store;

    public CsvDatabaseImpl(CsvSchema<T> schema, Path csvPath, CsvStore<T, Object> store) {
        this.schema = schema;
        this.csvPath = csvPath;
        this.store = store;
    }

    @Override
    public void put(T data) {
        Objects.requireNonNull(data, "data");
        try {
            Object key = schema.keyField().get(data);
            if (key == null) throw new IllegalStateException("@Key는 null일 수 없습니다.");
            store.put(key, data);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("키 추출 실패", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T find(K key) {
        return store.get(key);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void flush() {
        CsvIO.save(schema, csvPath, store);
    }
}