package mission.application.port.in;

import mission.application.domain.model.dto.OrderRequest;

public interface GetPlaceInputUseCase {
    OrderRequest getOrderRequest();
}
