package mission.application.port.out;

import java.util.Optional;
import mission.application.domain.model.Place;

public interface PlacePersistence {
    Optional<Place> find(String name);
}
