package com.rocketseat.planner.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class ViolacaoException extends DataIntegrityViolationException {

	private static final long serialVersionUID = 1L;

	public ViolacaoException(String msg) {
		super(msg);
	}

}
