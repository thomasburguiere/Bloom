/**
 * @author mhachet
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
/**
 * 
 * src.model
 * 
 * MappingDwC.java
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
    private String originalName = "";
    private String originalExtension = "";
    
    /**
     * 
     * src.model
     * MappingDwC
     */
    public MappingDwC(CSVFile noMappedFile, boolean mapping){
	this.noMappedFile = noMappedFile;
	this.mapping = mapping;
    }
    
    /**
     * Initialise mapping to DarwinCore format 
     * set correct DwC tags
     * set all tags in input file
     * set present tags in both 
     * 
     * @return void
     */
    public void initialiseMapping(String nbSessionRandom){
	this.setTagsListDwC(this.initialiseDwCTags(nbSessionRandom));
	this.setTagsListNoMapped(this.initialiseNoMappedTags());
	this.setPresentTags(this.initialisePresentTags());
    }

    /**
     * Create the mapped DwC file
     * 
     * @param nbFileRandom
     * @throws IOException
     * @return File
     */
    public File createNewDwcFile(String nbFileRandom) throws IOException{
	String mappedFilename = noMappedFile.getCsvFile().getParent() + "/mappedDWC_" + nbFileRandom + "_" + this.getCounterID() +".csv";
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
		    lineValues += listValues.get(countLines) +",";
		}
		else{
		    lineValues += listValues.get(countLines) +"\n";
		}
		countCol ++;
	    }
	    writerMappedFile.write(lineValues);
	    countLines++;
	}
	writerMappedFile.close();
	return mappedFile;
    }

    /**
     * Found all DwC tags
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> initialiseDwCTags(String nbSessionRandom){

	ArrayList<String> tagsListDwCInit = new ArrayList<String>();
	ArrayList<String> tempList = new ArrayList<String>();
	ConnectionDatabase columnsNameDwC = new ConnectionDatabase();
	String choiceStatement = "executeQuery";
	ArrayList<String> messages = new ArrayList<String>();
	messages.add("\n--- Get columns name of DwC format ---");
	String getColumnsName = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME='DarwinCoreInput';";
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

    /**
     * Found all tags in input file
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> initialiseNoMappedTags(){
	CSVFile csvNoMapped = new CSVFile(this.getNoMappedFile().getCsvFile());
	ArrayList<String> linesCSV = csvNoMapped.getLines();
	String [] firstLine = linesCSV.get(0).split(csvNoMapped.getSeparator());
	ArrayList<String > tagsListNoMappedInit = new ArrayList(Arrays.asList(firstLine));

	return tagsListNoMappedInit;
    }

    /**
     * Found all tags already DwC in input file 
     * 
     * @return ArrayList<String>
     */
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

    /**
     * Connect tags (from input file) to value (from input file)
     * 
     * @return HashMap<String,ArrayList<String>>
     */
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
	}
	catch(Exception e){
	    System.err.println(e);
	}
	System.out.println(connectionValuesTags);
	return connectionValuesTags;
    }
    
    public boolean doMapping(){
	
	ArrayList<String> listNoMapped = this.getTagsListNoMapped();
	ArrayList<String> listDwCTags= this.getTagsListDwC();

	for(int i = 0 ; i < listNoMapped.size() ; i++){
	    String tag = listNoMapped.get(i);
	    if(!listDwCTags.contains(tag)){
		//System.out.println(tag);
		
		return true;
	    }
	    else{
		
	    }
	}
	
	return false;
    }
    
    /**
     * 
     * @return String
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * 
     * @param originalName
     * @return void
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }   

    /**
     * 
     * @return String
     */
    public String getOriginalExtension() {
        return originalExtension;
    }

    /**
     * 
     * @param originalExtension
     * @return void
     */
    public void setOriginalExtension(String originalExtension) {
        this.originalExtension = originalExtension;
    }

    /**
     * 
     * @return CSVFile
     */
    public CSVFile getNoMappedFile() {
	return noMappedFile;
    }

    /**
     * 
     * @param noMappedFile
     * @return void
     */
    public void setNoMappedFile(CSVFile noMappedFile) {
	this.noMappedFile = noMappedFile;
    }

    /**
     * 
     * @return File
     */
    public File getMappedFile() {
	return mappedFile;
    }

    /**
     * 
     * @param mappedFile
     * @return void
     */
    public void setMappedFile(File mappedFile) {
	this.mappedFile = mappedFile;
    }

    /**
     * 
     * @return int
     */
    public int getCounterID() {
	return counterID;
    }

    /**
     * 
     * @param counterID
     * @return void
     */
    public void setCounterID(int counterID) {
	this.counterID = counterID;
    }

    /**
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getPresentTags() {
	return presentTags;
    }

    /**
     * 
     * @param presentTags
     * @return void
     */
    public void setPresentTags(ArrayList<String> presentTags) {
	this.presentTags = presentTags;
    }

    /**
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getTagsListNoMapped() {
	return tagsListNoMapped;
    }

    /**
     * 
     * @param tagsListNoMapped
     * @return void
     */
    public void setTagsListNoMapped(ArrayList<String> tagsListNoMapped) {
	this.tagsListNoMapped = tagsListNoMapped;
    }

    /**
     * 
     * @return ArrayList<String>
     */
    public ArrayList<String> getTagsListDwC() {
	return tagsListDwC;
    }

    /**
     * 
     * @param tagsListDwC
     * @return void
     */
    public void setTagsListDwC(ArrayList<String> tagsListDwC) {
	this.tagsListDwC = tagsListDwC;
    }

    /**
     * 
     * @return boolean
     */
    public boolean isMapping() {
	return mapping;
    }

    /**
     * 
     * @param mapping
     * @return void
     */
    public void setMapping(boolean mapping) {
	this.mapping = mapping;
    }

    /**
     * 
     * @return HashMap<String,String>
     */
    public HashMap<String, String> getConnectionTags() {
	return connectionTags;
    }

    /**
     * 
     * @param connectionTags
     * @return void
     */
    public void setConnectionTags(HashMap<String, String> connectionTags) {
	this.connectionTags = connectionTags;
    }

    /**
     * 
     * @return HashMap<String,ArrayList<String>>
     */
    public HashMap<String, ArrayList<String>> getConnectionValuesTags() {
	return connectionValuesTags;
    }

    /**
     * 
     * @param connectionValuesTags
     * @return void
     */
    public void setConnectionValuesTags(HashMap<String, ArrayList<String>> connectionValuesTags) {
	this.connectionValuesTags = connectionValuesTags;
    }
}
