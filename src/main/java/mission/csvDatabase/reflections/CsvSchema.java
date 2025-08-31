package mission.csvDatabase.reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mission.csvDatabase.annotations.Column;
import mission.csvDatabase.annotations.Key;
import mission.csvDatabase.annotations.Table;

public final class CsvSchema<T> {
    private final Class<T> modelClass;
    private final Constructor<T> ctor;
    private final Field keyField;
    private final List<Field> allColumnsInOrder; // 헤더/출력 순서

    private CsvSchema(Class<T> modelClass, Constructor<T> ctor, Field keyField, List<Field> columnsOrder) {
        this.modelClass = modelClass;
        this.ctor = ctor;
        this.keyField = keyField;
        this.allColumnsInOrder = List.copyOf(columnsOrder);
    }

    public static <T> CsvSchema<T> of(Class<T> modelClass) {
        if (modelClass.getAnnotation(Table.class) == null) {
            throw new IllegalStateException("@Table 이 필요합니다: " + modelClass.getName());
        }

        Field key = null;
        List<Field> cols = new ArrayList<>();
        for (Field f : modelClass.getDeclaredFields()) {
            if (f.isAnnotationPresent(Key.class)) {
                if (key != null) throw new IllegalStateException("@Key 는 하나만 허용됩니다: " + modelClass.getName());
                key = f;
            }
            if (f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(Key.class)) {
                f.setAccessible(true);
                cols.add(f);
            }
        }
        if (key == null) throw new IllegalStateException("@Key 가 없습니다: " + modelClass.getName());
        key.setAccessible(true);

        Constructor<T> ctor;
        try {
            ctor = modelClass.getDeclaredConstructor();
            ctor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("기본 생성자가 필요합니다: " + modelClass.getName(), e);
        }

        return new CsvSchema<>(modelClass, ctor, key, cols);
    }

    public Class<T> modelClass() { return modelClass; }
    public Constructor<T> ctor() { return ctor; }
    public Field keyField() { return keyField; }

    public List<String> headerNames() {
        return allColumnsInOrder.stream().map(Field::getName).collect(Collectors.toList());
    }

    public List<Field> columnsInDeclaredOrder() { return allColumnsInOrder; }

    public List<Field> reorderByHeader(List<String> header) {
        Map<String, Field> map = new HashMap<>();
        for (Field f : allColumnsInOrder) map.put(f.getName(), f);
        List<Field> ordered = new ArrayList<>(header.size());
        for (String h : header) ordered.add(map.get(h)); // 없으면 null
        return ordered;
    }
}