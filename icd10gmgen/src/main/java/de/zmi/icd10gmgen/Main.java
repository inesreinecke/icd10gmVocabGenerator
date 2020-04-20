package de.zmi.icd10gmgen;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.zmi.icd10gmgen.connection.DbConnection;
import de.zmi.icd10gmgen.connection.FileConnection;
import de.zmi.icd10gmgen.exception.DbException;
import de.zmi.icd10gmgen.exception.FileConnectionException;
import de.zmi.icd10gmgen.model.ImportConcept;
import de.zmi.icd10gmgen.model.KnownConcept;
import de.zmi.icd10gmgen.model.KnownConceptProcessor;
import de.zmi.icd10gmgen.model.Params;

public class Main {
	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		// verify input parameters
		if (args.length != 9) {
			printExpectedParams();
			System.exit(-1);
		}

		Params params = new Params();
		params.setYear(args[0]);
		params.setFilename(args[1]);
		params.setDatabase(args[2]);
		params.setDbUserName(args[3]);
		params.setDbUserPassword(args[4]);
		params.setTable(args[5]);
		params.setDbServerIp(args[6]);
		params.setDbServerPort(Integer.parseInt(args[7]));
		params.setEncoding(args[8]);

		// params get default values - predefined in Params.java
//		 Params params = new Params().getDefaults();

		try {
			// connect to the database that contains a concept table with ICD10GM vocabulary
			DbConnection myDbCon = new DbConnection(params);

			if (myDbCon.isConnected()) {
				LOG.info("connection successfull");
				LOG.info("db version: {}", myDbCon.getDbVersion());

				LOG.info("create new list of concepts from the database and define the next unique conceptId");
				List<KnownConcept> knownConcepts = myDbCon.readConcepts();
				int currentMaxId = myDbCon.getCurrentMaxId();
				int nextId = (currentMaxId < params.getMinId()) ? params.getMinId() : currentMaxId++;

				LOG.info("create new knownConceptProcessor object that contains all knownConcepts");
				KnownConceptProcessor knownConceptProcessor = new KnownConceptProcessor(knownConcepts, nextId);

				LOG.info("found {} in the db", knownConceptProcessor.getSize());
				LOG.info("next new valid conceptId will be {}", nextId);
				LOG.info("connect to the file that needs to be loaded into the concept table");
				FileConnection myFileCon = new FileConnection(params);

				LOG.info("read all concepts from the ICD10 file into imported Concepts object");

				List<ImportConcept> importedConcepts = myFileCon.readConcepts();
				LOG.info("found {} concepts in the import file", importedConcepts.size());

				LOG.info("processing synchronization, please wait...");
				// set counter for new and updated records to zero
				int newRecords = 0;
				int updatedRecords = 0;

				LOG.info("double iterate through known concepts vs. imported ones");
				for (ImportConcept importedC : importedConcepts) {

					// check if we have the imported code in the table
					KnownConcept kc = knownConceptProcessor.findByCode(importedC.getCode());

					// case-1: we found a concept for the given code
					if (kc != null) {

						LOG.info("case-1: we found a concept ({}) for the given code", kc.toString());
						LOG.info("known concept name \"{}\" - imported concept name \"{}\"", kc.getConceptName(),
								importedC.getName());

						// case-1.1: name of known and imported concept are equal

						if (kc.getConceptName().equals(importedC.getName())) {
							LOG.trace("case-1.1: name of known and imported concept are equal - no action needed");
							// no action needed, we throw away the new concept from the file list, since it
							// is the same as already in the database
						}
						// case-1.2: name of known and imported concept are different
						else {
							LOG.info(
									"case-1.2: name of known and imported concept are different - update the existing known concept record and invalidate it");

							// update the existing known concept record and invalidate it
							kc.setInvalidReason("U");
							kc.setValidEndDate(params.getCurrentValidEndDate());
							myDbCon.updateConcept(kc);

							// increase counter for updated concept records
							updatedRecords++;

							// now create a new concept record and validate it
							KnownConcept newConcept = new KnownConcept().setDefaults()
									.setConceptCode(importedC.getCode()).setConceptName(importedC.getName())
									// ensure correct concept_id is generated
									.setConceptId(knownConceptProcessor.getNextConceptIdAndIncrement())
									.setValidStartDate(params.getCurrentValidStartDate());
							myDbCon.insertConcept(newConcept);

							// increase counter for new concept records
							newRecords++;
						}
					}
					// case-2: nothing found, we just create the new concept
					else {

						LOG.info("case-2: nothing found, we just create the new concept");

						// now create a new concept record and validate it
						KnownConcept newConcept = new KnownConcept().setDefaults().setConceptCode(importedC.getCode())
								.setConceptName(importedC.getName())
								.setConceptId(knownConceptProcessor.getNextConceptIdAndIncrement())
								.setValidStartDate(params.getCurrentValidStartDate());
						myDbCon.insertConcept(newConcept);
						// increase counter for new concept records
						newRecords++;

						LOG.info("NEW2: {}", newConcept.toString());
					}
				}

				LOG.info("done -> new records: {}, updated records: {} ", newRecords, updatedRecords);
				LOG.info("exiting");

			} else
				// System.err.println("connection error! aborting...");
				LOG.error("connection error! aborting...");

		} catch (DbException ex) {
			LOG.error(ex.getCause().toString());
		} catch (FileConnectionException ex) {
			LOG.error(ex.getCause().toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error(ex.getCause().toString());
		}
	}

	public static void printExpectedParams() {
		LOG.info("***parameter expected***");
		LOG.info("ICD10GM Generator, expected parameters... ");

		LOG.info(
				"java -jar icd10gmgen.jar <currentYear> <inputFile> <database> <dbuser> <dbpassword> <table> <dbserverIp> <dbServerPort>  <encoding>");
		LOG.info("************************");
	}
}
