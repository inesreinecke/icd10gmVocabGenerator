package de.zmi.icd10gmgen.model;

public class KnownConcept {
	
	private Integer conceptId;
	private String conceptName;
	private String domainId;
	private String vocabularyId;
	private String conceptClassId;
	private String standardConcept;
	private String conceptCode;
	private String validStartDate;
	private String validEndDate;
	private String invalidReason;
	
	public KnownConcept() {
	}
	
	public KnownConcept(
			Integer concept_id,
			String concept_name,
			String domain_id,
			String vocabulary_id,
			String concept_class_id,
			String standard_concept,
			String concept_code,
			String valid_start_date,
			String valid_end_date,
			String invalid_reason
			) {
		this.conceptName = concept_name;
		this.domainId = domain_id;
		this.vocabularyId = vocabulary_id;
		this.conceptClassId = concept_class_id;
		this.standardConcept = standard_concept;
		this.conceptCode = concept_code;
		this.validStartDate = valid_start_date;
		this.validEndDate = valid_end_date;
		this.invalidReason = invalid_reason;		
	}

	public String sanitize(String input) {
		if(input != null) {
			if(input.endsWith("!")) input = input.substring(0, input.length() - 1);
			if(input.endsWith(".-")) input = input.substring(0, input.length() - 2);
			if(input.equals("UNDEF")) return null;
			return input;
		}
		return null;
	}

	public Integer getConceptId() {
		return conceptId;
	}

	public KnownConcept setConceptId(Integer conceptId) {
		this.conceptId = conceptId;
		return this;
	}

	public String getConceptName() {
		return conceptName.replaceAll("'", "");
	}

	public KnownConcept setConceptName(String conceptName) {
		this.conceptName = conceptName;
		return this;
	}

	public String getDomainId() {
		return domainId;
	}

	public KnownConcept setDomainId(String domainId) {
		this.domainId = domainId;
		return this;
	}

	public String getVocabularyId() {
		return vocabularyId;
	}

	public KnownConcept setVocabularyId(String vocabularyId) {
		this.vocabularyId = vocabularyId;
		return this;
	}

	public String getConceptClassId() {
		return conceptClassId;
	}

	public KnownConcept setConceptClassId(String conceptClassId) {
		this.conceptClassId = conceptClassId;
		return this;
	}

	public String getStandardConcept() {
		return standardConcept;
	}

	public KnownConcept setStandardConcept(String standardConcept) {
		this.standardConcept = standardConcept;
		return this;
	}

	public String getConceptCode() {
		return conceptCode;
	}

	public KnownConcept setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
		return this;
	}

	public String getValidStartDate() {
		return validStartDate;
	}

	public KnownConcept setValidStartDate(String validStartDate) {
		this.validStartDate = validStartDate;
		return this;
	}

	public String getValidEndDate() {
		return validEndDate;
	}

	public KnownConcept setValidEndDate(String validEndDate) {
		this.validEndDate = validEndDate;
		return this;
	}

	public String getInvalidReason() {
		return invalidReason;
	}

	public KnownConcept setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
		return this;
	}
	
	public KnownConcept setDefaults() {
		this.vocabularyId = "ICD10GM";
		this.domainId = "D";
		this.standardConcept = "";
		this.validEndDate = "2099-12-31";
		this.conceptClassId = "D";
		this.invalidReason="";
		return this;
	}
	
	@Override
	public String toString() {
		return "conceptId=" + conceptId + " name=" + conceptName;
	}
	
	
}	
