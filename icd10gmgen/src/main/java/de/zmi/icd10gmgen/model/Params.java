package de.zmi.icd10gmgen.model;

public class Params {
	private String year;
	private String filename;
	private String database;
	private String dbUserName;
	private String dbUserPassword;
	private String table;
	private String dbServerIp;
	private int dbServerPort;
	private String encoding = "ISO-8859-1";
	private int minId = 2000000;
	
	public Params() {
	}

	public Params getDefaults() {
		this.year = "2002";
		this.filename = "//Users//wabommel//work//coding//ICD10GM//allCodingsByYear//2004.txt";
		this.database = "OHDSI";
		this.dbUserName = "postgres";
		this.dbUserPassword = "omop";
		this.table = "dev_cdm.concept";
		this.dbServerIp = "127.0.0.1";
		this.dbServerPort = 5432;
		return this;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbUserPassword() {
		return dbUserPassword;
	}

	public void setDbUserPassword(String dbUserPassword) {
		this.dbUserPassword = dbUserPassword;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getDbServerIp() {
		return dbServerIp;
	}

	public void setDbServerIp(String dbServerIp) {
		this.dbServerIp = dbServerIp;
	}

	public int getDbServerPort() {
		return dbServerPort;
	}

	public void setDbServerPort(int dbServerPort) {
		this.dbServerPort = dbServerPort;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
	public int getMinId() {
		return minId;
	}

	public void setMinId(int minId) {
		this.minId = minId;
	}

	/**
	 * based on the year that was given, we substract one day (so 12-31 of the former year)
	 * @return
	 */
	// CurrentValidEndDate is calculated by the given year minus one and adding -12-31 to the string
	// this works for postgreSQL with String even if we enter this into a database column of type date
	public String getCurrentValidEndDate() {
		int yearInt = Integer.parseInt(this.year);
		int previousYearInt = yearInt - 1;
		return previousYearInt + "-12-31";
	}
	// CurrentValidStartDate is calculated by the given year and adding -01-01 to the string
	// this works for postgreSQL with String even if we enter this into a database column of type date
	public String getCurrentValidStartDate() {
		return this.year + "-01-01";
	}
	
}
