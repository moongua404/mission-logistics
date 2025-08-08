package mission.application.domain.exception;

public class PlaceNotFoundException extends CustomException {
    public PlaceNotFoundException(String placeName) {
        super(String.format("\"%s\"을(를) 이름으로 갖는 장소는 존재하지 않습니다.", placeName));
    }
}
