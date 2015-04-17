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

import src.beans.Initialise;

/**
 * src.model
 * 
 * LaunchWorkflow.java
 * LaunchWorkflow
 */
public class LaunchWorkflow {

    private TreatmentData dataTreatment;
    private Initialise initialisation;

    public LaunchWorkflow(Initialise initialise){
	this.initialisation = initialise;
    }

    public void initialiseLaunchWorkflow() throws IOException{
	this.dataTreatment = new TreatmentData();
	this.dataTreatment.generateRandomKey();

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

	System.out.println("establishment : " + this.initialisation.getEstablishmentList());
	//keep introduced data
	if(this.initialisation.isEstablishment()){
	    this.launchEstablishmentMeansOption();
	}

	/*if(){
	    System.out.println("ipt : " + this.initialisation.isMappingDwC());
	}*/
    }

    public void launchWorkflow() throws IOException{
	this.dataTreatment.deleteTables();
	for(int i = 0 ; i < this.initialisation.getInputFilesList().size() ; i++){
	    int idFile = i + 1;

	    List<String> linesInputFile = this.dataTreatment.initialiseFile(this.initialisation.getInputFilesList().get(i), idFile);
	    File inputFileModified = this.dataTreatment.createTemporaryFile(linesInputFile, idFile);
	    String sqlInsert = this.dataTreatment.createSQLInsert(inputFileModified, linesInputFile);
	    this.dataTreatment.createTableDarwinCoreInput(sqlInsert);
	}	

	this.dataTreatment.deleteWrongIso2();
	this.dataTreatment.createTableClean();
	File wrongCoordinatesFile = dataTreatment.deleteWrongCoordinates();
	File wrongGeospatial = dataTreatment.deleteWrongGeospatial();

	File wrongPolygon = this.dataTreatment.getPolygonTreatment();

    }

    public boolean isValidInputFiles(){
	System.out.println("size input : " + this.initialisation.getInputFilesList().size());
	if(this.initialisation.getInputFilesList().size() != 0){
	    System.out.println("Your data are valid");
	    return true;
	}
	else{
	    System.out.println("Your data aren't valid");
	    return false;
	}

    }

    public boolean isValidRasterFiles(){
	System.out.println("size raster : " + this.initialisation.getInputRastersList().size());
	System.out.println("size header : " + this.initialisation.getHeaderRasterList().size());
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

    public boolean isValidSynonymFile(){
	System.out.println("size synonym : " + this.initialisation.getInputSynonymsList());
	if(this.initialisation.getInputSynonymsList().size() != 0){
	    return true;
	}
	else{
	    return false;
	}
    }

    public void launchSynonymOption(boolean isValidSynonymFile){
	if(isValidSynonymFile){
	    this.dataTreatment.includeSynonyms(this.initialisation.getInputSynonymsList().get(0));
	}
	else{
	    this.dataTreatment.includeSynonyms(null);
	}
    }

    public void launchRasterOption(){

	File matrixFileValidCells = this.dataTreatment.checkWorldClimCell(this.initialisation.getInputRastersList());

    }

    public void launchEstablishmentMeansOption(){
	if(this.initialisation.getEstablishmentList().size() != 0){
	    this.inverseEstablishmentList();
	    this.dataTreatment.establishmentMeansOption(this.initialisation.getEstablishmentList());
	}
    }

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

    public TreatmentData getDataTreatment() {
	return dataTreatment;
    }

    public void setDataTreatment(TreatmentData dataTreatment) {
	this.dataTreatment = dataTreatment;
    }

}
