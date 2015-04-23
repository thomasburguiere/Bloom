/**
 * src.model
 * LaunchWorkflow
 * TODO
 */
package src.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import src.beans.Finalisation;
import src.beans.Initialise;

/**
 * src.model
 * 
 * LaunchWorkflow.java
 */
public class LaunchWorkflow {

    private TreatmentData dataTreatment;
    private Initialise initialisation;
    private Finalisation finalisation;
    private String DIRECTORY_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/";
    
    /**
     * 
     * src.model
     * LaunchWorkflow
     */
    public LaunchWorkflow(Initialise initialise){
	this.initialisation = initialise;
    }

    /**
     * Call steps of the workflow
     * 
     * @throws IOException
     * @return void
     */
    public void initialiseLaunchWorkflow() throws IOException{
	this.dataTreatment = new TreatmentData();
	this.dataTreatment.setNbFileRandom(initialisation.getNbFileRandom());
	
	finalisation = new Finalisation();
	
	boolean inputFilesIsValid = this.isValidInputFiles();
	
	if(inputFilesIsValid){
	    
	    this.launchWorkflow();
	}

	if(this.initialisation.isSynonym()){

	    boolean synonymFileIsValid = this.isValidSynonymFile();
	    this.launchSynonymOption(synonymFileIsValid);
	}

	if(this.initialisation.isTdwg4Code()){
	    dataTreatment.checkIsoTdwgCode();
	}

	if(this.initialisation.isRaster()){

	    boolean rasterFilesIsValid = this.isValidRasterFiles();
	    if(rasterFilesIsValid){
		this.launchRasterOption();
	    }
	}

	//System.out.println("establishment : " + this.initialisation.getEstablishmentList());
	//keep introduced data
	if(this.initialisation.isEstablishment()){
	    this.launchEstablishmentMeansOption();
	}
	
	this.writeFinalOutput();
    }
    
    /**
     * Call main steps of the workflow
     * 
     * @throws IOException
     * @return void
     */
    public void launchWorkflow() throws IOException{
	this.dataTreatment.deleteTables();
	
	ArrayList<MappingDwC> listMappingDWC = this.initialisation.getListDwcFiles();
	
	for(int i = 0 ; i < listMappingDWC.size() ; i++){
	    boolean mapping = listMappingDWC.get(i).isMapping();
	    if(mapping){
		this.dataTreatment.mappingDwC(listMappingDWC.get(i));
	    }
	}
	
	for(int i = 0 ; i < this.initialisation.getListDwcFiles().size() ; i++){
	    MappingDwC fileInput = this.initialisation.getListDwcFiles().get(i);
	    int idFile = fileInput.getCounterID();
	    List<String> linesInputFile = null;
	    
	    if(fileInput.isMapping()){
		linesInputFile = this.dataTreatment.initialiseFile(fileInput.getMappedFile(), idFile);
	    }
	    else{
		linesInputFile = this.dataTreatment.initialiseFile(fileInput.getNoMappedFile().getCsvFile(), idFile);
	    }
	   
	    File inputFileModified = this.dataTreatment.createTemporaryFile(linesInputFile, idFile);
	    String sqlInsert = this.dataTreatment.createSQLInsert(inputFileModified, linesInputFile);
	    
	    this.dataTreatment.createTableDarwinCoreInput(sqlInsert);
	}	

	this.dataTreatment.deleteWrongIso2();
	this.dataTreatment.createTableClean();
	
	File wrongCoordinatesFile = dataTreatment.deleteWrongCoordinates();
	finalisation.setWrongCoordinatesFile(wrongCoordinatesFile);
	finalisation.setPathWrongCoordinatesFile(wrongCoordinatesFile.getAbsolutePath().replace(DIRECTORY_PATH, ""));	
	
	File wrongGeospatial = dataTreatment.deleteWrongGeospatial();
	finalisation.setWrongGeospatial(wrongGeospatial);
	finalisation.setPathWrongGeospatial(wrongGeospatial.getAbsolutePath().replace(DIRECTORY_PATH, ""));
	
	File wrongPolygon = this.dataTreatment.getPolygonTreatment();
	finalisation.setWrongPolygon(wrongPolygon);
	finalisation.setPathWrongPolygon(wrongPolygon.getAbsolutePath().replace(DIRECTORY_PATH, ""));
	
    }

    /**
     * Check if data (from input files) are valid 
     * 
     * @return boolean
     */
    public boolean isValidInputFiles(){
	System.out.println("size input : " + this.initialisation.getListDwcFiles().size());
	if(this.initialisation.getListDwcFiles().size() != 0){
	    System.out.println("Your data are valid");
	    return true;
	}
	else{
	    System.out.println("Your data aren't valid");
	    return false;
	}

    }

    /**
     * Check if raster files are valid
     * 
     * @return boolean
     */
    public boolean isValidRasterFiles(){
	//System.out.println("size raster : " + this.initialisation.getInputRastersList().size());
	//System.out.println("size header : " + this.initialisation.getHeaderRasterList().size());
	if(this.initialisation.getInputRastersList().size() == this.initialisation.getHeaderRasterList().size()){
	    if(this.initialisation.getInputRastersList().size() != 0){
		return true;
	    }
	    else{
		System.err.println("You have to put a raster file (format : bil, ...) if you desire to match your point and cells data.");
		return false;
	    }
	}
	else{
	    System.err.println("You have to put a raster file AND its header file (format : hdr).");
	    return false;
	}

    }

    /**
     * Check if synonym file is valid
     * 
     * @return boolean
     */
    public boolean isValidSynonymFile(){
	System.out.println("size synonym : " + this.initialisation.getInputSynonymsList());
	if(this.initialisation.getInputSynonymsList().size() != 0){
	    return true;
	}
	else{
	    return false;
	}
    }

    /**
     * Launch synonym option
     * 
     * @param isValidSynonymFile
     * @return void
     */
    public void launchSynonymOption(boolean isValidSynonymFile){
	if(isValidSynonymFile){
	    this.dataTreatment.includeSynonyms(this.initialisation.getInputSynonymsList().get(0));
	}
	else{
	    this.dataTreatment.includeSynonyms(null);
	}
    }

    /**
     * Launch raster option
     * 
     * @return void
     */
    public void launchRasterOption(){

	File matrixFileValidCells = this.dataTreatment.checkWorldClimCell(this.initialisation.getInputRastersList());
	finalisation.setMatrixFileValidCells(matrixFileValidCells);
	finalisation.setPathMatrixFile(matrixFileValidCells.getAbsolutePath().replace(DIRECTORY_PATH, ""));
	
    }

    /**
     * Launch establishmentMeans option
     * 
     * @return void
     */
    public void launchEstablishmentMeansOption(){
	if(this.initialisation.getEstablishmentList().size() != 0){
	    this.inverseEstablishmentList();
	    File wrongEstablishmentMeans = this.dataTreatment.establishmentMeansOption(this.initialisation.getEstablishmentList());
	    finalisation.setWrongEstablishmentMeans(wrongEstablishmentMeans);
	    finalisation.setPathWrongEstablishmentMeans(wrongEstablishmentMeans.getAbsolutePath().replace(DIRECTORY_PATH, ""));
	}
    }

    /**
     * Retrieve establishmentMeans to delete
     * 
     * @return void
     */
    public void inverseEstablishmentList(){
	ArrayList<String> allEstablishmentMeans = new ArrayList<>();
	allEstablishmentMeans.add("native");
	allEstablishmentMeans.add("introduced");
	allEstablishmentMeans.add("naturalised");
	allEstablishmentMeans.add("invasive");
	allEstablishmentMeans.add("managed");
	allEstablishmentMeans.add("uncertain");
	allEstablishmentMeans.add("others");

	ArrayList<String> inverseEstablishmentList = new ArrayList<>();
	for(int i = 0 ; i < allEstablishmentMeans.size() ; i++){
	    if(!this.initialisation.getEstablishmentList().contains(allEstablishmentMeans.get(i))){
		inverseEstablishmentList.add(allEstablishmentMeans.get(i));
	    }
	}
	//this.initialisation.getEstablishmentList().removeAll(this.initialisation.getEstablishmentList());
	this.initialisation.setEstablishmentList(inverseEstablishmentList);
    }

    public void writeFinalOutput(){
	ArrayList<File> listFinalOutput = new ArrayList<>();
	ArrayList<String> listPathsOutput = new ArrayList<>();
	
	int nbFiles = this.initialisation.getNbFiles();
	for(int i = 0 ; i < nbFiles ; i++){
	    int idFile = this.initialisation.getListDwcFiles().get(i).getCounterID();
	    String originalName = this.initialisation.getListDwcFiles().get(i).getOriginalName();
	    String originalExtension = this.initialisation.getListDwcFiles().get(i).getOriginalExtension();
	    
	    ConnectionDatabase newConnection = new ConnectionDatabase();
	    ArrayList<String > resultCleanTable = newConnection.getCleanTableFromIdFile(idFile);
	    String nameFile = originalName.replace("." + originalExtension, "") + "_clean.csv";
	    File cleanOutput = this.dataTreatment.createFileCsv(resultCleanTable, nameFile);
	    listFinalOutput.add(cleanOutput);
	    String pathFile = cleanOutput.getAbsolutePath().replace(DIRECTORY_PATH,"");
	    listPathsOutput.add(pathFile);
	}
	finalisation.setListPathsOutputFiles(listPathsOutput);
	finalisation.setFinalOutputFiles(listFinalOutput);
    }
    
    /**
     *  
     * @return TreatmentData
     */
    public TreatmentData getDataTreatment() {
	return dataTreatment;
    }

    /**
     * 
     * @param dataTreatment
     * @return void
     */
    public void setDataTreatment(TreatmentData dataTreatment) {
	this.dataTreatment = dataTreatment;
    }

    public Initialise getInitialisation() {
        return initialisation;
    }

    public void setInitialisation(Initialise initialisation) {
        this.initialisation = initialisation;
    }

    public Finalisation getFinalisation() {
        return finalisation;
    }

    public void setFinalisation(Finalisation finalisation) {
        this.finalisation = finalisation;
    }    
}
