/**
 * src.beans
 * Step2_ReconciliationService
 */
package src.beans;

import java.util.ArrayList;
import java.util.HashMap;

import src.model.MappingDwC;
import src.model.ReconciliationService;

/**
 * src.beans
 * 
 * Step2_ReconciliationService.java
 * Step2_ReconciliationService
 */
public class Step2_ReconciliationService {

    private boolean step2_ok = true;
    private boolean involved = false;
    private HashMap<ReconciliationService, String> reconciledFilesAssociatedPath = new HashMap<ReconciliationService, String>();
    
    public Step2_ReconciliationService(){
	
    }

    public boolean isStep2_ok() {
        return step2_ok;
    }

    public void setStep2_ok(boolean step2_ok) {
        this.step2_ok = step2_ok;
    }

    public boolean isInvolved() {
        return involved;
    }

    public void setInvolved(boolean involved) {
        this.involved = involved;
    }

    public HashMap<ReconciliationService, String> getReconciledFilesAssociatedPath() {
        return reconciledFilesAssociatedPath;
    }

    public void setReconciledFilesAssociatedPath(
    	HashMap<ReconciliationService,  String> reconciledFilesAssociatedPath) {
        this.reconciledFilesAssociatedPath = reconciledFilesAssociatedPath;
    }
    
    
}
