package mission.application.domain.exception;

public class PositionNotFoundException extends CustomException {
    public PositionNotFoundException(int locationId) {
        super(String.format("\"%s\"을(를) id 갖는 장소는 존재하지 않습니다.", locationId));
    }
}
