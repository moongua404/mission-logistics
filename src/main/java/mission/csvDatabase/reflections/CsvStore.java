package mission.csvDatabase.reflections;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public final class CsvStore<T, K> {
    private final TreeMap<Object, T> tree;

    public CsvStore(Class<?> keyType) {
        this.tree = new TreeMap<>(new KeyComparator(keyType));
    }

    public void put(K key, T value) {
        tree.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public T get(K key) {
        return tree.get(key);
    }

    public Collection<T> values() {
        return tree.values();
    }

    public Set<Entry<Object, T>> entrySet() {
        return tree.entrySet();
    }

    /**
     * 키 비교기 (숫자/문자열 일반용)
     */
        private record KeyComparator(Class<?> keyType) implements Comparator<Object> {

        @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public int compare(Object a, Object b) {
                if (a == b) {
                    return 0;
                }
                if (a == null) {
                    return -1;
                }
                if (b == null) {
                    return 1;
                }

                if (a.getClass() == b.getClass() && a instanceof Comparable<?>) {
                    return ((Comparable) a).compareTo(b);
                }

                // 숫자형 우선 처리
                try {
                    if (Number.class.isAssignableFrom(keyType) || keyType.isPrimitive()) {
                        double da = (a instanceof Number) ? ((Number) a).doubleValue() : Double.parseDouble(a.toString());
                        double db = (b instanceof Number) ? ((Number) b).doubleValue() : Double.parseDouble(b.toString());
                        return Double.compare(da, db);
                    }
                } catch (Exception ignore) {
                }

                return a.toString().compareTo(b.toString());
            }
        }
}