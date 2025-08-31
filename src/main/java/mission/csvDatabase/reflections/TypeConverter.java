package mission.csvDatabase.reflections;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class TypeConverter {
    private TypeConverter() {}

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;         // 2025-08-31
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");   // 13:05:27
    private static final DateTimeFormatter DT_FMT   = DateTimeFormatter.ISO_LOCAL_DATE_TIME;     // 2025-08-31T13:05:27

    private static final DateTimeFormatter[] TIME_TRY = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_LOCAL_TIME,                      // 13:05:27.123
            DateTimeFormatter.ofPattern("HH:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm")
    };

    public static Object fromString(String s, Class<?> type) {
        if (s == null) s = "";
        if (type == String.class) return s;
        if (type == int.class || type == Integer.class) return s.isEmpty() ? 0 : Integer.parseInt(s);
        if (type == long.class || type == Long.class) return s.isEmpty() ? 0L : Long.parseLong(s);
        if (type == double.class || type == Double.class) return s.isEmpty() ? 0.0 : Double.parseDouble(s);
        if (type == float.class || type == Float.class) return s.isEmpty() ? 0f : Float.parseFloat(s);
        if (type == boolean.class || type == Boolean.class) return !s.isEmpty() && Boolean.parseBoolean(s);
        if (type == short.class || type == Short.class) return s.isEmpty() ? (short)0 : Short.parseShort(s);
        if (type == byte.class || type == Byte.class) return s.isEmpty() ? (byte)0 : Byte.parseByte(s);
        if (type == char.class || type == Character.class) return s.isEmpty() ? '\0' : s.charAt(0);

        if (type == LocalTime.class) {
            if (s.isEmpty()) return null;
            // 다양한 포맷 시도
            for (DateTimeFormatter f : TIME_TRY) {
                try { return LocalTime.parse(s, f); } catch (DateTimeParseException ignore) {}
            }
            // mm:ss만 온 경우 00:mm:ss로 보정
            if (s.matches("\\d{1,2}:\\d{2}")) {
                try { return LocalTime.parse("00:" + s, DateTimeFormatter.ofPattern("HH:mm:ss")); } catch (DateTimeParseException ignore) {}
            }
            throw new IllegalArgumentException("LocalTime 파싱 실패: " + s);
        }
        return s;
    }

    public static String toString(Object v) {
        if (v == null) return "";
        if (v instanceof LocalDate d)     return DATE_FMT.format(d);
        if (v instanceof LocalTime t)     return TIME_FMT.format(t);        // HH:mm:ss
        if (v instanceof LocalDateTime dt)return DT_FMT.format(dt);
        if (v instanceof Duration dur)    return dur.toString();            // ISO-8601 (e.g., PT5M30S)
        // 나머지는 기본 toString
        return String.valueOf(v);
    }
}