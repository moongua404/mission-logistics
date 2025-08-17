package mission.application.port.in;

import java.time.LocalTime;
import mission.application.domain.model.dto.OrderRequest;

public interface MakeOrderUseCase {
    void makeOrder(OrderRequest orderRequest, LocalTime duration);
}
