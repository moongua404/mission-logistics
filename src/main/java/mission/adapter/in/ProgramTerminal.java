package mission.adapter.in;

import java.time.LocalTime;
import mission.application.domain.model.Place;
import mission.application.port.in.GetPlaceInputUseCase;
import mission.application.port.in.PredictDurationUseCase;

public class ProgramTerminal {
    private final GetPlaceInputUseCase getPlaceInputUseCase;
    private final PredictDurationUseCase predictDurationUseCase;

    public ProgramTerminal(
            GetPlaceInputUseCase getPlaceInputUseCase,
            PredictDurationUseCase predictDurationUseCase) {
        this.getPlaceInputUseCase = getPlaceInputUseCase;
        this.predictDurationUseCase = predictDurationUseCase;
    }

    public Place getStartPoint() {
        return getPlaceInputUseCase.getStartPoint();
    }

    public Place getEndPoint() {
        return getPlaceInputUseCase.getEndPoint();
    }

    public LocalTime predictDuration(Place startPoint, Place endPoint) {
        return predictDurationUseCase.predictDuration(startPoint, endPoint);
    }
}
