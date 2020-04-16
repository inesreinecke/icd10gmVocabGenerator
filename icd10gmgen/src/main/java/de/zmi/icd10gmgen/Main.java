package de.zmi.icd10gmgen;
import java.util.List;

import de.zmi.icd10gmgen.connection.DbConnection;
import de.zmi.icd10gmgen.connection.FileConnection;
import de.zmi.icd10gmgen.model.ImportConcept;
import de.zmi.icd10gmgen.model.KnownConcept;
import de.zmi.icd10gmgen.model.KnownConceptProcessor;
import de.zmi.icd10gmgen.model.Params;


public class Main {
	 public static void main(String[] args) {

		 // verify input parameters
		 if(args.length != 9 ) {
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
			 
			 if(myDbCon.isConnected()) {
				 System.out.println("connection successful");
				 System.out.println("db version: "+myDbCon.getDbVersion());
				 
				 // create new list of concepts from the database and define the next unique conceptId
				 List<KnownConcept> knownConcepts = myDbCon.readConcepts();
				 int currentMaxId = myDbCon.getCurrentMaxId();
				 int nextId = (currentMaxId < params.getMinId()) ? params.getMinId() : currentMaxId++;
					
				 // create new knownConceptProcessor object that contains all knownConcepts
				 KnownConceptProcessor knownConceptProcessor = new KnownConceptProcessor( knownConcepts, nextId );
				
				 System.out.println("found "+knownConceptProcessor.getSize()+" in the db");
				 System.out.println("next new valid conceptId will be "+nextId);
				 // connect to the file that needs to be loaded into the concept table
				 FileConnection myFileCon = new FileConnection(params);
				 
				 // read all concepts from the ICD10 file into imported Concepts object
				 List<ImportConcept> importedConcepts = myFileCon.readConcepts();
				 System.out.println("found "+importedConcepts.size()+" concepts in the import file");

				 
				 System.out.println("processing synchronization, please wait...");
				 // set counter for new and updated records to zero
				 int newRecords = 0;
				 int updatedRecords = 0;
				 
				 // double iterate through known concepts vs. imported ones
				 for(ImportConcept importedC : importedConcepts) {
					 
					 // check if we have the imported code in the table
					 KnownConcept kc = knownConceptProcessor.findByCode( importedC.getCode() );
					 
					 // case-1: we found a concept for the given code
					 if(kc != null) {
						 // case-1.1: name of known and imported concept are equal

						 if(kc.getConceptName().equals( importedC.getName() )) {
							 // no action needed, we throw away the new concept from the file list, since it is the same as already in the database
						 }
						 // case-1.2: name of known and imported concept are different
						 else {
							 // update the existing known concept record and invalidate it
							 kc.setInvalidReason("U");
							 kc.setValidEndDate( params.getCurrentValidEndDate() );
							 myDbCon.updateConcept(kc);

							 //increase counter for updated concept records
							 updatedRecords++;

							 // now create a new concept record and validate it
							 KnownConcept newConcept = new KnownConcept()
								 .setDefaults()
								 .setConceptCode(importedC.getCode())
								 .setConceptName(importedC.getName())
								 // ensure correct concept_id is generated
								 .setConceptId( knownConceptProcessor.getNextConceptIdAndIncrement() )
								 .setValidStartDate( params.getCurrentValidStartDate() );
							 myDbCon.insertConcept(newConcept);
							 
							//increase counter for new concept records
							 newRecords++;
						 }
					 }
					 // case-2: nothing found, we just create the new concept
					 else {
						 
						 // now create a new concept record and validate it
						 KnownConcept newConcept = new KnownConcept()
							 .setDefaults()
							 .setConceptCode(importedC.getCode())
							 .setConceptName(importedC.getName())
							 .setConceptId( knownConceptProcessor.getNextConceptIdAndIncrement() )
							 .setValidStartDate( params.getCurrentValidStartDate() );
						 myDbCon.insertConcept(newConcept);
						//increase counter for new concept records
						 newRecords++;
						 
						 System.out.println("NEW2: " + newConcept.toString());
					 }
				 }
				 
				 System.out.println("done -> new records: "+newRecords+ " updated records: "+updatedRecords);
				 System.out.println("exiting");
				 
			 }
			 else System.err.println("connection error! aborting...");
		 } catch(Exception ex) {
			 ex.printStackTrace();
		 }
	 }
	 
	 public static void printExpectedParams() {
		 System.out.println();
		 System.out.println("ICD10GM Generator, expected parameters... ");
		 System.out.println("java -jar icd10gmgen.jar <currentYear> <inputFile> <database> <dbuser> <dbpassword> <table> <dbserverIp> <dbServerPort>  <encoding>");
		 System.out.println();
	 }
}
