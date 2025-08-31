package mission.csvDatabase.reflections;

import java.util.List;

public interface CsvDatabase<T, K> {
    void put(T data);
    void flush();
    T find(K key);
    List<T> findAll();
}
