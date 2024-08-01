package com.rocketseat.planner.link;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/link")
public class LinkController {

	@Autowired
	private LinkService linkService;

	@PostMapping("/{id}")
	public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id, @RequestBody LinkRequestPayload payload) {
		return linkService.registraLink(id, payload);
	}

	@GetMapping("/{id}")
	public ResponseEntity<List<LinkData>> getAllLinks(@PathVariable UUID id) {
		List<LinkData> linkDataList = this.linkService.getAllLinksFromId(id);

		log.info("Buscando links...");
		return ResponseEntity.ok(linkDataList);
	}

}
