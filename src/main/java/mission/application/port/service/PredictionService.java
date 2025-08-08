package mission.application.port.service;

import java.time.LocalTime;
import mission.application.domain.exception.PositionNotFoundException;
import mission.application.domain.model.Place;
import mission.application.domain.model.Position;
import mission.application.port.in.PredictDurationUseCase;
import mission.application.port.out.PositionPersistence;

public class PredictionService implements PredictDurationUseCase {
    private final PositionPersistence positionPersistence;
    private static final int KM_PER_HOUR = 60;
    private static final double EARTH_RADIUS_KM = 6371.0;

    public PredictionService(PositionPersistence positionPersistence) {
        this.positionPersistence = positionPersistence;
    }

    @Override
    public LocalTime predictDuration(Place startPoint, Place endPoint) {
        Position pos1 = positionPersistence.find(startPoint.id())
                .orElseThrow(() -> new PositionNotFoundException(startPoint.id()));
        Position pos2 = positionPersistence.find(endPoint.id())
                .orElseThrow(() -> new PositionNotFoundException(endPoint.id()));
        return calculateDuration(pos1, pos2);
    }

    private LocalTime calculateDuration(Position pos1, Position pos2) {
        double distanceKm = haversine(pos1.lat(), pos1.lng(), pos2.lat(), pos2.lng());
        double hours = distanceKm / KM_PER_HOUR;
        long totalMinutes = Math.round(hours * 60);
        return LocalTime.MIDNIGHT.plusMinutes(totalMinutes);
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(latRad1) * Math.cos(latRad2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
