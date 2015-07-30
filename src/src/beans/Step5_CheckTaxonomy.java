/**
 * src.beans
 * Step5_CheckTaxonomy
 */
package src.beans;

/**
 * src.beans
 * 
 * Step5_CheckTaxonomy.java
 * Step5_CheckTaxonomy
 */
public class Step5_CheckTaxonomy {

    private boolean step5_ok = true;
    private boolean involved = false;
    private int nbSynonymsInvolved;
    
    /**
     * 
     * src.beans
     * Step5_CheckTaxonomy
     */
    public Step5_CheckTaxonomy(){
	
    }

    public boolean isStep5_ok() {
        return step5_ok;
    }

    public void setStep5_ok(boolean step5_ok) {
        this.step5_ok = step5_ok;
    }

    public boolean isInvolved() {
        return involved;
    }

    public void setInvolved(boolean involved) {
        this.involved = involved;
    }
 
}
