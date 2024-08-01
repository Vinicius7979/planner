package com.rocketseat.planner.trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rocketseat.planner.activity.ActivityService;
import com.rocketseat.planner.exception.ViolacaoException;
import com.rocketseat.planner.model.TripModel;
import com.rocketseat.planner.participant.ParticipantService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TripService {

	private TripRepository tripRepository;

	private ModelMapper modelMapper;

	private ParticipantService participantService;

	private ActivityService activityService;

	public TripService(TripRepository tripRepository, ModelMapper modelMapper, ParticipantService participantService,
			ActivityService activityService) {
		super();
		this.tripRepository = tripRepository;
		this.modelMapper = modelMapper;
		this.participantService = participantService;
		this.activityService = activityService;
	}

	@Transactional
	public ResponseEntity<TripCreateResponse> criaTrip(TripRequestPayload payload) {

		if (validaFormatoData(payload)) {
			log.warn("A data inserida esta em um formato inválido");
			throw new ViolacaoException("Errou ai na data");
		}

		Trip newTrip = new Trip(payload);

		if (validaTrip(newTrip)) {
			log.warn("Um ou mais campos preenchidos estão com valores invalidos");
			throw new DataIntegrityViolationException("Um ou mais campos estão com valores invalidos");
		}

		if (!validaData(newTrip)) {
			log.warn("A data da viagem esta invalida: " + newTrip);
			return null;
		}

		this.tripRepository.save(newTrip);

		this.participantService.registerParticipantsToEvent(payload.emails_to_invite(), newTrip);

		log.info("Criando viagem: " + newTrip);

		return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));

	}

	private boolean validaFormatoData(TripRequestPayload payload) {
		int num = 1;
		if (num == 1) {
			try {
				LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME);
			} catch (DateTimeParseException e) {
				num++;
				return true;
			}

		}
		return false;
	}

	private boolean validaData(Trip trip) {
		LocalDateTime dataInicio = trip.getStartsAt();
		LocalDateTime dataTermino = trip.getEndsAt();

		return dataInicio.isBefore(dataTermino);
	}

	private boolean validaTrip(Trip trip) {
		if (trip.getDestination() == null || trip.getDestination() == "") {
			return true;
		}
		if (trip.getOwnerEmail() == null || trip.getOwnerEmail() == "") {
			return true;
		}

		if (trip.getOwnerName() == null || trip.getOwnerName() == "") {
			return true;
		}

		if (trip.getStartsAt() == null) {
			return true;
		}

		if (trip.getEndsAt() == null) {
			return true;
		}

		else {
			return false;
		}
	}

	public ResponseEntity<TripModel> getDetalhesDaTrip(UUID id) {
		log.info("Buscando detalhes da viagem");
		return tripRepository.findById(id).map(trip -> {
			TripModel tripModel = modelMapper.map(trip, TripModel.class);

			return ResponseEntity.ok(tripModel);
		}).orElse(ResponseEntity.notFound().build());

	}

	public boolean verificaTrip(UUID id) {
		return tripRepository.existsById(id);
	}

	public ResponseEntity<Trip> excluiViagem(UUID id) {
		Optional<Trip> trip = this.tripRepository.findById(id);

		if (trip.isPresent()) {

			participantService.excluiParticipanteDaTrip(id);
			activityService.excluiActivityDaTrip(id);
			tripRepository.deleteById(id);
			log.info("Viagem excluida");
		}
		return ResponseEntity.notFound().build();
	}

	public List<Trip> listaTrips() {
		return tripRepository.findAll();
	}

	public ResponseEntity<Trip> atualizaTrip(UUID id, TripRequestPayload payload) {
		Optional<Trip> trip = this.tripRepository.findById(id);

		if (trip.isPresent()) {
			Trip rawTrip = trip.get();
			rawTrip.setEndsAt(LocalDateTime.parse(payload.ends_at(), DateTimeFormatter.ISO_DATE_TIME));
			rawTrip.setStartsAt(LocalDateTime.parse(payload.starts_at(), DateTimeFormatter.ISO_DATE_TIME));
			rawTrip.setDestination(payload.destination());

			this.tripRepository.save(rawTrip);
			log.info("Editando uma viagem...");

			return ResponseEntity.ok(rawTrip);
		}

		return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	public ResponseEntity<Trip> confirmaTrip(UUID id) {

		Optional<Trip> trip = this.tripRepository.findById(id);

		if (trip.isPresent()) {
			Trip rawTrip = trip.get();
			rawTrip.setIsConfirmed(true);

			this.tripRepository.save(rawTrip);
			this.participantService.triggerConfirmationEmailToPartcipants(id);

			log.info("Confirmando uma viagem");
			return ResponseEntity.ok(rawTrip);
		}

		return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	public Optional<Trip> getTrips(UUID id) {
		return this.tripRepository.findById(id);
	}

}
