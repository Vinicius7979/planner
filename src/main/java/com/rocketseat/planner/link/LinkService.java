package com.rocketseat.planner.link;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.rocketseat.planner.trip.Trip;
import com.rocketseat.planner.trip.TripService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LinkService {

	@Autowired
	private LinkRepository linkRepository;
	
	@Autowired
	private TripService tripService;

	public LinkResponse registraLinkPayload(LinkRequestPayload payload, Trip trip) {
		Link newLink = new Link(payload.title(), payload.url(), trip);

		this.linkRepository.save(newLink);

		log.info("criando um link...");
		return new LinkResponse(newLink.getId());

	}
	
	public ResponseEntity<LinkResponse> registraLink(UUID id, LinkRequestPayload payload) {
		Optional<Trip> trip = this.tripService.getTrips(id);

		if (trip.isPresent()) {
			Trip rawTrip = trip.get();

			LinkResponse linkResponse = registraLinkPayload(payload, rawTrip);

			return ResponseEntity.ok(linkResponse);
		}

		
		return ResponseEntity.notFound().build();
	}

	public List<LinkData> getAllLinksFromId(UUID tripId) {
		return this.linkRepository.findByTripId(tripId).stream()
				.map(link -> new LinkData(link.getId(), link.getTitle(), link.getUrl())).toList();
	}
	
	public boolean verificaLink(UUID id) {
		return linkRepository.existsById(id);
	}
	
	public Optional<Link> getLink(UUID id) {
		return linkRepository.findById(id);
	}

}
