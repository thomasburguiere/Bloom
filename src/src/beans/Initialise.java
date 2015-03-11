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


public class Initialise implements Serializable{

    private ArrayList<File> inputFilesList;

    public Initialise(){
	
    }
 
    public ArrayList<File> getInputFile() {
        return inputFilesList;
    }

    public void setInputFile(ArrayList<File> inputFilesList) {
        this.inputFilesList = inputFilesList;
    }

   
    
    
}
