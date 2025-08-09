package mission.application.service;

import java.time.LocalTime;
import mission.application.domain.exception.PositionNotFoundException;
import mission.application.domain.model.Place;
import mission.application.domain.model.Position;
import mission.application.port.in.PredictDurationUseCase;
import mission.application.port.out.PositionPersistence;
import mission.application.port.out.RoutePersistence;

public class PredictionService implements PredictDurationUseCase {
    private final PositionPersistence positionPersistence;
    private final RoutePersistence routePersistence;

    public PredictionService(PositionPersistence positionPersistence,  RoutePersistence routePersistence) {
        this.positionPersistence = positionPersistence;
        this.routePersistence = routePersistence;
    }

    @Override
    public LocalTime predictDuration(Place startPoint, Place endPoint) {
        Position pos1 = positionPersistence.find(startPoint.id())
                .orElseThrow(() -> new PositionNotFoundException(startPoint.id()));
        Position pos2 = positionPersistence.find(endPoint.id())
                .orElseThrow(() -> new PositionNotFoundException(endPoint.id()));
        return calculateDuration(pos1, pos2);
    }

    private LocalTime calculateDuration(Position pos1, Position pos2) {
        return routePersistence.predictDuration(pos1.id(), pos2.id());
    }
}
