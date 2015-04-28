/**
 * src.beans
 * Step1_MappingDwc
 */
package src.beans;

import java.util.HashMap;
import src.model.MappingDwC;

/**
 * src.beans
 * 
 * Step1_MappingDwc.java
 * Step1_MappingDwc
 */
public class Step1_MappingDwc {

    private boolean step1_ok = true;
    private boolean involved = false;
    private HashMap<MappingDwC, String> mappedFilesAssociatedPath = new HashMap<MappingDwC, String>();
    
    public Step1_MappingDwc(){
	
    }

    public boolean isStep1_ok() {
        return step1_ok;
    }

    public void setStep1_ok(boolean step1_ok) {
        this.step1_ok = step1_ok;
    }

    public boolean isInvolved() {
        return involved;
    }

    public void setInvolved(boolean involved) {
        this.involved = involved;
    }

    public HashMap<MappingDwC, String> getMappedFilesAssociatedPath() {
        return this.mappedFilesAssociatedPath;
    }

    public void setMappedFilesAssociatedPath(HashMap<MappingDwC, String> mappedFilesAssociatedPath) {
        this.mappedFilesAssociatedPath = mappedFilesAssociatedPath;
    }
}
