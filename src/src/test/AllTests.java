/**
 * src.test
 * ExampleWebTestCase
 */
package src.test;

/**
 * src.test
 * 
 * ExampleWebTestCase.java
 * ExampleWebTestCase
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
import src.model.CSVFile;
import src.model.LaunchWorkflow;
import src.model.MappingDwC;

public class AllTests{

    private String DIRECTORY_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/src/ressources/test/";
    private String RESSOURCES_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/src/ressources/";

    private Initialise initialisation;
    private String nbSessionRandom;
    private ArrayList<File> inputFilesList;
    private ArrayList<File> inputsRasterList;
    private ArrayList<File> inputHeaderList;

    private Finalisation finalisation;
    private Step1_MappingDwc step1;
    private Step2_CheckCoordinates step2;
    private Step3_CheckGeoIssue step3;
    private Step4_CheckTaxonomy step4;
    private Step5_IncludeSynonym step5;
    private Step6_CheckTDWG step6;
    private Step7_CheckISo2Coordinates step7;
    private Step8_CheckCoordinatesRaster step8;

    @Before
    public void prepare() {


	initialisation = new Initialise();
	inputFilesList = new ArrayList<>();
	inputHeaderList = new ArrayList<>();
	inputsRasterList = new ArrayList<>();

	initialisation.setDIRECTORY_PATH(DIRECTORY_PATH);
	initialisation.setRESSOURCES_PATH(RESSOURCES_PATH);

	this.setNbSessionRandom(this.generateRandomKey());
	this.prepareInputFiles();

	initialisation.setRaster(true);
	this.prepareInputsRaster();
	this.prepareInputsHeader();

	initialisation.setEstablishment(true);
	this.prepareEstablishment();

	initialisation.setSynonym(true);
	initialisation.setTdwg4Code(true);

	//this.test();

	//setBaseUrl("http://localhost:8080/WebWorkflowCleanData/HomePage.jsp");	
    }

    @Test
    public void test(){

	LaunchWorkflow newLaunch = new LaunchWorkflow(this.getInitialisation());
	System.out.println(this.getInitialisation());
	try {
	    newLaunch.initialiseLaunchWorkflow();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
    
    @After
    public void compareAfterTest(){
	
    }

    public void prepareInputFiles(){

	if(!new File(DIRECTORY_PATH + "temp/").exists()){
	    new File(DIRECTORY_PATH + "temp/").mkdirs();
	}

	ArrayList<MappingDwC> listDwcFiles = new ArrayList<>();
	inputFilesList.add(new File(DIRECTORY_PATH + "inputs_data/occurrence_mini.csv"));
	inputFilesList.add(new File(DIRECTORY_PATH + "inputs_data/occurrence_mini2.csv"));
	inputFilesList.add(new File(DIRECTORY_PATH + "inputs_data/test_ipt.csv"));

	for(int i = 0 ; i < inputFilesList.size() ; i++){
	    File file = new File(DIRECTORY_PATH + "temp/noMappedDWC_" + this.getNbSessionRandom() + "_" + (i + 1 ) + ".csv");
	    FileWriter writer = null;
	    try {
		writer = new FileWriter(file.getAbsoluteFile());

		BufferedWriter output = new BufferedWriter(writer);
		ArrayList<String> linesFile = this.getLinesFile(inputFilesList.get(i));

		for(int j = 0 ; j < linesFile.size() ; j++){
		    output.write(linesFile.get(j) + "\n");
		    output.flush();

		}
		writer.close();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    CSVFile csvFile = new CSVFile(file);
	    MappingDwC newMappingDWC = new MappingDwC(csvFile, false);
	    newMappingDWC.setCounterID(i);
	    newMappingDWC.setOriginalName(inputFilesList.get(i).getName());
	    String nameFile = inputFilesList.get(i).getName();
	    newMappingDWC.setOriginalExtension(nameFile.substring(nameFile.indexOf('.')+1,nameFile.length()));
	    listDwcFiles.add(newMappingDWC);
	    newMappingDWC.initialiseMapping(this.getNbSessionRandom());
	    HashMap<String, String> connectionTags = new HashMap<>();
	    ArrayList<String> tagsNoMapped = newMappingDWC.getTagsListNoMapped();
	    for(int k = 0 ; k < tagsNoMapped.size() ; k++){
		connectionTags.put(tagsNoMapped.get(k) + "_" + k, "");
	    }
	    System.out.println(connectionTags);
	    newMappingDWC.setConnectionTags(connectionTags);
	    newMappingDWC.getNoMappedFile().setCsvName(file.getName());
	    //initialisation.getInputFilesList().add(csvFile.getCsvFile());

	    boolean doMapping = newMappingDWC.doMapping();
	    newMappingDWC.setMapping(doMapping);
	    if(doMapping){
		ArrayList<String> presentTags = newMappingDWC.getPresentTags();
		for(Entry<String, String> entry : connectionTags.entrySet()) {
		    String [] tableKey = entry.getKey().split("_");
		    String idKey = tableKey[tableKey.length-1];
		    if(presentTags.contains(idKey)){
			connectionTags.put(entry.getKey(), idKey);
		    }else{
			//connectionTags.put(entry.getKey());
		    }
		}
	    }

	}

	this.initialisation.setListDwcFiles(listDwcFiles);
    }

    public void prepareInputsRaster(){

	initialisation.setRaster(true);

	inputsRasterList.add(new File(DIRECTORY_PATH + "inputs_data/alt_22.bil"));
	inputsRasterList.add(new File(DIRECTORY_PATH + "inputs_data/prec1_46.bil"));
	inputsRasterList.add(new File(DIRECTORY_PATH + "inputs_data/tmean1.bil"));

	this.initialisation.setInputRastersList(inputsRasterList);
    }

    public void prepareInputsHeader(){
	inputHeaderList.add(new File(DIRECTORY_PATH + "inputs_data/alt_22.hdr"));
	inputHeaderList.add(new File(DIRECTORY_PATH + "inputs_data/prec1_46.hdr"));
	inputHeaderList.add(new File(DIRECTORY_PATH + "inputs_data/tmean1.hdr"));

	this.initialisation.setHeaderRasterList(inputHeaderList);

    }

    public void prepareEstablishment(){
	ArrayList<String> listEstablishment = new ArrayList<>();
	listEstablishment.add("introduced");
	listEstablishment.add("invasive");
	listEstablishment.add("native");
	listEstablishment.add("managed");

	this.initialisation.setEstablishmentList(listEstablishment);
    }

    public ArrayList<String> getLinesFile(File file){
	ArrayList<String> linesFile = new ArrayList<>();
	try{
	    InputStream ips = new FileInputStream(file.getAbsoluteFile()); 
	    InputStreamReader ipsr = new InputStreamReader(ips);
	    BufferedReader br = new BufferedReader(ipsr);
	    String line;
	    while ((line = br.readLine()) != null){
		linesFile.add(line);
	    }
	    br.close(); 
	}		
	catch (Exception e){
	    System.out.println(e.toString());
	}

	return linesFile;

    }

    /**
     * @return int
     */
    private String generateRandomKey() {
	String nbSessionRandom = UUID.randomUUID().toString().replace("-","_");
	return nbSessionRandom;
    }

    public String getDIRECTORY_PATH() {
	return DIRECTORY_PATH;
    }

    public void setDIRECTORY_PATH(String dIRECTORY_PATH) {
	DIRECTORY_PATH = dIRECTORY_PATH;
    }

    public Initialise getInitialisation() {
	return initialisation;
    }

    public void setInitialisation(Initialise initialisation) {
	this.initialisation = initialisation;
    }

    public String getNbSessionRandom() {
	return nbSessionRandom;
    }

    public void setNbSessionRandom(String nbSessionRandom) {
	this.nbSessionRandom = nbSessionRandom;
    }

    public Finalisation getFinalisation() {
	return finalisation;
    }
    //_clean
    public void setFinalisation(Finalisation finalisation) {
	this.finalisation = finalisation;
    }

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