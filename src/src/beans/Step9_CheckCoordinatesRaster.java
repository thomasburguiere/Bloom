/**
 * src.beans
 * Step9_CheckCoordinatesRaster
 */
package src.beans;

/**
 * src.beans
 * 
 * Step9_CheckCoordinatesRaster.java
 * Step9_CheckCoordinatesRaster
 */
public class Step9_CheckCoordinatesRaster {

    private boolean step9_ok = true;
    private boolean involved = false;
    private int nbFound = 0;
    private String pathWrongRaster = "";
    
    public Step9_CheckCoordinatesRaster(){
	
    }

    public boolean isStep9_ok() {
        return step9_ok;
    }

    public void setStep9_ok(boolean step9_ok) {
        this.step9_ok = step9_ok;
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
