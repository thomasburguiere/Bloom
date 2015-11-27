/**
 * fr.bird.bloom.beans
 * Step1_MappingDwc
 */
package fr.bird.bloom.stepresults;

import fr.bird.bloom.model.MappingDwC;

import java.util.HashMap;
import java.util.Map;

/**
 * fr.bird.bloom.beans
 * 
 * Step1_MappingDwc.java
 * Step1_MappingDwc
 */
public class Step1_MappingDwc {

	private Map<Integer,MappingDwC> infos_mapping = new HashMap<>();
	private int nbInputs = 0;
    private boolean involved = false;
    //private HashMap<MappingDwC, String> mappedFilesAssociatedPath = new HashMap<MappingDwC, String>();
   //<!--<c:if test="${count1 == info.idFile}">-->
    //<!--<span class="value"><c:out value='${info.value.isSuccess}'/></span>-->
    
    public Step1_MappingDwc(){
	
    }

	public Map<Integer, MappingDwC> getInfos_mapping() {
		return infos_mapping;
	}

	public void setInfos_mapping(HashMap<Integer, MappingDwC> infos_mapping) {
		this.infos_mapping = infos_mapping;
	}

	public boolean isInvolved() {
		return involved;
	}

	public void setInvolved(boolean involved) {
		this.involved = involved;
	}

	public int getNbInputs() {return nbInputs;}

	public void setNbInputs(int nbInputs) {this.nbInputs = nbInputs;}

}
