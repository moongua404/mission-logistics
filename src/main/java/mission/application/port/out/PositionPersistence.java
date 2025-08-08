package mission.application.port.out;

import java.util.Optional;
import mission.application.domain.model.Position;

public interface PositionPersistence {
    Optional<Position> find(int id);
}
