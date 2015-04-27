/**
 * src.beans
 * Step3_CheckGeoIssue
 */
package src.beans;

/**
 * src.beans
 * 
 * Step3_CheckGeoIssue.java
 * Step3_CheckGeoIssue
 */
public class Step3_CheckGeoIssue {
    private boolean step3_ok = false;
    private int nbFound = 0;
    private boolean involved = true;
    private String pathWrongGeoIssue= "";
    
    /**
     * 
     * src.beans
     * Step3_CheckGeoIssue
     */
    public Step3_CheckGeoIssue(){
	
    }

    public boolean isInvolved() {
        return involved;
    }


    public void setInvolved(boolean involved) {
        this.involved = involved;
    }

    public boolean isStep3_ok() {
        return step3_ok;
    }

    public void setStep3_ok(boolean step3_ok) {
        this.step3_ok = step3_ok;
    }

    public int getNbFound() {
        return nbFound;
    }

    public void setNbFound(int nbFound) {
        this.nbFound = nbFound;
    }

    public String getPathWrongGeoIssue() {
        return pathWrongGeoIssue;
    }

    public void setPathWrongGeoIssue(String pathWrongGeoIssue) {
        this.pathWrongGeoIssue = pathWrongGeoIssue;
    }

    
}
