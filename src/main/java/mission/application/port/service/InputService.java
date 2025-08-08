package mission.application.port.service;

import mission.application.domain.enums.MessageConstants;
import mission.application.domain.exception.PlaceNotFoundException;
import mission.application.domain.model.Place;
import mission.application.port.in.GetPlaceInputUseCase;
import mission.application.port.out.InputPort;
import mission.application.port.out.LoggerPort;
import mission.application.port.out.PlacePersistence;

public class InputService implements GetPlaceInputUseCase {
    private final LoggerPort logger;
    private final InputPort input;
    private final PlacePersistence placePersistence;

    public InputService(LoggerPort logger, InputPort input, PlacePersistence placePersistence) {
        this.logger = logger;
        this.input = input;
        this.placePersistence = placePersistence;
    }

    public Place getStartPoint() {
        logger.print(MessageConstants.GET_START_POINT);
        return getPoint();
    }

    public Place getEndPoint() {
        logger.print(MessageConstants.GET_END_POINT);
        return getPoint();
    }

    private Place getPoint() {
        String point = input.getTerminalInput();
        return placePersistence.find(point)
                .orElseThrow(() -> new PlaceNotFoundException(point));
    }
}
