package mission.application.domain.enums;

public enum MessageConstants {
    GET_START_POINT("출발지를 입력해주세요."),
    GET_END_POINT("도착지를 입력해주세요."),
    PRINT_PREDICTION_TIME("이동 시간은 %d시간 %2d분으로 예측됩니다.");

    private final String message;

    MessageConstants(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
