package mission.config;

import mission.adapter.in.ProgramTerminal;
import mission.adapter.out.PlaceCsvDatabase;
import mission.adapter.out.PositionCsvDatabase;
import mission.adapter.out.RouteCsvDatabase;
import mission.adapter.out.Terminal;
import mission.application.port.in.GetPlaceInputUseCase;
import mission.application.port.service.InputService;
import mission.application.port.service.PredictionService;

public class RuntimeConfig implements AppConfig {
    @Override
    public ProgramTerminal getProgramTerminal() {
        return new ProgramTerminal(
                getGetPlaceInputUseCase(),
                getPredictDurationUseCase()
        );
    }

    private GetPlaceInputUseCase getGetPlaceInputUseCase() {
        Terminal terminal = new Terminal();
        return new InputService(
                terminal,
                terminal,
                new PlaceCsvDatabase("place.csv")
        );
    }

    private PredictionService getPredictDurationUseCase() {
        return new PredictionService(
                new PositionCsvDatabase("position.csv"),
                new RouteCsvDatabase("route.csv")
        );
    }
}
