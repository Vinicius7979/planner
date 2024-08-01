package com.rocketseat.planner.participant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.rocketseat.planner.trip.Trip;
import com.rocketseat.planner.trip.TripService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ParticipantService {

	private ParticipantRepository participantRepository;

	private TripService tripService;

	public ParticipantService(ParticipantRepository participantRepository, @Lazy TripService tripService) {
		super();
		this.participantRepository = participantRepository;
		this.tripService = tripService;

	}

	public void registerParticipantsToEvent(List<String> participantsToInvite, Trip trip) {

		List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip))
				.toList();

		this.participantRepository.saveAll(participants);

		System.out.println(participants.get(0).getId());
	}

	public void triggerConfirmationEmailToPartcipants(UUID tripId) {

	}

	public void triggerConfirmationEmailToPartcipant(String email) {

	}

	public ParticipantCreateResponse registerParticipantToEvent(String email, Trip trip) {
		Participant newParticipant = new Participant(email, trip);

		this.participantRepository.save(newParticipant);

		return new ParticipantCreateResponse(newParticipant.getId());
	}

	public List<ParticipantData> getAllParticipantsFromEvent(UUID tripId) {
		return this.participantRepository.findByTripId(tripId).stream()
				.map(participant -> new ParticipantData(participant.getId(), participant.getName(),
						participant.getEmail(), participant.getIsConfirmed()))
				.toList();
	}

	public void excluiParticipanteDaTrip(UUID tripId) {
		List<Participant> participante = this.participantRepository.findByTripId(tripId);

		participantRepository.deleteAll(participante);
	}

	public ResponseEntity<List<ParticipantData>> buscaParticipantes(UUID id) {
		List<ParticipantData> participantList = getAllParticipantsFromEvent(id);

		log.info("Buscando participante...");
		return ResponseEntity.ok(participantList);
	}

	public boolean verificaParticipant(UUID id) {
		return participantRepository.existsById(id);

	}

	public ResponseEntity<Participant> excluiParticipant(UUID id) {
		Optional<Participant> participante = participantRepository.findById(id);
		if (participante.isPresent()) {
			participantRepository.deleteById(id);
			log.info("Excluindo participante...");
		}
		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<Participant> confirmaParticipante(UUID id, ParticipantRequestPayload payload) {
		Optional<Participant> participant = this.participantRepository.findById(id);

		if (participant.isPresent()) {
			Participant rawParticipant = participant.get();
			rawParticipant.setIsConfirmed(true);
			rawParticipant.setName(payload.name());

			this.participantRepository.save(rawParticipant);

			log.info("Confirmando participante...");
			return ResponseEntity.ok(rawParticipant);
		}

		return ResponseEntity.notFound().build();
	}

	public ResponseEntity<ParticipantCreateResponse> convidaParticipante(UUID id, ParticipantRequestPayload payload) {

		Optional<Trip> trip = tripService.getTrips(id);

		if (trip.isPresent()) {
			Trip rawTrip = trip.get();
			ParticipantCreateResponse participantCreateResponse = this.registerParticipantToEvent(payload.email(),
					rawTrip);

			if (rawTrip.getIsConfirmed()) {
				triggerConfirmationEmailToPartcipant(payload.email());
			}

			return ResponseEntity.ok(participantCreateResponse);
		}

		return ResponseEntity.notFound().build();

	}

}
