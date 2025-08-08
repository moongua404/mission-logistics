package mission.adapter.out;

import api.Console;
import mission.application.domain.enums.MessageConstants;
import mission.application.domain.exception.InvalidInputException;
import mission.application.domain.exception.InvalidOutputException;
import mission.application.port.out.InputPort;
import mission.application.port.out.LoggerPort;

public class Terminal implements InputPort, LoggerPort {
    public String getTerminalInput() {
        try {
            return Console.readLine();
        } catch (Exception ex) {
            throw new InvalidInputException(ex.getMessage());
        }
    }

    public void print(MessageConstants messageConstants, Object... args) {
        try {
            System.out.printf((messageConstants.toString()) + "%n", args);
        } catch (Exception e) {
            throw new InvalidOutputException("인수 목록이 맞지 않습니다.");
        }
    }
}
