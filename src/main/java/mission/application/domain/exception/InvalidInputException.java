package mission.application.domain.exception;

public class InvalidInputException extends CustomException {
    public InvalidInputException(String message) {
        super("Error while reading terminal input : " + message);
    }
}
