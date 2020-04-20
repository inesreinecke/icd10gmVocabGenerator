package de.zmi.icd10gmgen.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidDataException extends Exception {
	private static final Logger LOG = LoggerFactory.getLogger(InvalidDataException.class);

	public InvalidDataException(String message) {
		super(message);
		LOG.error("{}", message);
	}

}
