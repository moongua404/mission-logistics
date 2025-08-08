package mission.application.port.in;

import java.time.LocalTime;
import mission.application.domain.model.Place;

public interface PredictDurationUseCase {
    LocalTime predictDuration(Place startPoint, Place endPoint);
}
