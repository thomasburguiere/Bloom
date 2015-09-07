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

    private boolean involved = false;
    private HashMap<Integer, ReconciliationService> infos_reconcile = new HashMap<Integer, ReconciliationService>();
    
    public Step2_ReconciliationService(){
	
    }
    
    public boolean isInvolved() {
        return involved;
    }

    public void setInvolved(boolean involved) {
        this.involved = involved;
    }

	public HashMap<Integer, ReconciliationService> getInfos_reconcile() {
		return infos_reconcile;
	}

	public void setInfos_reconcile(HashMap<Integer, ReconciliationService> infos_reconcile) {
		this.infos_reconcile = infos_reconcile;
	}
    
    
}
