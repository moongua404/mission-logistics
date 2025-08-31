package mission.adapter.out;

import java.time.LocalTime;
import java.util.List;
import mission.application.domain.model.Log;
import mission.application.port.out.LogPort;
import mission.csvDatabase.reflections.CsvDatabase;
import mission.csvDatabase.reflections.CsvDatabaseInjector;
import mission.utility.UUIDv7;

public class LogCsvDatabase implements LogPort {
    private final CsvDatabase<Log, String> logDatabase = CsvDatabaseInjector.create(Log.class);

    @Override
    public void log(String orderer, String startPositionName, String endPositionName, LocalTime duration) {
        logDatabase.put(new Log(UUIDv7.random().toString(),
                orderer, startPositionName, endPositionName, duration));
        logDatabase.flush();
    }

    @Override
    public List<Log> getAll() {
        return logDatabase.findAll();
    }
}
