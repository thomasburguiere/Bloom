package src.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * package model
 * 
 * DarwinCore
 */
public class DarwinCore {

	private int idFile_;
	private final File file;
	private List<String> inputParsingFile;

	/**
	 * 
	 * package model
	 * 
	 * DarwinCore
	 * @param file
	 * @param nbFile
	 */
	public DarwinCore(File file, int nbFile){
		this.file = file;
		this.idFile_ = nbFile;
	}
	
	/**
	 * Modified input file to format it.
	 * 
	 * @return List<String>
	 */
	public List<String> readFile() throws IOException {
		
		inputParsingFile = new ArrayList<String>();
		FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        
        for (String line = br.readLine(); line != null; line = br.readLine()) {
        	inputParsingFile.add(line);
        }
        
		String firstLine = inputParsingFile.get(0);
        String firstNewLine = "";
		List<String> tags = Arrays.asList(firstLine.split(","));
		
		for(int i = 0 ; i < tags.size() ; i++){
			String newTag = tags.get(i) + "_,";
			firstNewLine += newTag;
		}
		
		firstNewLine += "idFile_,changeProjSyst_,id_";
		inputParsingFile.set(0, firstNewLine);
		
		// on n'ajoute pas le numéro du fichier pour la première ligne
		for(int l = 1; l < inputParsingFile.size() ; l++){
			String line [] = inputParsingFile.get(l).split(",", -1);
			// parcourir la ligne
			String newLine = "";
			for(int j = 0 ; j < line.length ; j++){
				
				newLine += "\"" + line[j] + "\",";
			}
			newLine += Integer.toString(idFile_) + ",0," + "";
			//System.out.println(newLine);
			inputParsingFile.set(l, newLine);
		}
		
		br.close();
        fr.close();
        
        return inputParsingFile;
	}
	
	/**
	 * Get all latitude coordinates clean from input file.
	 *  
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getDecimalLatitudeClean(){
		ConnectionDatabase newConnection = new ConnectionDatabase();
		
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("\n--- Select decimal latitude from coordinates ---");
		
		String sqlLatitude = "SELECT decimalLatitude_ FROM Workflow.Clean;";
		
		messages.addAll(newConnection.newConnection("executeQuery", sqlLatitude));
		ArrayList<String> resultatLatitude = newConnection.getResultatSelect();
		
		return resultatLatitude;
	}
	
	/**
	 * Get all longitude coordinates clean from input file.
	 *  
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getDecimalLongitudeClean(){
		ConnectionDatabase newConnection = new ConnectionDatabase();
		
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("\n--- Select decimal longitude from coordinates ---");
		
		String sqlLongitude = "SELECT decimalLongitude_ FROM Workflow.Clean;";
		
		messages.addAll(newConnection.newConnection("executeQuery", sqlLongitude));
		ArrayList<String> resultatLongitude = newConnection.getResultatSelect();
		
		return resultatLongitude;
	}
	
	public ArrayList<String> getIDClean(){
		ConnectionDatabase newConnection = new ConnectionDatabase();
		
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("\n--- Select id line from clean ---\n");
		
		String sqlID= "SELECT id_ FROM Workflow.Clean;";
		
		messages.addAll(newConnection.newConnection("executeQuery", sqlID));
		ArrayList<String> resultatID = newConnection.getResultatSelect();
		
		return resultatID;
	}
	
	public ArrayList<String> getGbifIDClean(){
		ConnectionDatabase newConnection = new ConnectionDatabase();
		
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("\n--- Select id line from clean ---\n");
		
		String sqlID= "SELECT gbifID_ FROM Workflow.Clean;";
		
		messages.addAll(newConnection.newConnection("executeQuery", sqlID));
		ArrayList<String> resultatGbifID = newConnection.getResultatSelect();
		
		return resultatGbifID;
	}
	
	public ArrayList<String> getID(){
	    ConnectionDatabase newConnection = new ConnectionDatabase();
		
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("\n--- Select id line from DarwinCoreInput ---\n");
		
		String sqlID= "SELECT id_ FROM Workflow.DarwinCoreInput;";
		
		messages.addAll(newConnection.newConnection("executeQuery", sqlID));
		ArrayList<String> resultatID = newConnection.getResultatSelect();
		
		return resultatID;
	}
	
	/**
	 * Get all iso2 code clean (countryCode_) from input file. 
	 * 
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getIso2Clean(){
		ConnectionDatabase newConnection = new ConnectionDatabase();
		
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("\n--- Select iso2 code ---");
		
		String sqlISO2 = "SELECT countryCode_ FROM Workflow.Clean;";
		
		messages.addAll(newConnection.newConnection("executeQuery", sqlISO2));
		ArrayList<String> resultatISO2 = newConnection.getResultatSelect();
		
		return resultatISO2;
	}
	
	/**
	 * Get file id
	 * 
	 * @return int
	 */
	public int getIdFile_() {
		return idFile_;
	}

	/**
	 * set id file
	 * 
	 * @return void
	 */
	public void setIdFile_(int idFile_) {
		this.idFile_ = idFile_;
	}

	/**
	 * Get all file lines
	 *  
	 * @return List<String>
	 */
	public List<String> getInputParsingFile() {
		return inputParsingFile;
	}

	/**
	 * Set all file lines
	 * @return void
	 */
	public void setInputParsingFile(List<String> inputParsingFile) {
		this.inputParsingFile = inputParsingFile;
	}

	/**
	 * Get file
	 * @return File
	 */
	public File getFile() {
		return file;
	}
}
