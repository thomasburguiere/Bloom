/**
 * src.beans
 * Step2_ReconciliationService
 */
package src.beans;

/**
 * src.beans
 * 
 * Step2_ReconciliationService.java
 * Step2_ReconciliationService
 */
public class Step2_ReconciliationService {

    private boolean step2_ok = true;
    private boolean involved = false;
    
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
    
    
}
