package mission.application.port.out;

import java.time.LocalTime;
import java.util.List;
import mission.application.domain.model.Log;

public interface LogPort {
    void log(String orderer, String startPositionName, String endPositionName, LocalTime duration);
    List<Log> getAll();
}
