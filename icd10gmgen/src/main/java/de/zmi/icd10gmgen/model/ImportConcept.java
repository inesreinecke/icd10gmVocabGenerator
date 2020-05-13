package de.zmi.icd10gmgen.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.zmi.icd10gmgen.exception.InvalidDataException;

public class ImportConcept {
	private String code = null;
	private String name = null;
	final static private String errorMessage = "invalid data";

	private static final Logger LOG = LoggerFactory.getLogger(ImportConcept.class);

	public ImportConcept(String code, String name) throws InvalidDataException {

		LOG.info("import concept: {} ({})", code, name);

		// sanitize code
		code = sanitize(code);
		if (code == null)
			throw new InvalidDataException(errorMessage);

		this.code = code;
		this.name = name;
	}

	// we need to handle ! and .- and ensure this gets removed from the imported
	// code
	public String sanitize(String input) {

		if (input != null) {

			LOG.trace("sanitize input: {}", input);

			if (input.endsWith("!"))
				input = input.substring(0, input.length() - 1);
			if (input.endsWith(".-"))
				input = input.substring(0, input.length() - 2);
			if (input.endsWith("-"))
				input = input.substring(0, input.length() - 1);
			if (input.equals("UNDEF"))
				return null;
			return input;
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name.replaceAll("'", "");
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.code + " : " + this.name;
	}

}
