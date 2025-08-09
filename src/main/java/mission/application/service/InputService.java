package mission.application.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mission.application.domain.enums.MessageConstants;
import mission.application.domain.exception.InvalidInputException;
import mission.application.domain.exception.PlaceNotFoundException;
import mission.application.domain.model.dto.OrderRequest;
import mission.application.port.in.GetPlaceInputUseCase;
import mission.application.port.out.InputPort;
import mission.application.port.out.LoggerPort;
import mission.application.port.out.PlacePersistence;

public class InputService implements GetPlaceInputUseCase {
    private static final String orderRegex = "^([가-힣A-Za-z ]*)-([가-힣A-Za-z ]*)\\(([가-힣A-Za-z ]*)\\)$";

    private final LoggerPort logger;
    private final InputPort input;
    private final PlacePersistence placePersistence;

    public InputService(LoggerPort logger, InputPort input, PlacePersistence placePersistence) {
        this.logger = logger;
        this.input = input;
        this.placePersistence = placePersistence;
    }

    @Override
    public OrderRequest getOrderRequest() {
        logger.print(MessageConstants.GET_ORDER);
        String orderLine = input.getTerminalInput().trim();
        Matcher matcher = Pattern.compile(orderRegex).matcher(orderLine);
        if (!matcher.matches()) {
            throw new InvalidInputException("입력 형식이 맞지 않습니다.");
        }
        return buildOrderRequest(matcher.group(3).trim(), matcher.group(1).trim(), matcher.group(2).trim());
    }

    private OrderRequest buildOrderRequest(String ordererName, String startPlace, String endPlace) {
        return new OrderRequest(
                ordererName,
                placePersistence.find(startPlace)
                        .orElseThrow(() -> new PlaceNotFoundException(startPlace)),
                placePersistence.find(endPlace)
                        .orElseThrow(() -> new PlaceNotFoundException(endPlace))
        );
    }
}
