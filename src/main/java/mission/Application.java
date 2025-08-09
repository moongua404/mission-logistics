package mission;

import api.Console;
import java.sql.Time;
import java.time.LocalTime;
import mission.adapter.in.ProgramTerminal;
import mission.application.domain.model.Place;
import mission.application.domain.model.dto.OrderRequest;
import mission.config.AppConfig;
import mission.config.RuntimeConfig;

public class Application {
    public static void main(String[] args) {
        //입력 스레드 띄워
        //배송관리 스레드 띄워
        //TODO: 미션 구현
        AppConfig appConfig = new RuntimeConfig();
        while (true) {
            ProgramTerminal program = appConfig.getProgramTerminal();
            OrderRequest orderRequest = program.getOrderRequest();
            System.out.println("배송이 정상적으로 접수되었습니다. (id : %d)");
            LocalTime duration = program.predictDuration(orderRequest.startPlace(), orderRequest.endPlace());
            try {
                System.out.printf("배송이 시작되었습니다. (id : %d, 예상 배송 시간 : %d시간 %2d분)%n",
                        1, duration.getHour(), duration.getMinute());
                Thread.sleep(duration.toSecondOfDay() * 1000L / 180);
                System.out.println("배송이 완료되었습니다. ");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}