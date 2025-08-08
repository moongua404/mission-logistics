package mission.application.domain.exception;

import java.util.List;

public class DataLoadException extends CustomException {
    public DataLoadException(String issuer, List<String> list) {
      super(String.format("%s 을(를) 로드할 수 없습니다. - %s"
              ,issuer,
              String.join(", ", list))
      );
    }
}
