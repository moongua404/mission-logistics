package mission.application.domain.model;

import java.time.LocalTime;
import mission.csvDatabase.annotations.Column;
import mission.csvDatabase.annotations.Key;
import mission.csvDatabase.annotations.Table;

@Table(name = "log")
public class Log {
    @Key
    String id;

    @Column
    String orderer;

    @Column
    String startPositionName;

    @Column
    String endPositionName;

    @Column
    LocalTime duration;

    @Override
    public String toString() {
        return String.format("%-8s|%-12s|%-16s|%-16s|%02d시간 %02d분 |",
                id, orderer, startPositionName, endPositionName,
                        duration.getHour(), duration.getMinute());
    }

    public Log() {}

    public Log(String id, String orderer, String startPositionName, String endPositionName, LocalTime duration) {
        this.id = id;
        this.orderer = orderer;
        this.startPositionName = startPositionName;
        this.endPositionName = endPositionName;
        this.duration = duration;
    }
}
