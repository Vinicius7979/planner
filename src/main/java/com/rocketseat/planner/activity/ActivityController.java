package com.rocketseat.planner.activity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rocketseat.planner.trip.Trip;
import com.rocketseat.planner.trip.TripService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/activities")
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@Autowired
	private TripService tripService;

	@PostMapping("/{id}")
	public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id,
			@RequestBody ActivityRequestPayload payload) {
		Optional<Trip> trip = this.tripService.getTrips(id);

		if (trip.isPresent()) {
			Trip rawTrip = trip.get();

			ActivityResponse activityResponse = this.activityService.registerActivity(payload, rawTrip);

			return ResponseEntity.ok(activityResponse);
		}

		log.info("criando uma atividade...");
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<List<ActivityData>> getAllActivities(@PathVariable UUID id) {
		return activityService.buscaTodasAtividades(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Activity> deleteActivity(@PathVariable UUID id) {
		if (!activityService.verificaActivity(id)) {
			return ResponseEntity.notFound().build();
		}

		activityService.excluiActivity(id);

		return ResponseEntity.noContent().build();
	}

}
