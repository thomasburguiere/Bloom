/**
 * src.model
 * MappingDwC
 * TODO
 */
package src.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * src.model
 * 
 * MappingDwC.java
 * MappingDwC
 */
public class MappingDwC {

    private File noMappedFile;
    private File mappedFile;
    private ArrayList<String> presentTags;
    private ArrayList<String> tagsListNoMapped;
    private ArrayList<String> tagsListDwC;
    private int counter;
    
    public MappingDwC(File noMappedFile){
	this.noMappedFile = noMappedFile;
    }
    
    public File getNoMappedFile() {
        return noMappedFile;
    }

    public void setNoMappedFile(File noMappedFile) {
        this.noMappedFile = noMappedFile;
    }

    public File getMappedFile() {
        return mappedFile;
    }

    public void setMappedFile(File mappedFile) {
        this.mappedFile = mappedFile;
    }
    
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    
    public ArrayList<String> getPresentTags() {
        return presentTags;
    }

    public void setPresentTags(ArrayList<String> presentTags) {
        this.presentTags = presentTags;
    }

    public ArrayList<String> getTagsListNoMapped() {
        return tagsListNoMapped;
    }

    public void setTagsListNoMapped(ArrayList<String> tagsListNoMapped) {
        this.tagsListNoMapped = tagsListNoMapped;
    }

    public ArrayList<String> getTagsListDwC() {
        return tagsListDwC;
    }

    public void setTagsListDwC(ArrayList<String> tagsListDwC) {
        this.tagsListDwC = tagsListDwC;
    }

    public void mappingDwC(){
	ConnectionDatabase columnsNameDwC = new ConnectionDatabase();
	String choiceStatement = "executeQuery";
	ArrayList<String> messages = new ArrayList<String>();
	messages.add("\n--- Get columns name of DwC format ---");
	String getColumnsName = "SHOW COLUMNS FROM Workflow.DarwinCoreInput;";
	messages.addAll(columnsNameDwC.newConnection(choiceStatement, getColumnsName));
	
	for(int i = 0 ; i < messages.size() ; i++){
	    System.out.println(messages.get(i));
	}
	
	
	CSVFile csvNoMapped = new CSVFile(this.getNoMappedFile());
	ArrayList<String> linesCSV = csvNoMapped.getLines();
	String [] firstLine = linesCSV.get(0).split(csvNoMapped.getSeparator());
	ArrayList<String > tagsListNoMapped = new ArrayList(Arrays.asList(firstLine));
	this.setTagsListNoMapped(tagsListNoMapped);
    }
    
}
