package mission.utility;

import java.util.List;

public interface Parsable<T> {
    T parse(List<String> objects);
}
