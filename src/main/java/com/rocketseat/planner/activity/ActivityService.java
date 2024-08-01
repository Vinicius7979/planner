package com.rocketseat.planner.activity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.rocketseat.planner.trip.Trip;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActivityService {

	@Autowired
	private ActivityRepository activityRepository;

	public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
		Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);

		if (validaAtividade(newActivity, trip)) {
			log.warn("A atividade é invalida");
			return null;
		}
		this.activityRepository.save(newActivity);

		log.info("criando atividade");
		return new ActivityResponse(newActivity.getId());

	}

	private boolean validaAtividade(Activity atividade, Trip trip) {
		LocalDateTime dataInicio = trip.getStartsAt();
		LocalDateTime dataTermino = trip.getEndsAt();
		LocalDateTime dataOcorre = atividade.getOccursAt();

		if (dataOcorre.isBefore(dataInicio) || dataOcorre.isAfter(dataTermino)) {
			return true;
		} else {
			return false;
		}
	}

	public List<ActivityData> getAllActivitiesFromId(UUID tripId) {

		this.activityRepository.findByTripId(tripId);
		return this.activityRepository.findByTripId(tripId).stream()
				.map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt()))
				.toList();
	}

	public ResponseEntity<List<ActivityData>> buscaTodasAtividades(UUID id) {
		List<ActivityData> activityDataList = getAllActivitiesFromId(id);
		log.info("Buscando atividade...");
		return ResponseEntity.ok(activityDataList);
	}

	public ResponseEntity<Activity> excluiActivity(UUID activityId) {
		Optional<Activity> act = this.activityRepository.findById(activityId);
		
		if (act.isPresent()) {
			activityRepository.deleteById(activityId);
			log.info("Excluindo atividade...");
		}
		return ResponseEntity.notFound().build();
	}
	
	public boolean verificaActivity(UUID id) {
		return activityRepository.existsById(id);
	}
	
	public void excluiActivityDaTrip(UUID tripId) {
		List<Activity> atividades = activityRepository.findByTripId(tripId);
		
		activityRepository.deleteAll(atividades);
	}
}
