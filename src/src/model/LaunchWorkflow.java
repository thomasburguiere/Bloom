/**
 * src.model
 * LaunchWorkflow
 * TODO
 */
package src.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import src.beans.Finalisation;
import src.beans.Initialise;
import src.beans.Step1_MappingDwc;
import src.beans.Step2_CheckCoordinates;
import src.beans.Step3_CheckGeoIssue;
import src.beans.Step4_CheckTaxonomy;
import src.beans.Step5_IncludeSynonym;
import src.beans.Step6_CheckTDWG;
import src.beans.Step7_CheckISo2Coordinates;
import src.beans.Step8_CheckCoordinatesRaster;

/**
 * src.model
 * 
 * LaunchWorkflow.java
 */
public class LaunchWorkflow {

    private TreatmentData dataTreatment;
    private Initialise initialisation;
    private Finalisation finalisation;
    //private String DIRECTORY_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/";
    
    private Step1_MappingDwc step1;
    private Step2_CheckCoordinates step2;
    private Step3_CheckGeoIssue step3;
    private Step4_CheckTaxonomy step4;
    private Step5_IncludeSynonym step5;
    private Step6_CheckTDWG step6;
    private Step7_CheckISo2Coordinates step7;
    private Step8_CheckCoordinatesRaster step8;
    
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
	this.dataTreatment.setNbSessionRandom(initialisation.getNbSessionRandom());
	this.dataTreatment.setDIRECTORY_PATH(initialisation.getDIRECTORY_PATH());
	this.dataTreatment.setRESSOURCES_PATH(initialisation.getRESSOURCES_PATH());
	
	finalisation = new Finalisation();
	step1 = new Step1_MappingDwc();
	step2 = new Step2_CheckCoordinates();
	step3 = new Step3_CheckGeoIssue();
	step4 = new Step4_CheckTaxonomy();
	step5 = new Step5_IncludeSynonym();
	step6 = new Step6_CheckTDWG();
	step7 = new Step7_CheckISo2Coordinates();
	step8 = new Step8_CheckCoordinatesRaster();
	
	boolean inputFilesIsValid = this.isValidInputFiles();
	
	if(inputFilesIsValid){
	    
	    this.launchWorkflow();
	}

	if(this.initialisation.isSynonym()){

	    boolean synonymFileIsValid = this.isValidSynonymFile();
	    this.launchSynonymOption(synonymFileIsValid);
	    step5.setInvolved(true);
	}

	if(this.initialisation.isTdwg4Code()){
	    dataTreatment.checkIsoTdwgCode();
	    step6.setInvolved(true);
	}

	if(this.initialisation.isRaster()){
	    step8.setInvolved(true);
	    boolean rasterFilesIsValid = this.isValidRasterFiles();
	    if(rasterFilesIsValid){
		this.launchRasterOption();	
		step8.setStep8_ok(true);
	    }
	    else{
		step8.setStep8_ok(false);
	    }
	}

	//System.out.println("establishment : " + this.initialisation.getEstablishmentList());
	//keep introduced data
	if(this.initialisation.isEstablishment()){
	    this.launchEstablishmentMeansOption();
	}
	
	this.writeFinalOutput();
	
	this.dataTreatment.deleteTables();
    }
    
    /**
     * Call main steps of the workflow
     * 
     * @throws IOException
     * @return void
     */
    public void launchWorkflow() throws IOException{
	
	ArrayList<MappingDwC> listMappingDWC = this.initialisation.getListDwcFiles();
	HashMap<MappingDwC, String> mappingPath = step1.getMappedFilesAssociatedPath();
	
	for(int i = 0 ; i < listMappingDWC.size() ; i++){
	    MappingDwC mappingDwc = listMappingDWC.get(i);
	    boolean mapping = mappingDwc.isMapping();
	    if(mapping){
		step1.setInvolved(mapping);
		
		this.dataTreatment.mappingDwC(mappingDwc);
		String pathMappedFile = mappingDwc.getMappedFile().getAbsolutePath().replace(initialisation.getDIRECTORY_PATH(),"");
		mappingPath.put(mappingDwc, pathMappedFile);
		
		
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
	finalisation.setPathWrongCoordinatesFile(wrongCoordinatesFile.getAbsolutePath().replace(initialisation.getDIRECTORY_PATH(), ""));	
	step2.setNbFound(this.dataTreatment.getNbWrongCoordinates());
	
	File wrongGeospatial = dataTreatment.deleteWrongGeospatial();
	finalisation.setWrongGeospatial(wrongGeospatial);
	finalisation.setPathWrongGeospatial(wrongGeospatial.getAbsolutePath().replace(initialisation.getDIRECTORY_PATH(), ""));
	step3.setNbFound(this.dataTreatment.getNbSynonymInvolved());
	
	File wrongPolygon = this.dataTreatment.getPolygonTreatment();
	finalisation.setWrongPolygon(wrongPolygon);
	finalisation.setPathWrongPolygon(wrongPolygon.getAbsolutePath().replace(initialisation.getDIRECTORY_PATH(), ""));
	step7.setNbFound(this.dataTreatment.getNbWrongIso2());
	
    }

    /**
     * Check if data (from input files) are valid 
     * 
     * @return boolean
     */
    public boolean isValidInputFiles(){

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
	boolean isValid = true;
	if(this.initialisation.getInputRastersList().size() == this.initialisation.getHeaderRasterList().size()){
	    if(this.initialisation.getInputRastersList().size() == 0){
		System.err.println("You have to put a raster file (format : bil, ...) if you desire to match your point and cells data.");
		isValid = false;
	    }
	}
	else{
	    System.err.println("You have to put a raster file AND its header file (format : hdr).");
	    isValid = false;
	}
	
	for(int i = 0 ; i < this.initialisation.getInputRastersList().size() ; i++){
	    File raster = this.initialisation.getInputRastersList().get(i);
	    String extensionRaster = raster.getName().substring(raster.getName().lastIndexOf("."));
	    String [] extensionsRaster = {".bil", ".grd", ".asc", ".sdat", ".rsc", ".nc", ".cdf", ".bsq", ".bip"};
	    ArrayList<String> extensionsRasterList = new ArrayList(Arrays.asList(extensionsRaster));
	    
	    if(!extensionsRasterList.contains(extensionRaster)){
		isValid = false;
	    }
	    
	}
	for(int i = 0 ; i < this.initialisation.getHeaderRasterList().size() ; i++){
	    File header = this.initialisation.getHeaderRasterList().get(i);
	    String extensionHeader = header.getName().substring(header.getName().lastIndexOf("."));
	    if(!extensionHeader.equals(".hdr")){
		isValid = false;
	    }
	    
	    
	}
	

	return isValid;
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
	
	step5.setNbFound(this.dataTreatment.getNbSynonymInvolved());
    }

    /**
     * Launch raster option
     * 
     * @return void
     */
    public void launchRasterOption(){

	File matrixFileValidCells = this.dataTreatment.checkWorldClimCell(this.initialisation.getInputRastersList());
	finalisation.setMatrixFileValidCells(matrixFileValidCells);
	finalisation.setPathMatrixFile(matrixFileValidCells.getAbsolutePath().replace(initialisation.getDIRECTORY_PATH(), ""));
	step8.setNbFound(this.dataTreatment.getNbWrongRaster());
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
	    finalisation.setPathWrongEstablishmentMeans(wrongEstablishmentMeans.getAbsolutePath().replace(initialisation.getDIRECTORY_PATH(), ""));
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
	
	if(!new File(initialisation.getDIRECTORY_PATH() + "temp/final_results/").exists()){
	    new File(initialisation.getDIRECTORY_PATH() + "temp/final_results/").mkdir();
	}
	
	int nbFiles = this.initialisation.getNbFiles();
	for(int i = 0 ; i < nbFiles ; i++){
	    int idFile = this.initialisation.getListDwcFiles().get(i).getCounterID();
	    String originalName = this.initialisation.getListDwcFiles().get(i).getOriginalName();
	    String originalExtension = this.initialisation.getListDwcFiles().get(i).getOriginalExtension();
	    
	    ConnectionDatabase newConnection = new ConnectionDatabase();
	    ArrayList<String > resultCleanTable = newConnection.getCleanTableFromIdFile(idFile, initialisation.getNbSessionRandom());
	    String nameFile = originalName.replace("." + originalExtension, "") + "_" + initialisation.getNbSessionRandom() + "_clean.csv";
	    File cleanOutput = this.dataTreatment.createFileCsv(resultCleanTable, nameFile);

	    listFinalOutput.add(cleanOutput);
	    String pathFile = cleanOutput.getAbsolutePath().replace(initialisation.getDIRECTORY_PATH(),"");
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

    /*
    public String getDIRECTORY_PATH() {
        return DIRECTORY_PATH;
    }

    public void setDIRECTORY_PATH(String dIRECTORY_PATH) {
        DIRECTORY_PATH = dIRECTORY_PATH;
    }
     */
    public Step1_MappingDwc getStep1() {
        return step1;
    }

    public void setStep1(Step1_MappingDwc step1) {
        this.step1 = step1;
    }

    public Step2_CheckCoordinates getStep2() {
        return step2;
    }

    public void setStep2(Step2_CheckCoordinates step2) {
        this.step2 = step2;
    }

    public Step3_CheckGeoIssue getStep3() {
        return step3;
    }

    public void setStep3(Step3_CheckGeoIssue step3) {
        this.step3 = step3;
    }

    public Step4_CheckTaxonomy getStep4() {
        return step4;
    }

    public void setStep4(Step4_CheckTaxonomy step4) {
        this.step4 = step4;
    }

    public Step5_IncludeSynonym getStep5() {
        return step5;
    }

    public void setStep5(Step5_IncludeSynonym step5) {
        this.step5 = step5;
    }

    public Step6_CheckTDWG getStep6() {
        return step6;
    }

    public void setStep6(Step6_CheckTDWG step6) {
        this.step6 = step6;
    }

    public Step7_CheckISo2Coordinates getStep7() {
        return step7;
    }

    public void setStep7(Step7_CheckISo2Coordinates step7) {
        this.step7 = step7;
    }

    public Step8_CheckCoordinatesRaster getStep8() {
        return step8;
    }

    public void setStep8(Step8_CheckCoordinatesRaster step8) {
        this.step8 = step8;
    }  
    
}
