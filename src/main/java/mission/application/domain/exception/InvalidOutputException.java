package mission.application.domain.exception;

public class InvalidOutputException extends CustomException {
    public InvalidOutputException(String message) {
        super("Error while logging on terminal : " + message);
    }
}
