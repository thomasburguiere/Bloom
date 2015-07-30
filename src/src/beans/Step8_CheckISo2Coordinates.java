/**
 * src.beans
 * Step8_CheckISo2Coordinates
 */
package src.beans;

/**
 * src.beans
 * 
 * Step8_CheckISo2Coordinates.java
 * Step8_CheckISo2Coordinates
 */
public class Step8_CheckISo2Coordinates {

    private boolean step8_ok = true;
    private String pathWrongIso2 = "";
    private int nbFound = 0;
    private boolean involved = true;
    
    public Step8_CheckISo2Coordinates(){
	
    }

    public boolean isStep8_ok() {
        return step8_ok;
    }

    public void setStep8_ok(boolean step8_ok) {
        this.step8_ok = step8_ok;
    }

    public String getPathWrongIso2() {
        return pathWrongIso2;
    }

    public void setPathWrongIso2(String pathWrongIso2) {
        this.pathWrongIso2 = pathWrongIso2;
    }

    public int getNbFound() {
        return nbFound;
    }

    public void setNbFound(int nbFound) {
        this.nbFound = nbFound;
    }

    public boolean isInvolved() {
        return involved;
    }

    public void setInvolved(boolean involved) {
        this.involved = involved;
    }	   
}
