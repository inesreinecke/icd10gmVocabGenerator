package de.zmi.icd10gmgen.connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.zmi.icd10gmgen.exception.InvalidDataException;
import de.zmi.icd10gmgen.model.ImportConcept;
import de.zmi.icd10gmgen.model.Params;

public class FileConnection {

	private BufferedReader reader = null;
	
	public FileConnection(Params params) {
		try {
			File file = new File(params.getFilename());
			
			reader = new BufferedReader(
			   new InputStreamReader(new FileInputStream(file), params.getEncoding()));
			
			// reader = new BufferedReader(new FileReader( filename ));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<ImportConcept> readConcepts() {
		List<ImportConcept> result = new ArrayList<ImportConcept>();
		

		try {
			if(reader != null) {
				String line = reader.readLine();
				while (line != null) {
					if(line != null) {
//						System.out.println("LINE:" + line.toString());
						String split[] = line.split(";");
						if(split.length == 2) { 
							try {
								ImportConcept newConcept = new ImportConcept(split[0], split[1]);
								result.add(newConcept);
							} catch(InvalidDataException ignored) {}
						}
					}	
					// now read a new line
					line = reader.readLine();
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		return result;
	}
}
