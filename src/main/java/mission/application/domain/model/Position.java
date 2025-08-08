package mission.application.domain.model;

import java.util.List;
import mission.application.domain.exception.DataLoadException;
import mission.utility.Parsable;

public record Position(int id, double lat, double lng) implements Parsable<Position> {

    @Override
    public Position parse(List<String> objects) {
        return parseStatic(objects);
    }

    public static Position parseStatic(List<String> objects) {
        try {
            return new Position(
                    Integer.parseInt(objects.get(0)),
                    Double.parseDouble(objects.get(1)),
                    Double.parseDouble(objects.get(2))
            );
        } catch (Exception e) {
            throw new DataLoadException("Place", objects);
        }
    }
}
