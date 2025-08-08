package mission.application.domain.model;

import java.util.List;
import mission.application.domain.exception.DataLoadException;
import mission.utility.Parsable;

public record Place(int id, String name, String address) implements Parsable<Place> {

    @Override
    public Place parse(List<String> objects) {
        return parseStatic(objects);
    }

    public static Place parseStatic(List<String> objects) {
        try {
            return new Place(
                    Integer.parseInt(objects.get(0)),
                    objects.get(1),
                    objects.get(2)
            );
        } catch (Exception e) {
            throw new DataLoadException("Place", objects);
        }
    }
}
