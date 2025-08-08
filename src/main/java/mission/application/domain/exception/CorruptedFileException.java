package mission.application.domain.exception;

public class CorruptedFileException extends CustomException {
    public CorruptedFileException(String message) {
        super("Exception Occurred while reading csv file - " + message);
    }
}
