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
public class TreatmentData {

    private DarwinCore fileDarwinCore;
    private ArrayList<File> rasterFiles;
    private HashMap<Integer, HashMap<String, Boolean>> hashMapValidOrNot;
    private int nbFileRandom;
    private String DIRECTORY_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/";

    /**
     * 
     * package model
     * TreatmentData
     */
    public TreatmentData(){

    }

    /**
     * Drop Clean and temp tables. 
     * Delete DarwinCoreInput table.
     * 
     * @return void
     */
    public void deleteTables(){
	ConnectionDatabase newConnectionDeleteClean = new ConnectionDatabase();
	ArrayList<String> messagesClean = new ArrayList<String>();
	messagesClean.add("\n--- Delete Clean table ---");
	messagesClean.addAll(newConnectionDeleteClean.dropTable("Workflow.Clean"));

	for(int i = 0 ; i < messagesClean.size() ; i++){
	    System.out.println(messagesClean.get(i));
	}

	ConnectionDatabase newConnectionDeleteDarwin = new ConnectionDatabase();
	ArrayList<String> messagesDarwin = new ArrayList<String>();
	messagesDarwin.add("\n--- Delete DarwinCoreInput table ---");
	messagesDarwin.addAll(newConnectionDeleteDarwin.deleteTable("Workflow.DarwinCoreInput"));

	for(int i = 0 ; i < messagesDarwin.size() ; i++){
	    System.out.println(messagesDarwin.get(i));
	}

	ConnectionDatabase newConnectionDeleteTemp = new ConnectionDatabase();
	ArrayList<String> messagesTemp = new ArrayList<String>();
	messagesTemp.add("\n--- Delete temp table ---");
	messagesTemp.addAll(newConnectionDeleteTemp.dropTable("Workflow.temp"));

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
	File mappedFile = mappingDWC.createNewDwcFile(this.getNbFileRandom());
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

	fileDarwinCore = new DarwinCore(inputFile, nbFile);
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


    //Vérifier que le code iso2 existe et qu'il est bien inscrit dans la table IsoCode !!!
    /**
     * Create temporary table "temp" with only correct iso2 code in DarwinCoreInput table.
     * Iso2 code (countryCode_) is correct if it's contained in IsoCode table (iso2_).
     * 
     * @return void
     */
    public void deleteWrongIso2() {
	ConnectionDatabase newConnectionTemp = new ConnectionDatabase();
	ArrayList<String> messages = new ArrayList<String>();
	String choiceStatement = "executeUpdate";
	messages.add("\n--- Create temporary table with correct ISO2 ---");
	String sqlCreateTemp = "CREATE TABLE Workflow.temp AS SELECT DarwinCoreInput.* FROM Workflow.DarwinCoreInput,Workflow.IsoCode WHERE countryCode_=IsoCode.iso2_;";

	messages.addAll(newConnectionTemp.newConnection(choiceStatement, sqlCreateTemp));

	for(int i = 0 ; i < messages.size() ; i++){
	    System.out.println(messages.get(i));
	}
    }

    /**
     * From temp table, create a Clean table with correct geospatial coordinates :
     * -90 >= latitude > 0
     *  0 < latitude <= 90
     *  
     *  -180 >= longitude > 0
     *   0 < longitude <= 180
     *   
     *   tag "hasGeospatialIssues" = false
     *   
     *  @return void
     */
    public void createTableClean(){
	ConnectionDatabase newConnectionClean = new ConnectionDatabase();
	ArrayList<String> messages = new ArrayList<String>();
	String choiceStatement = "executeUpdate";
	messages.add("\n--- Create Table Clean from temporary table ---");
	String sqlCreateClean = "CREATE TABLE Workflow.Clean AS SELECT * FROM Workflow.temp WHERE " +
		"(decimalLatitude_!=0 AND decimalLatitude_<90 AND decimalLatitude_>-90 AND decimalLongitude_!=0 " +
		"AND decimalLongitude_>-180 AND decimalLongitude_<180) AND (hasGeospatialIssues_='false');";
	messages.addAll(newConnectionClean.newConnection(choiceStatement, sqlCreateClean));

	for(int i = 0 ; i < messages.size() ; i++){
	    System.out.println(messages.get(i));
	}
    }

    /**
     * Select wrong coordinates and write in a file:
     * latitude = 0 ; <-90 ; >90
     * longitude = 0 ; <-180 ; >180
     * 
     * @return File wrong coordinates
     */
    public File deleteWrongCoordinates(){
	ConnectionDatabase newConnection = new ConnectionDatabase();

	ArrayList<String> messages = new ArrayList<String>();
	messages.add("\n--- Select wrong coordinates ---");

	String sqlRetrieveWrongCoord = "SELECT * FROM Workflow.DarwinCoreInput WHERE decimalLatitude_=0 OR decimalLatitude_>90 OR decimalLatitude_<-90 OR decimalLongitude_=0 OR decimalLongitude_>180 OR decimalLongitude_<-180;";
	messages.addAll(newConnection.newConnection("executeQuery", sqlRetrieveWrongCoord));
	ArrayList<String> resultatSelect = newConnection.getResultatSelect();
	messages.add("nb lignes affectées : " + Integer.toString(resultatSelect.size() - 1));

	if(!new File(DIRECTORY_PATH + "temp/wrong/").exists())
	{
	    new File(DIRECTORY_PATH + "temp/wrong/").mkdirs();
	}

	File wrongCoor = this.createFileCsv(resultatSelect, "wrong/wrong_coordinates_" + this.getNbFileRandom() + ".csv");


	for(int j = 0 ; j < messages.size() ; j++){
	    System.out.println(messages.get(j));
	}

	return wrongCoor;
    }


    /**
     * Select wrong geospatial and write in a file :
     * tag "hasGeospatialIssues_" = true
     * 
     * @return File wrong geospatial
     */
    public File deleteWrongGeospatial(){
	ConnectionDatabase newConnection = new ConnectionDatabase();

	ArrayList<String> messages = new ArrayList<String>();
	messages.add("\n--- Select wrong geospatialIssues ---");

	String sqlRetrieveWrongGeo = "SELECT * FROM Workflow.DarwinCoreInput WHERE hasGeospatialIssues_='true' AND !(decimalLatitude_=0 OR decimalLatitude_>90 OR decimalLatitude_<-90 OR decimalLongitude_=0 OR decimalLongitude_>180 OR decimalLongitude_<-180);";
	messages.addAll(newConnection.newConnection("executeQuery", sqlRetrieveWrongGeo));

	ArrayList<String> resultatSelect = newConnection.getResultatSelect();

	messages.add("nb lignes affectées : " + Integer.toString(resultatSelect.size() - 1));

	if(!new File(DIRECTORY_PATH + "temp/wrong/").exists())
	{
	    new File(DIRECTORY_PATH + "temp/wrong/").mkdirs();
	}

	File wrongGeo = this.createFileCsv(resultatSelect, "wrong/wrong_geospatialIssues_" + this.getNbFileRandom() + ".csv");

	for(int j = 0 ; j < messages.size() ; j++){
	    System.out.println(messages.get(j));
	}

	return wrongGeo;
    }


    /**
     * Add synonyms for each taxon (if exist)
     * 
     * @param includeSynonyms
     * @return void
     */
    public void includeSynonyms(File includeSynonyms){

	if(includeSynonyms != null){
	    SynonymsTreatment treatmentSynonyms = new SynonymsTreatment(includeSynonyms);
	    //treatmentSynonyms.getTagsSynonymsTempTable();
	    treatmentSynonyms.createSynonymTempTable();
	    treatmentSynonyms.updateCleanFromSynonymTemp();
	}
	else{
	    SynonymsTreatment treatmentSynonymsDefault = new SynonymsTreatment();
	    treatmentSynonymsDefault.updateClean();

	}

    }

    /**
     * Found indice corresponding to tag name
     * 
     * @param tagName
     * @return int
     */
    public int getIndiceFromTag(String tagName){

	HashMap<String, ArrayList<String>> idAssoData = fileDarwinCore.getIdAssoData();

	for(String id_ : idAssoData.keySet()){
	    if(id_.equals("id_")){
		ArrayList<String> tagsList = idAssoData.get(id_);
		for(int i = 0 ; i < tagsList.size() ; i++){
		    if(tagsList.get(i).equals(tagName)){
			return i;
		    }
		}
	    }
	}
	return 0;
    }


    /**
     * Check if coordinates (latitude and longitude) are included in the country indicated by the iso2 code
     * 
     * @return void
     */
    public File getPolygonTreatment(){
	PolygonTreatment polygone = new PolygonTreatment();
	fileDarwinCore.associateIdData();

	ArrayList<String> listToDelete = new ArrayList<>();

	HashMap<String, ArrayList<String>> idAssoData = fileDarwinCore.getIdAssoData(); 

	int iLatitude = this.getIndiceFromTag("decimalLatitude_");
	int iLongitude = this.getIndiceFromTag("decimalLongitude_");
	int iIso2 = this.getIndiceFromTag("countryCode_");
	int iGbifID = this.getIndiceFromTag("gbifID_");

	for (String id_ : idAssoData.keySet()) {
	    if(!id_ .equals("id_")){
		ArrayList<String> listInfos = idAssoData.get(id_);

		float latitude = 0;
		float longitude = 0;
		String iso2 = "";
		String iso3 = "";
		String gbifId_ = "";

		latitude = Float.parseFloat(listInfos.get(iLatitude).replace("\"", ""));
		longitude = Float.parseFloat(listInfos.get(iLongitude).replace("\"", ""));
		iso2 = listInfos.get(iIso2);
		iso3 = this.convertIso2ToIso3(iso2);
		gbifId_ = listInfos.get(iGbifID);

		File geoJsonFile = new File(DIRECTORY_PATH + "src/ressources/gadm_json/" + iso3.toUpperCase() + "_adm0.json");
		GeometryFactory geometryFactory = new GeometryFactory();
		Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
		System.out.println("--------------------------------------------------------------");
		System.out.println("------------------ Check point in polygon --------------------");
		System.out.println("Lat : " + latitude + "\tLong : " +  longitude);
		System.out.println("id_ : " + id_ + "\tgbifID : " + gbifId_ + "\tIso3 : " + iso3 + "\tiso2 : " + iso2);

		boolean isContained = polygone.polygonContainedPoint(point, geoJsonFile);

		System.out.println("The point is contained in the polygone : " + isContained);
		System.out.println("--------------------------------------------------------------\n");

		if(!isContained){

		    ConnectionDatabase newConnectionSelectID = new ConnectionDatabase();
		    ArrayList<String> messagesSelectID = new ArrayList<String>();
		    String sqlSelectID = "SELECT * FROM Workflow.Clean WHERE Clean.id_=" + id_ + ";";
		    messagesSelectID.addAll(newConnectionSelectID.newConnection("executeQuery", sqlSelectID));
		    ArrayList<String> selectIDResults = newConnectionSelectID.getResultatSelect();

		    for(int j = 0 ; j < messagesSelectID.size() ; j++){
			System.out.println(messagesSelectID.get(j));
		    }

		    for(int k = 0 ; k < selectIDResults.size() ; k++){
			if(!listToDelete.contains(selectIDResults.get(k))){
			    listToDelete.add(selectIDResults.get(k));
			}
		    }


		    ConnectionDatabase newConnectionDeleteID = new ConnectionDatabase();
		    ArrayList<String> messagesDeleteID = new ArrayList<String>();
		    String sqlDeleteID = "DELETE FROM Workflow.Clean WHERE id_=" + id_ + ";";
		    messagesDeleteID.addAll(newConnectionDeleteID.newConnection("executeUpdate", sqlDeleteID));

		    for(int i = 0 ; i < messagesDeleteID.size() ; i++){
			System.out.println(messagesDeleteID.get(i));
		    }
		}

	    }
	}

	File wrongPolygon = this.createFileCsv(listToDelete, "wrong/wrongPolygon_" + this.getNbFileRandom() + ".csv");

	return wrongPolygon;
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
     * Convert the iso2 code (2 letters) to iso3 code (3 letters)
     * 
     * @param String iso2
     * @return String iso3
     */
    public String convertIso2ToIso3(String iso2){
	String iso3 = "";

	ConnectionDatabase newConnection = new ConnectionDatabase();

	ArrayList<String> messages = new ArrayList<String>();
	messages.add("\n--- Convert iso2 code to iso3 code ---");
	String sqlConvertIso2Iso3 = "SELECT iso3_ FROM Workflow.IsoCode WHERE iso2_ = \"" + iso2.replaceAll("\"", "") + "\";";
	messages.addAll(newConnection.newConnection("executeQuery", sqlConvertIso2Iso3));

	ArrayList<String> resultatConvert = newConnection.getResultatSelect();
	if(resultatConvert.size() != 2){
	    System.err.println("Several iso2");
	}
	else{
	    iso3 = resultatConvert.get(1).replaceAll("\"", "");
	}
	return iso3;
    }

    /**
     * 
     * Tdwg4 code is retrieved for each coordinates 
     * 
     * @return void
     */
    public void checkIsoTdwgCode(){
	//change example : locationID="TDWG:MXS-JA"
	PolygonTreatment tdwg4 = new PolygonTreatment();
	fileDarwinCore.associateIdData();
	HashMap<String, ArrayList<String>> idAssoData = fileDarwinCore.getIdAssoData(); 

	int iLatitude = this.getIndiceFromTag("decimalLatitude_");
	int iLongitude = this.getIndiceFromTag("decimalLongitude_");
	int iIso2 = this.getIndiceFromTag("countryCode_");

	for (String id_ : idAssoData.keySet()) {
	    if(!id_ .equals("id_")){
		ArrayList<String> listInfos = idAssoData.get(id_);

		float latitude = 0;
		float longitude = 0;
		String iso2 = "";

		latitude = Float.parseFloat(listInfos.get(iLatitude).replace("\"", ""));
		longitude = Float.parseFloat(listInfos.get(iLongitude).replace("\"", ""));
		iso2 = listInfos.get(iIso2);
		GeometryFactory geometryFactory = new GeometryFactory();
		Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
		System.out.println("--------------------------------------------------------------");
		System.out.println("---------------- Check point in TDWG4 code -------------------");
		System.out.println("Lat : " + latitude + "\tLong : " + longitude);
		System.out.print("iso2 : " + iso2);
		String tdwg4Code = "";
		try {
		    tdwg4Code = tdwg4.tdwg4ContainedPoint(point, iso2);
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		System.out.println("\ttdwg4 : " + tdwg4Code);
		System.out.println("--------------------------------------------------------------");

		ConnectionDatabase newConnectionSelectID = new ConnectionDatabase();
		String sqlSelectID = "SELECT locationID_ FROM Workflow.Clean WHERE Clean.id_=" + id_ + ";";
		newConnectionSelectID.newConnection("executeQuery", sqlSelectID);
		ArrayList<String> selectIDResults = newConnectionSelectID.getResultatSelect();
		System.out.println(selectIDResults);
		String newLocationID = "";
		if(selectIDResults.size() > 1 || !selectIDResults.get(1).replaceAll("\"", "").equals(" ")){
		    newLocationID = selectIDResults.get(1).replaceAll("\"", "") + ";TDWG=" + tdwg4Code;
		}
		else{
		    newLocationID = "TDWG=" + tdwg4Code;
		}
		
		String sqlUpdateTDWG = "UPDATE Workflow.Clean SET Clean.locationID_=\"" + newLocationID + "\" WHERE Clean.id_=" + id_ + ";";
		//System.out.println(sqlUpdateTDWG);
		ConnectionDatabase newConnectionUpdateClean = new ConnectionDatabase();
		newConnectionUpdateClean.newConnection("executeUpdate", sqlUpdateTDWG);
		
	    }
	}
    }

    /**
     * 
     * Check if coordinates are included in raster cells
     * 
     * @param ArrayList<File> raster file
     * @return void
     */
    public File checkWorldClimCell(ArrayList<File> rasterFiles) {

	RasterTreatment rasterTreatment = new RasterTreatment(rasterFiles, this);

	File matrixFileValidCells = rasterTreatment.treatmentRaster();

	return matrixFileValidCells;
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
     * Filter on establishmentMeans
     * 
     * @param establishmentList
     * @return void
     */
    public File establishmentMeansOption(ArrayList<String> establishmentList){

	// list containing tags "establishmentMeans" to delete
	// inversed list of the begining (user want to keep the others) 
	ArrayList<String> noEstablishment = new ArrayList<>();

	for(int i = 0; i < establishmentList.size() ; i++){
	    if(establishmentList.get(i).equals("others")){
		ConnectionDatabase newConnectionOthers = new ConnectionDatabase();
		ArrayList<String> messagesOthers = new ArrayList<String>();

		String sqlOthers = "SELECT * FROM Workflow.Clean WHERE Clean.establishmentMeans_!=\"native\" && " +
			"Clean.establishmentMeans_!=\"introduced\" && " +
			"Clean.establishmentMeans_!=\"naturalised\" && " +
			"Clean.establishmentMeans_!=\"invasive\" && " +
			"Clean.establishmentMeans_!=\"managed\" && " +
			"Clean.establishmentMeans_!=\"uncertain\";" ;
		messagesOthers.addAll(newConnectionOthers.newConnection("executeQuery", sqlOthers));
		ArrayList<String> othersResults = newConnectionOthers.getResultatSelect();
		if(othersResults.size() > 1){
		    for(int m = 0 ; m < othersResults.size() ; m++){
			if(!noEstablishment.contains(othersResults.get(m))){
			    noEstablishment.add(othersResults.get(m));
			}
		    }

		}

		for(int l = 0; l < messagesOthers.size() ; l++){
		    System.out.println(messagesOthers.get(l));
		}

	    }
	    else{

		ConnectionDatabase newConnectionSelect = new ConnectionDatabase();
		ArrayList<String> messagesSelect = new ArrayList<String>();
		messagesSelect.add("\n--- Select no establishment Means ---\n");
		String sqlSelectNoEstablishment = "SELECT * FROM Workflow.Clean WHERE Clean.establishmentMeans_=\"" + establishmentList.get(i) + "\";";
		messagesSelect.addAll(newConnectionSelect.newConnection("executeQuery", sqlSelectNoEstablishment));

		ArrayList<String> establishmentResults = newConnectionSelect.getResultatSelect();
		if(establishmentResults.size() > 1){
		    for(int m = 0 ; m < establishmentResults.size() ; m++){
			if(!noEstablishment.contains(establishmentResults.get(m))){
			    noEstablishment.add(establishmentResults.get(m));
			}
		    }
		}

		for(int k = 0; k < messagesSelect.size() ; k++){
		    System.out.println(messagesSelect.get(k));
		}

		ConnectionDatabase newConnection = new ConnectionDatabase();
		ArrayList<String> messagesDelete = new ArrayList<String>();
		messagesDelete.add("\n--- establishment Means ---\n");
		String sqlDeleteEstablishment = "DELETE FROM Workflow.Clean WHERE Clean.establishmentMeans_=\"" + establishmentList.get(i) + "\";";
		messagesDelete.addAll(newConnection.newConnection("executeUpdate", sqlDeleteEstablishment));

		for(int j = 0; j < messagesDelete.size() ; j++){
		    System.out.println(messagesDelete.get(j));
		}
	    }
	}

	for(int k = 0; k < noEstablishment.size() ; k++){
	    System.out.println(noEstablishment.get(k));
	}

	File noEstablishmentFile = this.createFileCsv(noEstablishment, "wrong/noEstablishmentMeans_" + this.getNbFileRandom() + ".csv");

	return noEstablishmentFile;

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
    public int getNbFileRandom() {
	return nbFileRandom;
    }

    /**
     * 
     * @param nbFileRandom
     * @return void
     */
    public void setNbFileRandom(int nbFileRandom) {
	this.nbFileRandom = nbFileRandom;
    }

}

