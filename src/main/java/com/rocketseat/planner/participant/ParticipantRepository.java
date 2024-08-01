package com.rocketseat.planner.participant;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

public interface ParticipantRepository extends JpaRepository<Participant, UUID> {

	List<Participant> findByTripId(UUID tripId);

	void deleteAllById(ResponseEntity<List<ParticipantData>> participanteId);

}
