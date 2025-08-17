package mission.adapter.out;

import api.Console;
import mission.adapter.Terminal;
import mission.application.domain.enums.MessageConstants;
import mission.application.domain.exception.InvalidInputException;
import mission.application.domain.exception.InvalidOutputException;
import mission.application.port.out.InputPort;
import mission.application.port.out.LoggerPort;

public class ClientTerminal implements InputPort, LoggerPort {
    Terminal terminal = new Terminal();

    public String getTerminalInput() {
        return terminal.getLine();
    }

    public void print(MessageConstants messageConstants, Object... args) {
        terminal.print(messageConstants, args);
    }
}
