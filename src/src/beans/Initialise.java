/**
 * @author mhachet
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

    private ArrayList<File> inputMappedFilesList = new ArrayList<>();
    private ArrayList<File> inputRastersList = new ArrayList<>();
    private ArrayList<File> headerRasterList = new ArrayList<>();
    private ArrayList<File> inputSynonymsList = new ArrayList<>();
    private ArrayList<String> establishmentList = new ArrayList<>();
    private ArrayList<MappingDwC> listDwcFiles = new ArrayList<>();
    
    
    private boolean synonym;
    private boolean tdwg4Code;
    private boolean raster;
    private boolean establishment;    
    private int nbFileRandom;
    
    /**
     * 
     * src.beans
     * Initialise
     */
    public Initialise(){
	
    }
    
    /**
     * 
     * @return ArrayList<MappingDwC>
     */
    public ArrayList<MappingDwC> getListDwcFiles() {
        return listDwcFiles;
    }

    /**
     * 
     * @param listDwcFiles
     * @return void
     */
    public void setListDwcFiles(ArrayList<MappingDwC> listDwcFiles) {
        this.listDwcFiles = listDwcFiles;
    }

    /**
     * 
     * @return ArrayList<File>
     */
    public ArrayList<File> getInputMappedFilesList() {
        return inputMappedFilesList;
    }

    /**
     * 
     * @param inputMappedFilesList
     * @return void
     */
    public void setInputMappedFilesList(ArrayList<File> inputMappedFilesList) {
        this.inputMappedFilesList = inputMappedFilesList;
    }

    /**
     * 
     * @return ArrayList<File>
     */
    public ArrayList<File> getInputRastersList() {
        return inputRastersList;
    }

    /**
     * 
     * @param inputRastersList
     * @return void
     */
    public void setInputRastersList(ArrayList<File> inputRastersList) {
        this.inputRastersList = inputRastersList;
    }
    
    /**
     * 
     * @return ArrayList<File>
     */
    public ArrayList<File> getHeaderRasterList() {
        return headerRasterList;
    }

    /**
     * 
     * @param headerRasterList
     * @return void
     */
    public void setHeaderRasterList(ArrayList<File> headerRasterList) {
        this.headerRasterList = headerRasterList;
    }

    /**
     * 
     * @return ArrayList<File>
     */
    public ArrayList<File> getInputSynonymsList() {
        return inputSynonymsList;
    }

    /**
     * 
     * @param inputSynonymsList
     * @return void
     */
    public void setInputSynonymsList(ArrayList<File> inputSynonymsList) {
        this.inputSynonymsList = inputSynonymsList;
    }

    /**
     * 
     * @return boolean
     */
    public boolean isSynonym() {
        return synonym;
    }

    /**
     * 
     * @param synonym
     * @return void
     */
    public void setSynonym(boolean synonym) {
        this.synonym = synonym;
    }

    /**
     * 
     * @return boolean
     */
    public boolean isTdwg4Code() {
        return tdwg4Code;
    }

    /**
     * 
     * @param tdwg4Code
     * @return void
     */
    public void setTdwg4Code(boolean tdwg4Code) {
        this.tdwg4Code = tdwg4Code;
    }

    /**
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getEstablishmentList() {
        return establishmentList;
    }

    /**
     * 
     * @param establishmentList
     * @return void
     */
    public void setEstablishmentList(ArrayList<String> establishmentList) {
        this.establishmentList = establishmentList;
    }

    /**
     * 
     * @return boolean
     */
    public boolean isEstablishment() {
        return establishment;
    }

    /**
     * 
     * @param establishment
     * @return void
     */
    public void setEstablishment(boolean establishment) {
        this.establishment = establishment;
    }    

    /**
     * 
     * @return boolean
     */
    public boolean isRaster() {
        return raster;
    }

    /**
     * 
     * @param raster
     * @return void
     */
    public void setRaster(boolean raster) {
        this.raster = raster;
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
    
    public int getNbFiles(){
	int nbFiles = this.getListDwcFiles().size();
	
	return nbFiles;
    }
}
