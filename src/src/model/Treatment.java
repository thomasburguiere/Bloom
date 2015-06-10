/**
 * @author mhachet
 */
package src.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import src.model.RasterTreatment;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


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
	ConnectionDatabase newConnectionDeleteClean = new ConnectionDatabase();
	ArrayList<String> messagesClean = new ArrayList<String>();
	messagesClean.add("\n--- Delete Clean table ---");
	messagesClean.addAll(newConnectionDeleteClean.dropTable("Workflow.Clean_" + this.getNbSessionRandom()));

	for(int i = 0 ; i < messagesClean.size() ; i++){
	    System.out.println(messagesClean.get(i));
	}

	
	ConnectionDatabase newConnectionDeleteDarwin = new ConnectionDatabase();
	ArrayList<String> messagesDarwin = new ArrayList<String>();
	messagesDarwin.add("\n--- Delete DarwinCoreInput table ---");
	messagesDarwin.addAll(newConnectionDeleteDarwin.deleteTable("Workflow.DarwinCoreInput", this.getNbSessionRandom()));

	for(int i = 0 ; i < messagesDarwin.size() ; i++){
	    System.out.println(messagesDarwin.get(i));
	}
	 
	
	ConnectionDatabase newConnectionDeleteTemp = new ConnectionDatabase();
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
    public void mappingDwC(MappingDwC mappingDWC) throws IOException{
	mappingDWC.setConnectionValuesTags(mappingDWC.doConnectionValuesTags());
	File mappedFile = mappingDWC.createNewDwcFile(this.getNbSessionRandom());
	mappingDWC.setMappedFile(mappedFile);
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
    public List <String> initialiseFile(File inputFile, int nbFile) throws IOException{

	fileDarwinCore = new DarwinCore(inputFile, nbFile, this.getNbSessionRandom());
	fileDarwinCore.readDarwinCoreFile();
	List<String> listLinesDarwinCore = fileDarwinCore.getDarwinLines();

	return listLinesDarwinCore;
    }

    /**
     * Modified initial files in order to fill in table DarwinCoreInput
     * @param List<String> linesInputModified
     * @param int nbFile
     * @throws IOException
     * @return File temporary
     */
    public File createTemporaryFile(List<String> linesInputModified, int nbFile) throws IOException{

	if(!new File(DIRECTORY_PATH + "temp/").exists()){
	    new File(DIRECTORY_PATH + "temp/").mkdirs();
	}
	if(!new File(DIRECTORY_PATH + "temp/data/").exists()){
	    new File(DIRECTORY_PATH + "temp/data/").mkdirs();
	}
	File tempFile = new File(DIRECTORY_PATH + "temp/data/inputFile_" + Integer.toString(nbFile) + ".csv");
	FileWriter writer = null;
	try{
	    writer = new FileWriter(tempFile);
	    for(int i = 0 ; i < linesInputModified.size() ; i++){
		writer.write(linesInputModified.get(i) + "\n");
	    }
	}catch(IOException ex){
	    ex.printStackTrace();
	}finally{
	    if(writer != null){
		writer.close();
	    }
	}
	return tempFile;
    }

    /**
     * Create sql request to insert input file modified (temporary) in DarwinCoreInput table
     * 
     * @param File inputDarwinCoreModified
     * @param List<String> linesInputFile
     * @return String sql request
     */
    public String createSQLInsert(File inputDarwinCoreModified, List<String> linesInputFile){
	String sql = "";		
	String firstLine = linesInputFile.get(0).replace("\t", "_,");
	sql = "LOAD DATA LOCAL INFILE '" + inputDarwinCoreModified.getAbsolutePath() + "' INTO TABLE Workflow.DarwinCoreInput FIELDS TERMINATED BY ',' ENCLOSED BY '\"' IGNORE 1 LINES (" + firstLine + ");";
	return sql;
    }

    /**
     * Create DarwinCoreInput table in the database from input file(s)
     * 
     * @param String insertFileSQL
     * @return void
     */
    public void createTableDarwinCoreInput(String insertFileSQL){
	ConnectionDatabase newConnection = new ConnectionDatabase();
	String choiceStatement = "execute";
	ArrayList<String> messages = new ArrayList<String>();
	messages.add("\n--- Create DarwinCoreInput table ---");
	messages.addAll(newConnection.newConnection(choiceStatement, insertFileSQL));

	for(int i = 0 ; i < messages.size() ; i++){
	    System.out.println(messages.get(i));
	}
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
	    treatmentSynonyms.setNbSessionRandom(this.getNbSessionRandom());
	    treatmentSynonyms.updateClean();

	}

	this.setNbSynonymInvolved(treatmentSynonyms.getNbSynonymInvolved());
    }

    public void tdwgCodeOption(){
	TdwgTreatment tdwg4Treatment = new TdwgTreatment();
	tdwg4Treatment.setDIRECTORY_PATH(this.getDIRECTORY_PATH());
	tdwg4Treatment.setRESSOURCES_PATH(this.getRESSOURCES_PATH());
	tdwg4Treatment.setNbSessionRandom(this.getNbSessionRandom());
	
	tdwg4Treatment.checkIsoTdwgCode(fileDarwinCore);
    }

    public GeographicTreatment checkGeographicOption(){
	GeographicTreatment geoTreatment = new GeographicTreatment(this.getFileDarwinCore());
	
	geoTreatment.setDIRECTORY_PATH(this.getDIRECTORY_PATH());
	geoTreatment.setRESSOURCES_PATH(this.getRESSOURCES_PATH());
	geoTreatment.setNbSessionRandom(this.getNbSessionRandom());
	
	geoTreatment.geoGraphicTreatment();
	
	File wrongGeo = this.createFileCsv(geoTreatment.getWrongGeoList(), "wrong/wrong_geospatialIssues_" + this.getNbSessionRandom() + ".csv");
	geoTreatment.setWrongGeoFile(wrongGeo);
	
	File wrongCoord = this.createFileCsv(geoTreatment.getWrongCoordinatesList(), "wrong/wrong_coordinates_" + this.getNbSessionRandom() + ".csv");
	geoTreatment.setWrongCoordinatesFile(wrongCoord);
	
	File wrongPolygon = this.createFileCsv(geoTreatment.getWrongPolygonList(), "wrong/wrong_polygon_" + this.getNbSessionRandom() + ".csv");
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

    
    public File establishmentMeansOption(ArrayList<String> listEstablishmentChecked){
	EstablishmentTreatment establishTreatment = new EstablishmentTreatment(listEstablishmentChecked);
	establishTreatment.setDIRECTORY_PATH(this.getDIRECTORY_PATH());
	establishTreatment.setRESSOURCES_PATH(this.getRESSOURCES_PATH());
	establishTreatment.setNbSessionRandom(this.getNbSessionRandom());
	
	establishTreatment.establishmentMeansTreatment();
	ArrayList<String> noEstablishment = establishTreatment.getNoEstablishmentList();
	
	File noEstablishmentFile = this.createFileCsv(noEstablishment, "wrong/noEstablishmentMeans_" + this.getNbSessionRandom() + ".csv");
	
	return noEstablishmentFile;
    }
    
    /**
     * Create a new csv file from lines 
     * 
     * @param ArrayList<String> linesFile
     * @param String fileName
     * @return File 
     */
    public File createFileCsv(ArrayList<String> linesFile, String fileName){
	if(!new File(DIRECTORY_PATH + "temp/").exists())
	{
	    new File(DIRECTORY_PATH + "temp/").mkdirs();
	}
	if(!new File(DIRECTORY_PATH + "temp/wrong/").exists())
	{
	    new File(DIRECTORY_PATH + "temp/wrong/").mkdirs();
	}
	//String fileRename = fileName + "_" + nbFileRandom + ".csv";
	File newFile = new File(DIRECTORY_PATH + "temp/" + fileName);
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

