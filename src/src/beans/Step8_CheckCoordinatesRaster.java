/**
 * src.beans
 * Step8_CheckCoordinatesRaster
 */
package src.beans;

/**
 * src.beans
 * 
 * Step8_CheckCoordinatesRaster.java
 * Step8_CheckCoordinatesRaster
 */
public class Step8_CheckCoordinatesRaster {

    private boolean step8_ok = true;
    private boolean involved = false;
    private int nbFound = 0;
    private String pathWrongRaster = "";
    
    public Step8_CheckCoordinatesRaster(){
	
    }

    public boolean isStep8_ok() {
        return step8_ok;
    }

    public void setStep8_ok(boolean step8_ok) {
        this.step8_ok = step8_ok;
    }

    public boolean isInvolved() {
        return involved;
    }

    public void setInvolved(boolean involved) {
        this.involved = involved;
    }

    public int getNbFound() {
        return nbFound;
    }

    public void setNbFound(int nbFound) {
        this.nbFound = nbFound;
    }

    public String getPathWrongRaster() {
        return pathWrongRaster;
    }

    public void setPathWrongRaster(String pathWrongRaster) {
        this.pathWrongRaster = pathWrongRaster;
    }
    
    
}
