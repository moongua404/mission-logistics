package mission.adapter.out;

import java.util.List;
import java.util.Optional;
import mission.application.domain.model.Place;
import mission.application.port.out.PlacePersistence;

public class PlaceCsvDatabase implements PlacePersistence {
    private final List<Place> places;

    public PlaceCsvDatabase(String fileName) {
        CsvLoader<Place> csvLoader = new CsvLoader<Place>();
        this.places = csvLoader.readCsv(fileName, Place::parseStatic);
    }

    @Override
    public Optional<Place> find(String name) {
        return places.stream()
                .filter(place -> place.name().equals(name))
                .findAny();
    }
}
