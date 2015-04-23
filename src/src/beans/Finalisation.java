/**
 * src.model
 * Finalisation
 */
package src.beans;

import java.io.File;
import java.util.ArrayList;

/**
 * src.model
 * 
 * Finalisation.java
 * Finalisation
 */
public class Finalisation {

    private ArrayList<File> finalOutputFiles = new ArrayList<>();
    private File wrongCoordinatesFile = new File("");
    private File wrongGeospatial = new File("");
    private File wrongPolygon = new File("");
    private File matrixFileValidCells = new File("");
    private File wrongEstablishmentMeans = new File("");
    
    private String pathMatrixFile = "";
    private String pathWrongCoordinatesFile = "";
    private String pathWrongGeospatial = "";
    private String pathWrongPolygon = "";
    private String pathWrongEstablishmentMeans = "";
    private ArrayList<String> listPathsOutputFiles = new ArrayList<>();
    
    private String step1_resultMappingDwC = "";
    private String step2_checkCoordinates = "";
    private String step3_checkGeospatialIssue = "";
    private String step4_checkTaxonomy = "";
    private String step5_checkSynonym = "";
    private String step6_checkTDWG = "";
    private String step7_checkPolygonIso2 = "";
    private String step8_checkRaster = "";
    
    public Finalisation(){
	
    }
    
    public String getStep1_resultMappingDwC() {
        return step1_resultMappingDwC;
    }

    public void setStep1_resultMappingDwC(String step1_resultMappingDwC) {
        this.step1_resultMappingDwC = step1_resultMappingDwC;
    }

    public String getStep2_checkCoordinates() {
        return step2_checkCoordinates;
    }

    public void setStep2_checkCoordinates(String step2_checkCoordinates) {
        this.step2_checkCoordinates = step2_checkCoordinates;
    }

    public String getStep3_checkGeospatialIssue() {
        return step3_checkGeospatialIssue;
    }

    public void setStep3_checkGeospatialIssue(String step3_checkGeospatialIssue) {
        this.step3_checkGeospatialIssue = step3_checkGeospatialIssue;
    }

    public String getStep4_checkTaxonomy() {
        return step4_checkTaxonomy;
    }

    public void setStep4_checkTaxonomy(String step4_checkTaxonomy) {
        this.step4_checkTaxonomy = step4_checkTaxonomy;
    }

    public String getStep5_checkSynonym() {
        return step5_checkSynonym;
    }

    public void setStep5_checkSynonym(String step5_checkSynonym) {
        this.step5_checkSynonym = step5_checkSynonym;
    }

    public String getStep6_checkTDWG() {
        return step6_checkTDWG;
    }

    public void setStep6_checkTDWG(String step6_checkTDWG) {
        this.step6_checkTDWG = step6_checkTDWG;
    }

    public String getStep7_checkPolygonIso2() {
        return step7_checkPolygonIso2;
    }

    public void setStep7_checkPolygonIso2(String step7_checkPolygonIso2) {
        this.step7_checkPolygonIso2 = step7_checkPolygonIso2;
    }

    public String getStep8_checkRaster() {
        return step8_checkRaster;
    }

    public void setStep8_checkRaster(String step8_checkRaster) {
        this.step8_checkRaster = step8_checkRaster;
    }

    public String getPathWrongCoordinatesFile() {
        return pathWrongCoordinatesFile;
    }

    public void setPathWrongCoordinatesFile(String pathWrongCoordinatesFile) {
        this.pathWrongCoordinatesFile = pathWrongCoordinatesFile;
    }

    public String getPathWrongGeospatial() {
        return pathWrongGeospatial;
    }

    public void setPathWrongGeospatial(String pathWrongGeospatial) {
        this.pathWrongGeospatial = pathWrongGeospatial;
    }

    public String getPathWrongPolygon() {
        return pathWrongPolygon;
    }

    public void setPathWrongPolygon(String pathWrongPolygon) {
        this.pathWrongPolygon = pathWrongPolygon;
    }

    public String getPathWrongEstablishmentMeans() {
        return pathWrongEstablishmentMeans;
    }

    public void setPathWrongEstablishmentMeans(String pathWrongEstablishmentMeans) {
        this.pathWrongEstablishmentMeans = pathWrongEstablishmentMeans;
    }

    public ArrayList<String> getListPathsOutputFiles() {
        return listPathsOutputFiles;
    }

    public void setListPathsOutputFiles(ArrayList<String> listPathsOutputFiles) {
        this.listPathsOutputFiles = listPathsOutputFiles;
    }

    public String getPathMatrixFile() {
        return pathMatrixFile;
    }

    public void setPathMatrixFile(String pathMatrixFile) {
        this.pathMatrixFile = pathMatrixFile;
    }

    public ArrayList<File> getFinalOutputFiles() {
        return finalOutputFiles;
    }

    public void setFinalOutputFiles(ArrayList<File> finalOutputFiles) {
        this.finalOutputFiles = finalOutputFiles;
    }

    public File getWrongCoordinatesFile() {
        return wrongCoordinatesFile;
    }

    public void setWrongCoordinatesFile(File wrongCoordinatesFile) {
        this.wrongCoordinatesFile = wrongCoordinatesFile;
    }

    public File getWrongGeospatial() {
        return wrongGeospatial;
    }

    public void setWrongGeospatial(File wrongGeospatial) {
        this.wrongGeospatial = wrongGeospatial;
    }

    public File getWrongPolygon() {
        return wrongPolygon;
    }

    public void setWrongPolygon(File wrongPolygon) {
        this.wrongPolygon = wrongPolygon;
    }

    public File getMatrixFileValidCells() {
        return matrixFileValidCells;
    }

    public void setMatrixFileValidCells(File matrixFileValidCells) {
        this.matrixFileValidCells = matrixFileValidCells;
    }

    public File getWrongEstablishmentMeans() {
        return wrongEstablishmentMeans;
    }

    public void setWrongEstablishmentMeans(File wrongEstablishmentMeans) {
        this.wrongEstablishmentMeans = wrongEstablishmentMeans;
    }
    
}
