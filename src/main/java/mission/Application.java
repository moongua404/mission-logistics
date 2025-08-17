package mission;

import api.Console;
import java.sql.Time;
import java.time.LocalTime;
import mission.adapter.Terminal;
import mission.adapter.in.ProgramTerminal;
import mission.application.domain.model.Place;
import mission.application.domain.model.dto.OrderRequest;
import mission.config.AppConfig;
import mission.config.RuntimeConfig;

public class Application {
    public static void main(String[] args) {
        //TODO: 미션 구현
        AppConfig appConfig = new RuntimeConfig();
        while (true) {
            ProgramTerminal program = appConfig.getProgramTerminal();
            OrderRequest orderRequest = program.getOrderRequest();
            LocalTime duration = program.predictDuration(orderRequest.startPlace(), orderRequest.endPlace());
            program.makeOrder(orderRequest, duration);
        }
    }
}