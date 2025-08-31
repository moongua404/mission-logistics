package mission;

import api.Console;
import java.sql.Time;
import java.time.LocalTime;
import mission.adapter.Terminal;
import mission.adapter.in.ProgramTerminal;
import mission.application.domain.model.Place;
import mission.application.domain.model.dto.OrderRequest;
import mission.application.port.out.LogPort;
import mission.config.AppConfig;
import mission.config.RuntimeConfig;

public class Application {
    public static void main(String[] args) {
        //TODO: 미션 구현
        AppConfig appConfig = new RuntimeConfig();
        LogPort logPort = appConfig.getLogPort();

        try {
            while (true) {
                ProgramTerminal program = appConfig.getProgramTerminal();
                OrderRequest orderRequest = program.getOrderRequest();
                LocalTime duration = program.predictDuration(orderRequest.startPlace(), orderRequest.endPlace());
                program.makeOrder(orderRequest, duration);
            }
        } catch (Exception e) {
            System.out.println("\n ------ 프로그램 종료 ------ \n");
            logPort.getAll().stream().map(Object::toString).forEach(System.out::println);
        }
    }
}