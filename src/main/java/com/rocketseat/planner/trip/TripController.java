package com.rocketseat.planner.trip;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rocketseat.planner.link.LinkService;
import com.rocketseat.planner.model.TripModel;

@RestController
@RequestMapping("/trips")
public class TripController {

	private TripService tripService;

	public TripController(TripService tripService, LinkService linkService) {
		super();
		this.tripService = tripService;
	}

	@PostMapping
	public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
		return tripService.criaTrip(payload);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TripModel> getTripDetails(@PathVariable UUID id) {
		return tripService.getDetalhesDaTrip(id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
		return tripService.atualizaTrip(id, payload);
	}

	@GetMapping("/{id}/confirm")
	public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
		return tripService.confirmaTrip(id);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Trip> deleteTrip(@PathVariable UUID id) {
		if (!tripService.verificaTrip(id)) {
			return ResponseEntity.notFound().build();
		}
		tripService.excluiViagem(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public List<Trip> lista() {
		return tripService.listaTrips();
	}

}
