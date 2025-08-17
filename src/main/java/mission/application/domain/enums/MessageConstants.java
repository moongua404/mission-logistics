package mission.application.domain.enums;

public enum MessageConstants {
    GET_ORDER("배송 정보를 입력해 주세요. (ex. 봉화군청-숭실대학교 정보과학관(박호건)"),
    RECEIVE_ORDER("배송이 정상적으로 접수되었습니다. (id : %d)"),
    DELIVERY_START("배송이 시작되었습니다. (id : %d, 예상 배송 시간 : %d시간 %2d분)"),
    DELIVERY_COMPLETE("배송이 완료되었습니다. ");

    private final String message;

    MessageConstants(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
