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
import java.util.Random;

import org.apache.commons.io.FilenameUtils;
import org.junit.*;
import static net.sourceforge.jwebunit.junit.JWebUnit.*;
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
import src.model.MappingDwC;
import src.servlets.*;

public class ExampleWebTestCase {

    private String DIRECTORY_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/src/ressources/"; 
    private Initialise initialisation;
    private int nbFileRandom;
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
	
	this.setNbFileRandom(this.generateRandomKey());
	this.prepareInputFiles();
	this.prepareInputsRaster();
	this.prepareInputsHeader();
	
	initialisation.setSynonym(true);
	initialisation.setTdwg4Code(true);
	initialisation.setEstablishment(true);
	
	setBaseUrl("http://localhost:8080/WebWorkflowCleanData/HomePage.jsp");
	
	
    }

    public void prepareInputFiles(){
	
	if(!new File(DIRECTORY_PATH + "test/").exists()){
	    new File(DIRECTORY_PATH + "test/").mkdirs();
	}
	
	if(!new File(DIRECTORY_PATH + "test/temp/").exists()){
	    new File(DIRECTORY_PATH + "test/temp/").mkdirs();
	}

	ArrayList<MappingDwC> listDwcFiles = new ArrayList<>();
	inputFilesList.add(new File(DIRECTORY_PATH + "test/data/occurrence_mini.csv"));
	inputFilesList.add(new File(DIRECTORY_PATH + "test/data/occurrence_mini2.csv"));
	inputFilesList.add(new File(DIRECTORY_PATH + "test/data/test_ipt.csv"));

	for(int i = 0 ; i < inputFilesList.size() ; i++){
	    File file = new File(DIRECTORY_PATH + "test/temp/noMappedDWC_" + this.getNbFileRandom() + "_" + (i + 1 ) + ".csv");
	    FileWriter writer = null;
	    try {
		writer = new FileWriter(file.getAbsoluteFile());

		BufferedWriter output = new BufferedWriter(writer);
		ArrayList<String> linesFile = this.getLinesFile(inputFilesList.get(i));
		System.out.println(linesFile.size());
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
	    newMappingDWC.initialiseMapping();
	    HashMap<String, String> connectionTags = new HashMap<>();
	    ArrayList<String> tagsNoMapped = newMappingDWC.getTagsListNoMapped();
	    for(int k = 0 ; k < tagsNoMapped.size() ; k++){
		connectionTags.put(tagsNoMapped.get(k) + "_" + k, "");
	    }
	    newMappingDWC.setConnectionTags(connectionTags);
	    newMappingDWC.getNoMappedFile().setCsvName(file.getName());
	    //initialisation.getInputFilesList().add(csvFile.getCsvFile());
	}

    }

    public void prepareInputsRaster(){
	
	initialisation.setRaster(true);
	
	inputsRasterList.add(new File(DIRECTORY_PATH + "test/data/alt_22.bil"));
	inputsRasterList.add(new File(DIRECTORY_PATH + "test/data/prec1_46.bil"));
	inputsRasterList.add(new File(DIRECTORY_PATH + "test/data/tmean1.bil"));
	
	this.initialisation.setInputRastersList(inputsRasterList);
    }
    
    public void prepareInputsHeader(){
	inputHeaderList.add(new File(DIRECTORY_PATH + "test/data/alt_22.hdr"));
	inputHeaderList.add(new File(DIRECTORY_PATH + "test/data/prec1_46.hdr"));
	inputHeaderList.add(new File(DIRECTORY_PATH + "test/data/tmean1.hdr"));
	
	this.initialisation.setHeaderRasterList(inputHeaderList);
	
    }
    @Test
    public void testLogin() {

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
    private int generateRandomKey() {
	Random random = new Random();
	nbFileRandom = random.nextInt();
	return nbFileRandom;
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

    public int getNbFileRandom() {
	return nbFileRandom;
    }

    public void setNbFileRandom(int nbFileRandom) {
	this.nbFileRandom = nbFileRandom;
    }

    public Finalisation getFinalisation() {
	return finalisation;
    }

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