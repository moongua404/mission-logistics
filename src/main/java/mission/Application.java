package mission;

import api.Console;
import java.time.LocalTime;
import mission.adapter.in.ProgramTerminal;
import mission.application.domain.model.Place;
import mission.config.AppConfig;
import mission.config.RuntimeConfig;

public class Application {
    public static void main(String[] args) {
        //TODO: 미션 구현
        AppConfig appConfig = new RuntimeConfig();
        ProgramTerminal program = appConfig.getProgramTerminal();

        Place startPoint = program.getStartPoint();
        Place endPoint = program.getEndPoint();
        LocalTime duration = program.predictDuration(startPoint, endPoint);
        System.out.printf("이동 시간은 %d시간 %d분으로 예측됩니다. %n"
                ,duration.getHour(),
                duration.getMinute());
    }
}