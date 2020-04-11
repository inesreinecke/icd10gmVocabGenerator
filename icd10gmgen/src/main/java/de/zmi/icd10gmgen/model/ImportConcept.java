package de.zmi.icd10gmgen.model;

import de.zmi.icd10gmgen.exception.InvalidDataException;

public class ImportConcept {
	private String code = null;
	private String name = null;
	
	public ImportConcept(String code, String name) throws InvalidDataException {

		// sanitize code
		code = sanitize(code);
		if(code == null) throw new InvalidDataException();
			
		this.code = code;
		this.name = name;
	}
    // we need to handle ! and .- and ensure this gets removed from the imported code
	public String sanitize(String input) {
		if(input != null) {
			if(input.endsWith("!")) input = input.substring(0, input.length() - 1);
			if(input.endsWith(".-")) input = input.substring(0, input.length() - 2);
			if(input.equals("UNDEF")) return null;
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
