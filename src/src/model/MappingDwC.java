/**
 * src.model
 * MappingDwC
 * TODO
 */
package src.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * src.model
 * 
 * MappingDwC.java
 * MappingDwC
 */
public class MappingDwC {

    private CSVFile noMappedFile;
    private File mappedFile;
    private ArrayList<String> presentTags;
    private ArrayList<String> tagsListNoMapped;
    private ArrayList<String> tagsListDwC;
    private HashMap<String, String> connectionTags;
    private HashMap<String, ArrayList<String>> connectionValuesTags;
    private boolean mapping;
    private int counterID;

    public MappingDwC(CSVFile noMappedFile, boolean mapping){
	this.noMappedFile = noMappedFile;
	this.mapping = mapping;
    }

    public CSVFile getNoMappedFile() {
	return noMappedFile;
    }

    public void setNoMappedFile(CSVFile noMappedFile) {
	this.noMappedFile = noMappedFile;
    }

    public File getMappedFile() {
	return mappedFile;
    }

    public void setMappedFile(File mappedFile) {
	this.mappedFile = mappedFile;
    }

    public int getCounterID() {
	return counterID;
    }

    public void setCounterID(int counterID) {
	this.counterID = counterID;
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

    public boolean isMapping() {
	return mapping;
    }

    public void setMapping(boolean mapping) {
	this.mapping = mapping;
    }

    public HashMap<String, String> getConnectionTags() {
	return connectionTags;
    }

    public void setConnectionTags(HashMap<String, String> connectionTags) {
	this.connectionTags = connectionTags;
    }

    public HashMap<String, ArrayList<String>> getConnectionValuesTags() {
	return connectionValuesTags;
    }

    public void setConnectionValuesTags(HashMap<String, ArrayList<String>> connectionValuesTags) {
	this.connectionValuesTags = connectionValuesTags;
    }

    public void initialiseMapping(){
	this.setTagsListDwC(this.initialiseDwCTags());
	this.setTagsListNoMapped(this.initialiseNoMappedTags());
	this.setPresentTags(this.initialisePresentTags());
    }

    public void mappingDwC() throws IOException{
	CSVFile noMappedFile = this.getNoMappedFile();

	this.setConnectionValuesTags(this.doConnectionValuesTags());
	File mappedFile = this.createNewDwcFile();
	this.setMappedFile(mappedFile);
    }

    public File createNewDwcFile() throws IOException{
	String mappedFilename = noMappedFile.getCsvFile().getParent() + "/mappedDWC_" + noMappedFile.getCsvFile().getName();
	File mappedFile = new File(mappedFilename);
	FileWriter writerMappedFile = new FileWriter (mappedFile);
	HashMap<String, String> connectionTags = this.getConnectionTags();
	HashMap<String, ArrayList<String>> connectionValuesTags = this.getConnectionValuesTags();
	String firstNewLine = "";
	int nbCol = connectionTags.size();
	int countCol = 1;

	for(Entry<String, String> entryDwC : connectionTags.entrySet()){
	    String valueNoMapped = entryDwC.getValue();
	    if(countCol < nbCol){
		firstNewLine += valueNoMapped + ",";
	    }
	    else{
		firstNewLine += valueNoMapped + "\n";
	    }
	    countCol ++;
	}
	writerMappedFile.write(firstNewLine);
	int countLines = 0;
	int nbLines = noMappedFile.getLines().size();
	countCol = 1;
	while(countLines < nbLines - 1){
	    String lineValues = "";
	    countCol = 1;
	    for(Entry<String, ArrayList<String>> entryValuesTags : connectionValuesTags.entrySet()){
		ArrayList<String> listValues = entryValuesTags.getValue();
		if(countCol < nbCol){
		    //System.out.println(countLines + "  " + listValues.size());
		    //System.out.println(entryValuesTags.getKey() + "  " + listValues);
		    lineValues += listValues.get(countLines) +",";
		}
		else{
		    lineValues += listValues.get(countLines) +"\n";
		}
		countCol ++;
	    }
	    //System.out.println(countCol + " " + lineValues);
	    writerMappedFile.write(lineValues);
	    countLines++;
	}
	writerMappedFile.close();
	return mappedFile;
    }

    public ArrayList<String> initialiseDwCTags(){

	ArrayList<String> tagsListDwCInit = new ArrayList<String>();
	ArrayList<String> tempList = new ArrayList<String>();
	ConnectionDatabase columnsNameDwC = new ConnectionDatabase();
	String choiceStatement = "executeQuery";
	ArrayList<String> messages = new ArrayList<String>();
	messages.add("\n--- Get columns name of DwC format ---");
	String getColumnsName = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS where TABLE_NAME='DarwinCoreInput';";
	messages.addAll(columnsNameDwC.newConnection(choiceStatement, getColumnsName));

	tempList = columnsNameDwC.getResultatSelect();
	// delete "COLUMN_NAME" and "id_"
	tempList.remove(0);
	tempList.remove(0);

	for(int i = 0 ; i < messages.size() ; i++){
	    System.out.println(messages.get(i));
	}

	for(int i = 0 ; i < tempList.size() ; i++){
	    String [] tag = tempList.get(i).split("_");
	    String tagRename = tag[0].replace("\"", "");
	    tagsListDwCInit.add(tagRename);
	}

	return tagsListDwCInit;
    }

    public ArrayList<String> initialiseNoMappedTags(){
	CSVFile csvNoMapped = new CSVFile(this.getNoMappedFile().getCsvFile());
	ArrayList<String> linesCSV = csvNoMapped.getLines();
	String [] firstLine = linesCSV.get(0).split(csvNoMapped.getSeparator());
	ArrayList<String > tagsListNoMappedInit = new ArrayList(Arrays.asList(firstLine));

	return tagsListNoMappedInit;
    }

    public ArrayList<String> initialisePresentTags(){
	ArrayList<String> presentTagsInit = new ArrayList<String>();
	ArrayList<String> noMappedTags = this.getTagsListNoMapped();
	ArrayList<String> DwCtags = this.getTagsListDwC();

	for(int i = 0 ; i < noMappedTags.size() ; i++){
	    String noMappedTag = noMappedTags.get(i);
	    if(DwCtags.contains(noMappedTag)){
		presentTagsInit.add(noMappedTag);
	    }
	}
	return presentTagsInit;
    }

    
    public HashMap<String, ArrayList<String>> doConnectionValuesTags(){
	HashMap<String, ArrayList<String>> connectionValuesTags = new HashMap<String, ArrayList<String>>();
	CSVFile noMappedFile = this.getNoMappedFile();
	try{
	    InputStream inputStreamNoMapped = new FileInputStream(noMappedFile.getCsvFile()); 
	    InputStreamReader inputStreamReaderNoMapped = new InputStreamReader(inputStreamNoMapped);
	    BufferedReader readerNoMapped = new BufferedReader(inputStreamReaderNoMapped);
	    String line = "";
	    int countLine = 0;
	    while ((line = readerNoMapped.readLine()) != null){
		String [] lineSplit = line.split(noMappedFile.getSeparator(), -1);
		ArrayList<String> listValuesMap = new ArrayList<>();
		for(int i = 0 ; i < lineSplit.length ; i++){
		    String id = "";
		    if(countLine == 0){
			id = lineSplit[i] + "_" + i;
			ArrayList<String> values = new ArrayList<>();
			connectionValuesTags.put(id, values);
		    }
		    else{
			for(Entry<String, ArrayList<String>> entry : connectionValuesTags.entrySet()){
			    String [] idKeyTable = entry.getKey().split("_");
			    String idKey = idKeyTable[idKeyTable.length-1];
			    if(Integer.parseInt(idKey) == i){
				listValuesMap = entry.getValue();
				if(!lineSplit[i].isEmpty()){
				    listValuesMap.add(lineSplit[i]);
				}
				else{
				    listValuesMap.add(" ");
				}

				id = entry.getKey();
			    }
			}
			connectionValuesTags.put(id, listValuesMap);
		    }

		}

		countLine++;
	    }
	    readerNoMapped.close(); 
	    System.out.println(connectionValuesTags);
	}
	catch(Exception e){
	    System.err.println(e);
	}

	return connectionValuesTags;
    }
}
