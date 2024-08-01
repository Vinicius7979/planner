package com.rocketseat.planner.link;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rocketseat.planner.trip.Trip;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LinkService {

	@Autowired
	private LinkRepository linkRepository;

	public LinkResponse registerLink(LinkRequestPayload payload, Trip trip) {
		Link newLink = new Link(payload.title(), payload.url(), trip);

		this.linkRepository.save(newLink);

		log.info("criando um link...");
		return new LinkResponse(newLink.getId());

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
