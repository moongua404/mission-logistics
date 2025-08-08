package mission.application.domain.model;

import java.time.LocalTime;
import java.util.List;
import mission.application.domain.exception.DataLoadException;
import mission.utility.Parsable;

public record Route(int place_id_1, int place_id_2, LocalTime time) implements Parsable<Route> {
    @Override
    public Route parse(List<String> objects) {
        return parseStatic(objects);
    }

    public static Route parseStatic(List<String> objects) {
        try {
            return new Route(
                    Integer.parseInt(objects.get(0)),
                    Integer.parseInt(objects.get(1)),
                    parseTime(objects.get(2))
            );
        } catch (Exception e) {
            throw new DataLoadException("Route", objects);
        }
    }

    private static LocalTime parseTime(String timeString) {
        String[] timeParts = timeString.split(":");
        return LocalTime.of(
                Integer.parseInt(timeParts[0]),
                Integer.parseInt(timeParts[1])
        );
    }
}
