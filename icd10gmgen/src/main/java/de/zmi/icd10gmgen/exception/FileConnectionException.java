package de.zmi.icd10gmgen.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileConnectionException extends Exception {
	private static final Logger LOG = LoggerFactory.getLogger(FileConnectionException.class);

	public FileConnectionException(String message, Throwable cause) {
		super(message, cause);
		LOG.error("{}{}", message, System.lineSeparator(), cause);
		// LOG.error("{}", message);
	}

}
