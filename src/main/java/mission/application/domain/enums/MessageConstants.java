package mission.application.domain.enums;

public enum MessageConstants {
    GET_ORDER("배송 정보를 입력해 주세요. (ex. 봉화군청-숭실대학교 정보과학관(박호건)"),
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
