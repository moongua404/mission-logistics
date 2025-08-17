package mission.config;

import mission.adapter.in.ProgramTerminal;
import mission.adapter.out.PlaceCsvDatabase;
import mission.adapter.out.PositionCsvDatabase;
import mission.adapter.out.RouteCsvDatabase;
import mission.adapter.out.ClientTerminal;
import mission.application.port.in.GetPlaceInputUseCase;
import mission.application.port.in.MakeOrderUseCase;
import mission.application.service.InputService;
import mission.application.service.OrderService;
import mission.application.service.PredictionService;

public class RuntimeConfig implements AppConfig {
    private final ClientTerminal clientTerminal = new ClientTerminal();
    private final PlaceCsvDatabase placeCsvDatabase = new PlaceCsvDatabase("place.csv");
    private final PositionCsvDatabase positionCsvDatabase = new PositionCsvDatabase("position.csv");
    private final RouteCsvDatabase routeCsvDatabase = new RouteCsvDatabase("route.csv");
    private final InputService inputService = new InputService(clientTerminal, clientTerminal, placeCsvDatabase);
    private final PredictionService predictionService = new PredictionService(positionCsvDatabase, routeCsvDatabase);
    private final OrderService orderService = new OrderService(clientTerminal, clientTerminal);

    @Override
    public ProgramTerminal getProgramTerminal() {
        return new ProgramTerminal(
                getGetPlaceInputUseCase(),
                getPredictDurationUseCase(),
                getMakeOrderUseCase()
        );
    }

    private GetPlaceInputUseCase getGetPlaceInputUseCase() {
        return inputService;
    }

    private PredictionService getPredictDurationUseCase() {
        return predictionService;
    }

    public MakeOrderUseCase getMakeOrderUseCase() {
        return orderService;
    }
}
