/**
 * src.beans
 * Initiliaze
 * TODO
 */
package src.beans;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * src.beans
 * 
 * Initiliaze
 */


public class Initialise {

    private ArrayList<File> inputFilesList = new ArrayList<>();
    private ArrayList<File> inputRastersList = new ArrayList<>();
    private ArrayList<File> inputSynonymsList = new ArrayList<>();
    
    private boolean synonym;
    private boolean tdwg4Code;
    private boolean rasterCell;
    

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

    public boolean isRasterCell() {
        return rasterCell;
    }

    public void setRasterCell(boolean rasterCell) {
        this.rasterCell = rasterCell;
    }

    
}
