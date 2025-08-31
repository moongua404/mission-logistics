package mission.adapter.in;

import java.time.LocalTime;
import mission.application.domain.model.Place;
import mission.application.domain.model.dto.OrderRequest;
import mission.application.port.in.GetPlaceInputUseCase;
import mission.application.port.in.MakeOrderUseCase;
import mission.application.port.in.PredictDurationUseCase;

public class ProgramTerminal {
    private final GetPlaceInputUseCase getPlaceInputUseCase;
    private final PredictDurationUseCase predictDurationUseCase;
    private final MakeOrderUseCase makeOrderUseCase;

    public ProgramTerminal(
            GetPlaceInputUseCase getPlaceInputUseCase,
            PredictDurationUseCase predictDurationUseCase,
            MakeOrderUseCase makeOrderUseCase) {
        this.getPlaceInputUseCase = getPlaceInputUseCase;
        this.predictDurationUseCase = predictDurationUseCase;
        this.makeOrderUseCase = makeOrderUseCase;
    }

    public OrderRequest getOrderRequest() {
        return getPlaceInputUseCase.getOrderRequest();
    }

    public LocalTime predictDuration(Place startPoint, Place endPoint) {
        return predictDurationUseCase.predictDuration(startPoint, endPoint);
    }

    public void makeOrder(OrderRequest orderRequest, LocalTime duration) {
        makeOrderUseCase.makeOrder(orderRequest, duration);
    }
}
