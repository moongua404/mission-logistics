package mission.adapter.out;

import java.util.List;
import java.util.Optional;
import mission.application.domain.model.Position;
import mission.application.port.out.PositionPersistence;

public class PositionCsvDatabase implements PositionPersistence {
    private final List<Position> positions;

    public PositionCsvDatabase(String fileNAme) {
        CsvLoader<Position> csvLoader = new CsvLoader<Position>();
        positions = csvLoader.readCsv(fileNAme, Position::parseStatic);
    }

    @Override
    public Optional<Position> find(int id) {
        return positions.stream()
                .filter(p -> p.id() == id)
                .findFirst();
    }
}
