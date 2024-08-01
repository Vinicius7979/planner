package com.rocketseat.planner.participant;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/participants")
public class ParticipantController {

	@Autowired
	private ParticipantService participantService;

	@PostMapping("/{id}/confirm")
	public ResponseEntity<Participant> confirmParticipant(@PathVariable UUID id,
			@RequestBody ParticipantRequestPayload payload) {
		return participantService.confirmaParticipante(id, payload);
	}

	@GetMapping("/{id}")
	public ResponseEntity<List<ParticipantData>> getAllParticipants(@PathVariable UUID id) {
		return participantService.buscaParticipantes(id);
	}

	@PostMapping("/{id}/invite")
	public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id,
			@RequestBody ParticipantRequestPayload payload) {
		log.info("Convidando participante...");
		return participantService.convidaParticipante(id, payload);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Participant> deletaParticipante(@PathVariable UUID id) {
		if (!participantService.verificaParticipant(id)) {
			return ResponseEntity.notFound().build();
		}
		participantService.excluiParticipant(id);

		return ResponseEntity.noContent().build();
	}

}
