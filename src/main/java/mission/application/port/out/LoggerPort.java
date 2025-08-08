package mission.application.port.out;

import mission.application.domain.enums.MessageConstants;

public interface LoggerPort {
    void print(MessageConstants messageConstants, Object... arg);
}
