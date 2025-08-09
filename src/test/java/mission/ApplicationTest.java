package mission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import api.TestEnvironment;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ApplicationTest extends TestEnvironment {
    @Test
    void testApplication() {
        run("봉화군청", "숭실대학교 정보과학관");
        assert(output().contains("이동 시간은 3시간 10분으로 예측됩니다. "));
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
