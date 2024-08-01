package com.rocketseat.planner.exception;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class Problema {
	
	private Integer status;
	private String titulo;
	private OffsetDateTime dataHora;
	private List<Campo> campos;

}
