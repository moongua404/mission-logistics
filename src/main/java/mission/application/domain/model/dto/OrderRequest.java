package mission.application.domain.model.dto;

import mission.application.domain.model.Place;

public record OrderRequest(String ordererName, Place startPlace, Place endPlace) {
}
