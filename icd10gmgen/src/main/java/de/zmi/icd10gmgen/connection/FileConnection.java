package de.zmi.icd10gmgen.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.zmi.icd10gmgen.exception.FileConnectionException;
import de.zmi.icd10gmgen.exception.InvalidDataException;
import de.zmi.icd10gmgen.model.ImportConcept;
import de.zmi.icd10gmgen.model.Params;

public class FileConnection {
	private static final Logger LOG = LoggerFactory.getLogger(FileConnection.class);

	private BufferedReader reader = null;

	public FileConnection(Params params) throws FileConnectionException {
		LOG.info("establish file connection with parameters");

		try {
			Integer dbServerPort = params.getDbServerPort();
			Integer minId = params.getMinId();

			LOG.info("db server ip: {}", params.getDbServerIp());
			LOG.info("db server port: {}", dbServerPort.toString());

			LOG.info("db user name: {}", params.getDbUserName());
			LOG.info("db table name: {}", params.getTable());

			LOG.info("file name: {}", params.getFilename());
			LOG.info("file encoding: {}", params.getEncoding());

			LOG.info("selected year: {}", params.getYear());
			LOG.info("start date: {}", params.getCurrentValidStartDate());
			LOG.info("end date: {}", params.getCurrentValidEndDate());

			LOG.info("min id: {}", minId.toString());
		} catch (NullPointerException e) {
			LOG.warn("could not log parameter - please check {} {}", System.lineSeparator(), e.getCause());
		}

		try {
			File file = new File(params.getFilename());

			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), params.getEncoding()));

			// reader = new BufferedReader(new FileReader( filename ));
		} catch (FileNotFoundException e) {
			throw new FileConnectionException("file not found: " + params.getFilename(), e);
		} catch (UnsupportedEncodingException e) {
			throw new FileConnectionException("unsupported encoding: " + params.getEncoding(), e);
		}
	}

	public List<ImportConcept> readConcepts() throws FileConnectionException {

		LOG.info("read concepts from database");

		List<ImportConcept> result = new ArrayList<ImportConcept>();

		try {
			if (reader != null) {
				String line = reader.readLine();
				while (line != null) {
					if (line != null) {
						// System.out.println("LINE:" + line.toString());
						String split[] = line.split(";");
						if (split.length == 2) {
							try {
								ImportConcept newConcept = new ImportConcept(split[0], split[1]);
								result.add(newConcept);
							} catch (InvalidDataException ignored) {
								LOG.trace("InvalidDataException - ignored item \" {}\"", line);
							}
						} else {
							// line that could not be splitted or has irregular length
							LOG.warn("line with irregular length that could not be splitted and will be ignored: {}",
									line);
						}
					}
					// now read a new line
					line = reader.readLine();
				}
				reader.close();
			}
		} catch (IOException e) {
			throw new FileConnectionException("IO Exception", e);
		}

		return result;
	}
}
