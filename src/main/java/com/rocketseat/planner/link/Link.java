package com.rocketseat.planner.link;

import java.util.UUID;
import com.rocketseat.planner.trip.Trip;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "links")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Link {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@JoinColumn(nullable = false)
	private String url;

	@ManyToOne
	@JoinColumn(name = "trip_id", nullable = false)
	private Trip trip;

	@Column(nullable = false)
	private String title;

	public Link(String title, String url, Trip trip) {
		this.url = url;
		this.trip = trip;
		this.title = title;
	}

}
