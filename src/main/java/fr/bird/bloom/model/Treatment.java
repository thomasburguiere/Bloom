/**
 * @author mhachet
 */
package fr.bird.bloom.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;


/**
 *
 * src.model
 *
 * TreatmentData.java
 */
public class Treatment {

	private DarwinCore fileDarwinCore;
	private ArrayList<File> rasterFiles;
	private HashMap<Integer, HashMap<String, Boolean>> hashMapValidOrNot;
	private String nbSessionRandom;
	private String DIRECTORY_PATH = "";
	private String RESSOURCES_PATH = "";
	private int nbSynonymInvolved = 0;


	/**
	 *
	 * package model
	 * TreatmentData
	 */
	public Treatment(){

	}

	/**
	 * Delete Clean and temp tables. 
	 * Delete DarwinCoreInput table.
	 *
	 * @return void
	 */
	public void deleteTables(){
		Statement statementDeleteClean = null;
		try {
			statementDeleteClean = ConnectionDatabase.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseTreatment newConnectionDeleteClean = new DatabaseTreatment(statementDeleteClean);
		ArrayList<String> messagesClean = new ArrayList<String>();
		messagesClean.add("\n--- Delete Clean table ---");
		messagesClean.addAll(newConnectionDeleteClean.dropTable("Workflow.Clean_" + this.getNbSessionRandom()));

		for(int i = 0 ; i < messagesClean.size() ; i++){
			System.out.println(messagesClean.get(i));
		}


		Statement statementDeleteDarwin = null;
		try {
			statementDeleteDarwin = ConnectionDatabase.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseTreatment newConnectionDeleteDarwin = new DatabaseTreatment(statementDeleteDarwin);
		ArrayList<String> messagesDarwin = new ArrayList<String>();
		messagesDarwin.add("\n--- Delete DarwinCoreInput table ---");
		messagesDarwin.addAll(newConnectionDeleteDarwin.deleteTable("Workflow.DarwinCoreInput", this.getNbSessionRandom()));

		for(int i = 0 ; i < messagesDarwin.size() ; i++){
			System.out.println(messagesDarwin.get(i));
		}


		Statement statementDeleteTemp = null;
		try {
			statementDeleteTemp = ConnectionDatabase.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseTreatment newConnectionDeleteTemp = new DatabaseTreatment(statementDeleteTemp);
		ArrayList<String> messagesTemp = new ArrayList<String>();
		messagesTemp.add("\n--- Delete temp table ---");
		messagesTemp.addAll(newConnectionDeleteTemp.dropTable("Workflow.temp_" + this.getNbSessionRandom()));

		for(int i = 0 ; i < messagesTemp.size() ; i++){
			System.out.println(messagesTemp.get(i));
		}


	}

	/**
	 * Convert input file (not DwC) to DwC format
	 *
	 * @param mappingDWC
	 * @throws IOException
	 * @return void
	 */
	public void mappingDwC(MappingDwC mappingDWC, int idFile) throws IOException{

		mappingDWC.setConnectionValuesTags(mappingDWC.doConnectionValuesTags());
		mappingDWC.findInvalidColumns();
		File mappedFile = mappingDWC.createNewDwcFile(this.getNbSessionRandom(), idFile);

		mappingDWC.setMappedFile(mappedFile);
	}

	/**
	 * change name from reconcile service
	 *
	 * @param reconcileService
	 * @param referenceFileReconcile
	 * @param idFile
	 */
	public void reconcileService(ReconciliationService reconcileService, CSVFile referenceFileReconcile, int idFile){

		String tagReconcile = reconcileService.getReconcileTagBased();
		int tagReconcileColumn = 0;
		HashMap<Integer, String> linesConnectedNewName = reconcileService.getLinesConnectedNewName();
		ArrayList<String> listLinesReconciled = new ArrayList<>();
		try{
			System.out.println(referenceFileReconcile.getCsvFile().getAbsolutePath());
			InputStream inputStreamReference = new FileInputStream(referenceFileReconcile.getCsvFile());
			InputStreamReader inputStreamReaderReference = new InputStreamReader(inputStreamReference);
			BufferedReader readerReference = new BufferedReader(inputStreamReaderReference);
			String line = "";
			int countLine = 0;
			while ((line = readerReference.readLine()) != null){
				//System.out.println("separator : " + referenceFileReconcile.getSeparator());
				String [] lineSplit = line.split(referenceFileReconcile.getSeparator(), -1);

				if(countLine == 0){
					for(int i = 0; i < lineSplit.length; i++){
						if(lineSplit[i].equals(tagReconcile)){
							tagReconcileColumn = i;
						}
					}

				}
				else{
					for(Entry<Integer, String> entry : linesConnectedNewName.entrySet()){
						if(entry.getKey() + 1 == countLine){
							lineSplit[tagReconcileColumn] = entry.getValue();
						}

					}
				}
				String newLine = StringUtils.join(lineSplit, ",");
				listLinesReconciled.add(newLine);
				countLine++;
			}
			System.err.println("reconcile true");
			reconcileService.setSuccessReconcile(Boolean.toString(true));

		}
		catch(Exception e){
			System.err.println("error false : " + e);
			reconcileService.setSuccessReconcile(Boolean.toString(false));

		}
		File reconcileFile = this.createFileCsv(listLinesReconciled, "reconcile_" + this.getNbSessionRandom() + "_" + idFile + ".csv", "data");
		reconcileService.setReconcileFile(reconcileFile);
	}

	/**
	 * Create a DarwinCore class for each file.
	 * Initial file will be modified thanks to readFile()
	 *
	 * @param inputFile
	 * @param nbFile
	 * @throws IOException
	 * @return List<String>
	 */
	public DarwinCore initialiseFile(File inputFile, int nbFile, String separator) throws IOException{

		fileDarwinCore = new DarwinCore(inputFile, nbFile, this.getNbSessionRandom());
		fileDarwinCore.setDIRECTORY_PATH(DIRECTORY_PATH);
		File darwinCoreFileTemp = fileDarwinCore.readDarwinCoreFile(separator);
		fileDarwinCore.setDarwinCoreFileTemp(darwinCoreFileTemp);
		System.out.println("filename : " + darwinCoreFileTemp.getAbsolutePath());
		return fileDarwinCore;
	}


	/**
	 * Create sql request to insert input file modified (temporary) in DarwinCoreInput table
	 *
	 * @param File inputDarwinCoreModified
	 * @param List<String> linesInputFile
	 * @return String sql request
	 */
	public String createSQLInsert(File inputDarwinCoreModified){
		String sql = "";
		BufferedReader br = null;
		InputStream in = null;
		String firstLine = "";
		int count = 0;
		try{
			in = new FileInputStream(inputDarwinCoreModified);
			br = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = br.readLine() ) != null ){
				if(count == 0){
					firstLine = line.replace("\'","").replace("\"", "").replace("\t", "_,");
					break;
				}
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
		finally{
			if ( br != null ){
				try{
					br.close();
				}catch(Exception e){}
			}
		}
		sql = "LOAD DATA LOCAL INFILE '" + inputDarwinCoreModified.getAbsolutePath() + "' INTO TABLE Workflow.DarwinCoreInput FIELDS TERMINATED BY ',' ENCLOSED BY '\"' IGNORE 1 LINES (" + firstLine + ");";
		System.out.println("mistake : " + sql);
		return sql;
	}

	/**
	 * Create DarwinCoreInput table in the database from input file(s)
	 *
	 * @param String insertFileSQL
	 * @return void
	 */
	public void createTableDarwinCoreInput(DarwinCore darwinCoreModified){
		File darwinCoreFile = darwinCoreModified.getDarwinCoreFileTemp();
		BufferedReader buff = null;

		try {
			buff = new BufferedReader(new FileReader(darwinCoreFile));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int countLine = 0;
		String firstLine = "";
		String line;
		try {
			while ((line = buff.readLine()) != null) {
				if(countLine == 0){
					firstLine = line;
				}
				else{
					String sqlInsert = "INSERT INTO Workflow.DarwinCoreInput (" + firstLine + ") VALUES (" + line + ");";
					Statement statement = null;
					try {
						statement = ConnectionDatabase.getInstance().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DatabaseTreatment newConnection = new DatabaseTreatment(statement);
					String choiceStatement = "execute";
					ArrayList<String> messages = new ArrayList<String>();
					messages.add("\n--- Insert line " + countLine + " in DarwinCoreInput table ---");
					messages.add(countLine + " => " + sqlInsert);
					messages.addAll(newConnection.executeSQLcommand(choiceStatement, sqlInsert));
					/*for(int i = 0 ; i < messages.size() ; i++){
						writer.write(messages.get(i));
					}*/
				}
				countLine ++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				buff.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		//System.out.println("insertFileSQL : " + insertFileSQL);
		ConnectionDatabase newConnection = new ConnectionDatabase();
		String choiceStatement = "execute";
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("\n--- Create DarwinCoreInput table ---");
		//messages.addAll(newConnection.newConnection(choiceStatement, insertFileSQL));

		for(int i = 0 ; i < messages.size() ; i++){
			System.out.println(messages.get(i));
		}*/
	}

	/**
	 * Add synonyms for each taxon (if exist)
	 *
	 * @param includeSynonyms
	 * @return void
	 */
	public void includeSynonyms(File includeSynonyms){
		SynonymsTreatment treatmentSynonyms = null;

		if(includeSynonyms != null){
			treatmentSynonyms = new SynonymsTreatment(includeSynonyms);
			treatmentSynonyms.setNbSessionRandom(this.getNbSessionRandom());
			//treatmentSynonyms.getTagsSynonymsTempTable();
			treatmentSynonyms.createSynonymTempTable();
			treatmentSynonyms.updateCleanFromSynonymTemp();
		}
		else{
			treatmentSynonyms = new SynonymsTreatment();
			treatmentSynonyms.setDIRECTORY_PATH(this.getDIRECTORY_PATH());
			treatmentSynonyms.setRESSOURCES_PATH(this.getRESSOURCES_PATH());
			treatmentSynonyms.setNbSessionRandom(this.getNbSessionRandom());
			treatmentSynonyms.updateClean();

		}

		this.setNbSynonymInvolved(treatmentSynonyms.getNbSynonymInvolved());
	}

	/**
	 * Find tdwg level 4 for occurrences
	 *
	 * @return boolean
	 */
	public boolean tdwgCodeOption(){
		TdwgTreatment tdwg4Treatment = new TdwgTreatment();
		tdwg4Treatment.setDIRECTORY_PATH(this.getDIRECTORY_PATH());
		tdwg4Treatment.setRESSOURCES_PATH(this.getRESSOURCES_PATH());
		tdwg4Treatment.setNbSessionRandom(this.getNbSessionRandom());

		tdwg4Treatment.checkIsoTdwgCode(fileDarwinCore);

		boolean sucessTdwgTreatment = tdwg4Treatment.isSucessTdwgTreatment();

		return sucessTdwgTreatment;
	}

	/**
	 * process of geo checking 
	 *
	 * @return GeographicTreatment
	 */
	public GeographicTreatment checkGeographicOption(){
		GeographicTreatment geoTreatment = new GeographicTreatment(this.getFileDarwinCore());

		geoTreatment.setDIRECTORY_PATH(this.getDIRECTORY_PATH());
		geoTreatment.setRESSOURCES_PATH(this.getRESSOURCES_PATH());
		geoTreatment.setNbSessionRandom(this.getNbSessionRandom());

		geoTreatment.geoGraphicTreatment();

		File wrongGeo = this.createFileCsv(geoTreatment.getWrongGeoList(), "wrong_geospatialIssues_" + this.getNbSessionRandom() + ".csv", "wrong");
		geoTreatment.setWrongGeoFile(wrongGeo);

		File wrongCoord = this.createFileCsv(geoTreatment.getWrongCoordinatesList(), "wrong_coordinates_" + this.getNbSessionRandom() + ".csv", "wrong");
		geoTreatment.setWrongCoordinatesFile(wrongCoord);

		File wrongPolygon = this.createFileCsv(geoTreatment.getWrongPolygonList(), "wrong_polygon_" + this.getNbSessionRandom() + ".csv", "wrong");
		geoTreatment.setWrongPolygonFile(wrongPolygon);

		return geoTreatment;
	}


	/**
	 *
	 * Check if coordinates are included in raster cells
	 *
	 * @param ArrayList<File> raster file
	 * @return void
	 */
	public RasterTreatment checkWorldClimCell(ArrayList<File> rasterFiles) {

		RasterTreatment rasterTreatment = new RasterTreatment(rasterFiles, this);
		rasterTreatment.setDIRECTORY_PATH(this.getDIRECTORY_PATH());
		rasterTreatment.setRESSOURCES_PATH(this.getRESSOURCES_PATH());

		File matrixFileValidCells = rasterTreatment.treatmentRaster();
		rasterTreatment.setMatrixFileValidCells(matrixFileValidCells);

		return rasterTreatment;
	}


	/**
	 * process of establishmentMeans option
	 *
	 * @param listEstablishmentChecked
	 * @return EstablishmentTreatment
	 */
	public EstablishmentTreatment establishmentMeansOption(ArrayList<String> listEstablishmentChecked){
		EstablishmentTreatment establishTreatment = new EstablishmentTreatment(listEstablishmentChecked);
		establishTreatment.setDIRECTORY_PATH(this.getDIRECTORY_PATH());
		establishTreatment.setRESSOURCES_PATH(this.getRESSOURCES_PATH());
		establishTreatment.setNbSessionRandom(this.getNbSessionRandom());

		establishTreatment.establishmentMeansTreatment();
		ArrayList<String> noEstablishment = establishTreatment.getNoEstablishmentList();
		File wrongEstablishmentMeans = this.createFileCsv(noEstablishment, "noEstablishmentMeans_" + this.getNbSessionRandom() + ".csv", "wrong");
		establishTreatment.setWrongEstablishmentMeansFile(wrongEstablishmentMeans);

		return establishTreatment;
	}

	/**
	 * Create a new csv file from lines 
	 *
	 * @param ArrayList<String> linesFile
	 * @param String fileName
	 * @return File
	 */
	public File createFileCsv(ArrayList<String> linesFile, String fileName, String category){
		if(!new File(DIRECTORY_PATH + "temp/").exists()){
			new File(DIRECTORY_PATH + "temp/").mkdirs();
		}
		if(!new File(DIRECTORY_PATH + "temp/" + this.getNbSessionRandom()).exists()){
			new File(DIRECTORY_PATH + "temp/" + this.getNbSessionRandom());
		}
		if(!new File(DIRECTORY_PATH + "temp/" + this.getNbSessionRandom() + "/data/").exists()){
			new File(DIRECTORY_PATH + "temp/" + this.getNbSessionRandom() + "/data/").mkdirs();
		}
		if(!new File(DIRECTORY_PATH + "temp/" + this.getNbSessionRandom() + "/wrong/").exists()){
			new File(DIRECTORY_PATH + "temp/" + this.getNbSessionRandom() + "/wrong/").mkdirs();
		}
		if(!new File(DIRECTORY_PATH + "temp/" + this.getNbSessionRandom() + "/final_results/").exists()){
			new File(DIRECTORY_PATH + "temp/" + this.getNbSessionRandom() + "/final_results/").mkdirs();
		}

		//String fileRename = fileName + "_" + nbFileRandom + ".csv";
		File newFile = new File(DIRECTORY_PATH + "temp/" + this.getNbSessionRandom() + "/" + category +"/" + fileName);
		FileWriter writer = null;
		try {
			writer = new FileWriter(newFile);
			for(int i = 0 ; i < linesFile.size() ; i++){
				writer.write(linesFile.get(i) + "\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(fileName + " written !!!");
		return newFile;
	}

	/**
	 * Delete a directory
	 *
	 * @param path
	 * @return boolean
	 */
	public boolean deleteDirectory(File path){

		if(path.isDirectory()){

			String [] filesChild = path.list();

			for(int i = 0 ; i < filesChild.length ; i ++){
				System.out.println("delete : " + filesChild[i]);
				boolean succes = this.deleteDirectory(new File(path + "/" + filesChild[i]));
				if(!succes){
					return false;
				}
			}
		}

		return path.delete();

	}

	/**
	 *
	 * @return DarwinCore
	 */
	public DarwinCore getFileDarwinCore() {
		return fileDarwinCore;
	}

	/**
	 *
	 * @param fileDarwinCore
	 * @return void
	 */
	public void setFileDarwinCore(DarwinCore fileDarwinCore) {
		this.fileDarwinCore = fileDarwinCore;
	}

	/**
	 *
	 * @return ArrayList<File>
	 */
	public ArrayList<File> getRasterFiles() {
		return rasterFiles;
	}

	/**
	 *
	 * @param rasterFiles
	 * @return void
	 */
	public void setRasterFiles(ArrayList<File> rasterFiles) {
		this.rasterFiles = rasterFiles;
	}

	/**
	 *
	 * @return HashMap<Integer,HashMap<String,Boolean>>
	 */
	public HashMap<Integer, HashMap<String, Boolean>> getHashMapValidOrNot() {
		return hashMapValidOrNot;
	}

	/**
	 *
	 * @param hashMapValidOrNot
	 * @return void
	 */
	public void setHashMapValidOrNot(
			HashMap<Integer, HashMap<String, Boolean>> hashMapValidOrNot) {
		this.hashMapValidOrNot = hashMapValidOrNot;
	}

	/**
	 *
	 * @return int
	 */
	public String getNbSessionRandom() {
		return nbSessionRandom;
	}

	/**
	 *
	 * @param nbFileRandom
	 * @return void
	 */
	public void setNbSessionRandom(String nbFileRandom) {
		this.nbSessionRandom = nbFileRandom;
	}

	public String getDIRECTORY_PATH() {
		return DIRECTORY_PATH;
	}

	public void setDIRECTORY_PATH(String dIRECTORY_PATH) {
		DIRECTORY_PATH = dIRECTORY_PATH;
	}

	public String getRESSOURCES_PATH() {
		return RESSOURCES_PATH;
	}

	public void setRESSOURCES_PATH(String rESSOURCES_PATH) {
		RESSOURCES_PATH = rESSOURCES_PATH;
	}

	public int getNbSynonymInvolved() {
		return nbSynonymInvolved;
	}

	public void setNbSynonymInvolved(int nbSynonymInvolved) {
		this.nbSynonymInvolved = nbSynonymInvolved;
	}


}

