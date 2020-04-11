package de.zmi.icd10gmgen.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import de.zmi.icd10gmgen.exception.DbException;
import de.zmi.icd10gmgen.model.KnownConcept;
import de.zmi.icd10gmgen.model.Params;

public class DbConnection {

	// db characteristics
	private Params params = null;
	private Connection con = null;
	private String jdbcurl = null;

	// database meta
	private boolean isConnected = false;
	private String dbVersion = null;
	
	// construction
	public DbConnection(Params params ) throws DbException {
		this.params = params;
		
		// construct JDBC connection URL
        this.jdbcurl = "jdbc:postgresql://"+params.getDbServerIp()+":"+params.getDbServerPort()+"/"+params.getDatabase();

        try {
        	this.con = DriverManager.getConnection(this.jdbcurl, params.getDbUserName(), params.getDbUserPassword());
            Statement st = con.createStatement();
            //st.executeQuery("SET CLIENT_ENCODING TO 'ISO-8859-1'");
            //st.execute("SET CLIENT_ENCODING TO Unicode");
            
            ResultSet rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
            	isConnected = true;
            	dbVersion = rs.getString(1);
            }
            

        } catch (SQLException ex) {
        	ex.printStackTrace();
        	throw new DbException();
        }
	}	

	public String getDbVersion() {
		return this.dbVersion;
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public int getCurrentMaxId() throws SQLException {
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT MAX(concept_id) FROM " + params.getTable());
		if(rs.next()) {
			return rs.getInt(1);
		}
		return 0;
	}
	
	public List<KnownConcept> readConcepts() throws SQLException {
		List<KnownConcept> concepts = new ArrayList<KnownConcept>();
		
		System.out.println("::: querying all records of "+params.getTable());
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM " + params.getTable());
		System.out.println("::: processing results");
		
		while(rs.next()) {
			KnownConcept knownC = new KnownConcept(
				rs.getInt("concept_id"),
				rs.getString("concept_name"),
				rs.getString("domain_id"),
				rs.getString("vocabulary_id"),
				rs.getString("concept_class_id"),
				rs.getString("standard_concept"),
				rs.getString("concept_code"),
				rs.getString("valid_start_date"),
				rs.getString("valid_end_date"),
				rs.getString("invalid_reason")
			);
			concepts.add(knownC);
		}
		return concepts;
	}
	
	public DbConnection insertConcept(KnownConcept newConcept) throws SQLException {
		Statement st = con.createStatement();

		String query = "INSERT INTO " + 
				params.getTable() + 
				" (concept_id, concept_name, domain_id, vocabulary_id, concept_class_id, standard_concept, concept_code, valid_start_date, valid_end_date, invalid_reason) VALUES (" +
				newConcept.getConceptId() + ", '" + 
				newConcept.getConceptName() + "', '" + 
				newConcept.getDomainId() + "', '" + 
				newConcept.getVocabularyId() + "', '" + 
				newConcept.getConceptClassId() + "', '" + 
				newConcept.getStandardConcept() + "', '" + 
				newConcept.getConceptCode() + "', '" + 
				newConcept.getValidStartDate() + "', '" + 
				newConcept.getValidEndDate() + "', '" + 
				newConcept.getInvalidReason() + "')"; 
		st.execute(query);
		return this;
	}
	
	/**
	 * updates a concept in a table
	 * 
	 * @param existingConcept
	 * @return
	 * @throws SQLException
	 */
	public DbConnection updateConcept(KnownConcept existingConcept) throws SQLException {
		Statement st = con.createStatement();
		String query = "UPDATE " + params.getTable() + 
				" SET valid_end_date = '" + existingConcept.getValidEndDate() + "', " + 
				" invalid_reason = '" + existingConcept.getInvalidReason() + "' WHERE concept_id=" + existingConcept.getConceptId();
		st.execute(query);
		return this;
	}
	
}
