package mission.config;

import mission.adapter.in.ProgramTerminal;
import mission.adapter.out.LogCsvDatabase;
import mission.adapter.out.PlaceCsvDatabase;
import mission.adapter.out.PositionCsvDatabase;
import mission.adapter.out.RouteCsvDatabase;
import mission.adapter.out.ClientTerminal;
import mission.application.domain.model.Log;
import mission.application.port.in.GetPlaceInputUseCase;
import mission.application.port.in.MakeOrderUseCase;
import mission.application.port.out.LogPort;
import mission.application.service.InputService;
import mission.application.service.OrderService;
import mission.application.service.PredictionService;
import mission.csvDatabase.reflections.CsvDatabase;
import mission.csvDatabase.reflections.CsvDatabaseInjector;

public class RuntimeConfig implements AppConfig {
    private final ClientTerminal clientTerminal = new ClientTerminal();
    private final PlaceCsvDatabase placeCsvDatabase = new PlaceCsvDatabase("place.csv");
    private final PositionCsvDatabase positionCsvDatabase = new PositionCsvDatabase("position.csv");
    private final RouteCsvDatabase routeCsvDatabase = new RouteCsvDatabase("route.csv");
    private final InputService inputService = new InputService(clientTerminal, clientTerminal, placeCsvDatabase);
    private final PredictionService predictionService = new PredictionService(positionCsvDatabase, routeCsvDatabase);
    private final LogCsvDatabase logCsvDatabase = new LogCsvDatabase();
    private final OrderService orderService = new OrderService(clientTerminal, clientTerminal, logCsvDatabase);

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

    @Override
    public LogPort getLogPort() {
        return logCsvDatabase;
    }
}
