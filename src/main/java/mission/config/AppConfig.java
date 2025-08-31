package mission.config;

import mission.adapter.in.ProgramTerminal;
import mission.application.port.out.LogPort;

public interface AppConfig {
    ProgramTerminal getProgramTerminal();
    LogPort getLogPort();
}
