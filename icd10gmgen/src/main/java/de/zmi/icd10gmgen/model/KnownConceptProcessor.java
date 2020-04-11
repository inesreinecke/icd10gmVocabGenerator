package de.zmi.icd10gmgen.model;

import java.util.List;

import de.zmi.icd10gmgen.exception.InvalidDataException;

/**
 * class thats able to process a list of concepts to extract certain data elements easily
 * @author wabommel
 *
 */
public class KnownConceptProcessor {
	
	private List<KnownConcept> data = null;
	// check if uniqueConceptId exists already in the database, only when empty this starts with -1 or 2.000.000 - need to check exact start concept_id
	private int uniqueConceptId = -1;
	
	public KnownConceptProcessor(List<KnownConcept> input, int currentMaxId) throws InvalidDataException {
		if(input == null) throw new InvalidDataException();
		this.data = input;
		this.uniqueConceptId = currentMaxId;
		
		// calculate next concept Id
		for(KnownConcept kc : data) {
			if(kc.getConceptId() != null && kc.getConceptId() > uniqueConceptId) uniqueConceptId = kc.getConceptId();
		}
	}
	
	public int getSize() {
		return data.size();
	}
	
	public List<KnownConcept> getData() {
		return data;
	}

	/**
	 * tries to find a KnownConcept by a given conceptCode
	 * @param code
	 * @return
	 */
	public KnownConcept findByCode(String code) {
		for(KnownConcept kc : data) {
			if(kc.getConceptCode().contentEquals(code)) return kc;
		}
		return null;
	}
	
	public int getNextConceptIdAndIncrement() {
		uniqueConceptId = uniqueConceptId + 1;
		return uniqueConceptId;
	}
}
