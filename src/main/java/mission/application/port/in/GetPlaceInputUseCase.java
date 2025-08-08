package mission.application.port.in;

import mission.application.domain.model.Place;

public interface GetPlaceInputUseCase {
    Place getStartPoint();
    Place getEndPoint();
}
