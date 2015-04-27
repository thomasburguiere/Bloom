/**
 * src.beans
 * Step2_CheckCoordinates
 */
package src.beans;


/**
 * src.beans
 * 
 * Step2_CheckCoordinates.java
 * Step2_CheckCoordinates
 */
public class Step2_CheckCoordinates {
    private boolean step2_ok = false;
    private int nbFound = 0;
    private boolean involved = true;
    private String pathWrongCoordinates = "";
    
    /**
     * src.beans
     * Step2_CheckCoordinates
     */
    public Step2_CheckCoordinates() {

    }

    public boolean isInvolved() {
        return involved;
    }

    public void setInvolved(boolean involved) {
        this.involved = involved;
    }

    public boolean isStep2_ok() {
        return step2_ok;
    }

    public void setStep2_ok(boolean step2_ok) {
        this.step2_ok = step2_ok;
    }

    public int getNbFound() {
        return nbFound;
    }

    public void setNbFound(int nbFound) {
        this.nbFound = nbFound;
    }

    public String getPathWrongCoordinates() {
        return pathWrongCoordinates;
    }

    public void setPathWrongCoordinates(String pathWrongCoordinates) {
        this.pathWrongCoordinates = pathWrongCoordinates;
    }
    
    
}
