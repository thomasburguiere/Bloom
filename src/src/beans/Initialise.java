/**
 * src.beans
 * Initiliaze
 * TODO
 */
package src.beans;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import src.model.MappingDwC;

/**
 * src.beans
 * 
 * Initiliaze
 */


public class Initialise {

    private ArrayList<File> inputFilesList = new ArrayList<>();
    private ArrayList<File> inputMappedFilesList = new ArrayList<>();
    private ArrayList<File> inputRastersList = new ArrayList<>();
    private ArrayList<File> headerRasterList = new ArrayList<>();
    private ArrayList<File> inputSynonymsList = new ArrayList<>();
    private ArrayList<String> establishmentList = new ArrayList<>();
    private List<MappingDwC> listDwcFiles = new ArrayList<>();
    
    private boolean synonym;
    private boolean tdwg4Code;
    private boolean raster;
    private boolean establishment;    
    
    public Initialise(){
	
    }
    
    public List<MappingDwC> getListDwcFiles() {
        return listDwcFiles;
    }

    public void setListDwcFiles(List<MappingDwC> listDwcFiles) {
        this.listDwcFiles = listDwcFiles;
    }

    public ArrayList<File> getInputMappedFilesList() {
        return inputMappedFilesList;
    }

    public void setInputMappedFilesList(ArrayList<File> inputMappedFilesList) {
        this.inputMappedFilesList = inputMappedFilesList;
    }

    public ArrayList<File> getInputFilesList() {
        return inputFilesList;
    }

    public void setInputFilesList(ArrayList<File> inputFilesList) {
        this.inputFilesList = inputFilesList;
    }
    
    public ArrayList<File> getInputRastersList() {
        return inputRastersList;
    }

    public void setInputRastersList(ArrayList<File> inputRastersList) {
        this.inputRastersList = inputRastersList;
    }
    
    public ArrayList<File> getHeaderRasterList() {
        return headerRasterList;
    }

    public void setHeaderRasterList(ArrayList<File> headerRasterList) {
        this.headerRasterList = headerRasterList;
    }

    public ArrayList<File> getInputSynonymsList() {
        return inputSynonymsList;
    }

    public void setInputSynonymsList(ArrayList<File> inputSynonymsList) {
        this.inputSynonymsList = inputSynonymsList;
    }

    public boolean isSynonym() {
        return synonym;
    }

    public void setSynonym(boolean synonym) {
        this.synonym = synonym;
    }

    public boolean isTdwg4Code() {
        return tdwg4Code;
    }

    public void setTdwg4Code(boolean tdwg4Code) {
        this.tdwg4Code = tdwg4Code;
    }

    public ArrayList<String> getEstablishmentList() {
        return establishmentList;
    }

    public void setEstablishmentList(ArrayList<String> establishmentList) {
        this.establishmentList = establishmentList;
    }

    public boolean isEstablishment() {
        return establishment;
    }

    public void setEstablishment(boolean establishment) {
        this.establishment = establishment;
    }    

    public boolean isRaster() {
        return raster;
    }

    public void setRaster(boolean raster) {
        this.raster = raster;
    }   

}
