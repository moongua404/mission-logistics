package mission.application.port.out;

import java.time.LocalTime;

public interface RoutePersistence {
    LocalTime predictDuration(int startPlaceId, int endPlaceId);
}
