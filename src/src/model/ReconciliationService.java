/**
 * src.model
 * ReconciliationService
 */
package src.model;

import java.io.File;
import java.util.HashMap;

/**
 * src.model
 * 
 * ReconciliationService.java
 * ReconciliationService
 */
public class ReconciliationService {

    private File mappedFile;
    private boolean reconcile;
    private String reconcileTagBased;
    private HashMap<Integer, String> linesConnectedNewName;
    private int counterID;
    
    public ReconciliationService(int idFile){
	this.counterID = idFile;

    }

    public File getMappedFile() {
        return mappedFile;
    }

    public void setMappedFile(File mappedFile) {
        this.mappedFile = mappedFile;
    }

    public boolean isReconcile() {
        return reconcile;
    }

    public void setReconcile(boolean reconcile) {
        this.reconcile = reconcile;
    }

    public String getReconcileTagBased() {
        return reconcileTagBased;
    }

    public void setReconcileTagBased(String reconcileTagBased) {
        this.reconcileTagBased = reconcileTagBased;
    }

    public HashMap<Integer, String> getLineConnectedNewName() {
        return linesConnectedNewName;
    }

    public void setLineConnectedNewName(
    	HashMap<Integer, String> lineConnectedNewName) {
        this.linesConnectedNewName = lineConnectedNewName;
    }

    public HashMap<Integer, String> getLinesConnectedNewName() {
        return linesConnectedNewName;
    }

    public void setLinesConnectedNewName(
    	HashMap<Integer, String> linesConnectedNewName) {
        this.linesConnectedNewName = linesConnectedNewName;
    }

    public int getCounterID() {
        return counterID;
    }

    public void setCounterID(int counterID) {
        this.counterID = counterID;
    }
    
}
