package mission.csvDatabase.reflections;

import java.util.ArrayList;
import java.util.List;

public final class CsvUtils {
    private CsvUtils() {}

    public static String escape(Object v) {
        String s = (v == null) ? "" : String.valueOf(v);
        boolean needQuote = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        if (s.contains("\"")) s = s.replace("\"", "\"\"");
        return needQuote ? "\"" + s + "\"" : s;
    }

    public static List<String> parseCsvLine(String line) {
        List<String> out = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuote = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuote) {
                if (c == '\"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                        sb.append('\"'); i++;
                    } else {
                        inQuote = false;
                    }
                } else sb.append(c);
            } else {
                if (c == ',') { out.add(sb.toString()); sb.setLength(0); }
                else if (c == '\"') inQuote = true;
                else sb.append(c);
            }
        }
        out.add(sb.toString());
        return out;
    }
}