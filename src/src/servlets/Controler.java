/**
 * src.servlets
 * LaunchWorkflow
 * TODO
 */
package src.servlets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import src.beans.Finalisation;
import src.beans.Initialise;
import src.model.CSVFile;
import src.model.LaunchWorkflow;
import src.model.MappingDwC;

/**
 * src.servlets
 * 
 * LaunchWorkflow
 */

@WebServlet(name = "Controler")
public class Controler extends HttpServlet {

    private String DIRECTORY_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/"; 
    private Initialise initialisation;
    private int nbFileRandom;
    private Finalisation finalisation;
    
    /**
     * 
     * src.servlets
     * Controler
     */
    public Controler(){

    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     * @return void
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
	processRequest(request, response);
    }

    /**
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     * @return void
     */
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{	
	response.setContentType("text/plain");
	initialisation = new Initialise();
	
	this.setNbFileRandom(this.generateRandomKey());
	this.initialisation.setNbFileRandom(this.getNbFileRandom());
	
	List<FileItem> listFileItems = this.initialiseRequest(request);
	this.initialiseParameters(listFileItems, response);
	request.setAttribute("initialise", initialisation);	

	LaunchWorkflow newLaunch = new LaunchWorkflow(this.initialisation);

	newLaunch.initialiseLaunchWorkflow();
	
	finalisation = newLaunch.getFinalisation();
	request.setAttribute("finalisation", finalisation);
	
	this.getServletContext().getRequestDispatcher("/finalWorkflow.jsp").forward(request, response);

    }

    /**
     * Retrieve all request from the formulary
     * 
     * @param request
     * @return List<FileItem>
     */
    public List<FileItem> initialiseRequest(HttpServletRequest request){

	DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
	ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
	List<FileItem> items = null;
	try {
	    items = (List<FileItem>)uploadHandler.parseRequest(request);
	} catch (FileUploadException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return items;
    }

    /**
     * Initialise parameters/options
     * 
     * @param items
     * @param response
     * @throws IOException
     * @return void
     */
    public void initialiseParameters(List<FileItem> items, HttpServletResponse response) throws IOException{

	response.setContentType("text/html");

	Iterator<FileItem> iterator = (Iterator<FileItem>)items.iterator();
	ArrayList<MappingDwC> listDwcFiles = new ArrayList<>();

	int nbFilesInput = 0;
	int nbFilesRaster = 0;
	int nbFilesHeader = 0;
	int nbFilesSynonyms = 0;
	int nbMappingInput = 0;

	if(!new File(DIRECTORY_PATH + "temp/").exists()){
	    new File(DIRECTORY_PATH + "temp/").mkdirs();
	}
	if(!new File(DIRECTORY_PATH + "temp/data/").exists()){
	    new File(DIRECTORY_PATH + "temp/data/").mkdirs();
	}

	while (iterator.hasNext()) {
	    // DiskFileItem item = (DiskFileItem) iterator.next();
	    FileItem item = iterator.next();
	    String input = "inp_" + nbFilesInput;
	    String raster = "raster_" + nbFilesRaster;
	    String headerRaster = "header_" + nbFilesHeader;
	    String synonyms = "synonyms";
	    String mapping = "mappingActive_" + nbMappingInput;

	    String fieldName = item.getFieldName();
	    // System.out.println(fieldName);
	    if(fieldName.equals(input)){
		DiskFileItem itemFile = (DiskFileItem) item;
		String fileExtensionName = itemFile.getName();
		fileExtensionName = FilenameUtils.getExtension(fileExtensionName);
		
		File file = new File(DIRECTORY_PATH + "temp/data/noMappedDWC_" + this.getNbFileRandom() + "_" + (nbFilesInput + 1 ) + "." + fileExtensionName);
		try {
		    itemFile.write(file);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		CSVFile csvFile = new CSVFile(file);
		MappingDwC newMappingDWC = new MappingDwC(csvFile, false);
		newMappingDWC.setCounterID(nbFilesInput + 1);
		newMappingDWC.setOriginalName(itemFile.getName());
		newMappingDWC.setOriginalExtension(fileExtensionName);
		listDwcFiles.add(newMappingDWC);
		
		newMappingDWC.initialiseMapping();
		HashMap<String, String> connectionTags = new HashMap<>();
		ArrayList<String> tagsNoMapped = newMappingDWC.getTagsListNoMapped();
		for(int i = 0 ; i < tagsNoMapped.size() ; i++){
		    connectionTags.put(tagsNoMapped.get(i) + "_" + i, "");
		}
		newMappingDWC.setConnectionTags(connectionTags);
		newMappingDWC.getNoMappedFile().setCsvName(file.getName());
		//initialisation.getInputFilesList().add(csvFile.getCsvFile());
		nbFilesInput ++;
	    }
	    else if(fieldName.equals(raster)){
		//System.out.println("if raster : " + item);
		initialisation.setRaster(true);

		String fileExtensionName = item.getName();
		fileExtensionName = FilenameUtils.getExtension(fileExtensionName);
		String fileName = item.getName();
		File file = new File(DIRECTORY_PATH + "temp/data/" + fileName);
		try {
		    item.write(file);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		this.initialisation.getInputRastersList().add(file);
		nbFilesRaster ++;
	    }
	    else if(fieldName.equals(headerRaster)){
		//System.out.println("if header : " + item);

		String fileExtensionName = item.getName();
		fileExtensionName = FilenameUtils.getExtension(fileExtensionName);
		String fileName = item.getName();
		File file = new File(DIRECTORY_PATH + "temp/data/" + fileName);
		try {
		    item.write(file);
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		this.initialisation.getHeaderRasterList().add(file);
		nbFilesHeader ++;
	    }
	    else if(fieldName.equals(synonyms)){
		initialisation.setSynonym(true);		
	    }
	    else if(fieldName.equals("tdwg4")){
		initialisation.setTdwg4Code(true);
	    }
	    else if(fieldName.equals("establishment")){
		initialisation.setEstablishment(true);
	    }
	    else if(fieldName.contains("dropdownDwC_")){
		String valueDropdown = item.getString();
		String [] tableauField = fieldName.split("_");
		String idDropdown = tableauField[tableauField.length-1];
		for(int i = 0 ; i < listDwcFiles.size() ; i++ ){
		    HashMap<String, String> connectionTags = listDwcFiles.get(i).getConnectionTags();
		    for(Entry<String, String> entry : connectionTags.entrySet()) {
			String [] tableKey = entry.getKey().split("_");
			String idKey = tableKey[tableKey.length-1];
			if(idDropdown.equals(idKey)){
			    connectionTags.put(entry.getKey(), valueDropdown);
			}    
		    }
		}
	    }
	    else if(fieldName.equals(mapping)){
		//System.out.println("if mapping : " + item.getString());
		if(item.getString().equals("true")){
		    for(int i = 0 ; i < listDwcFiles.size() ; i++ ){
			int idFile = listDwcFiles.get(i).getCounterID();
			if(idFile == (nbMappingInput + 1 )){
			    MappingDwC mappingDWC = listDwcFiles.get(i);
			    mappingDWC.setMapping(true);
			}
		    }
		}

		nbMappingInput ++;
	    }

	    if(initialisation.isEstablishment()){
		String param = item.getFieldName();
		//System.out.println(param);
		switch(param){
		case "native" : this.initialisation.getEstablishmentList().add("native");
		break;
		case "introduced" : this.initialisation.getEstablishmentList().add("introduced");
		break;
		case "naturalised" : this.initialisation.getEstablishmentList().add("naturalised");
		break;
		case "invasive" : this.initialisation.getEstablishmentList().add("invasive");
		break;
		case "managed" : this.initialisation.getEstablishmentList().add("managed");
		break;
		case "uncertain" : this.initialisation.getEstablishmentList().add("uncertain");
		break;	
		case "others" : this.initialisation.getEstablishmentList().add("others");
		break;
		}
	    }

	}
	
	this.initialisation.setListDwcFiles(listDwcFiles);

    }

    /**
     * 
     * @return String
     */
    public String getDIRECTORY_PATH() {
	return DIRECTORY_PATH;
    }

    /**
     * 
     * @param dIRECTORY_PATH
     * @return void
     */
    public void setDIRECTORY_PATH(String dIRECTORY_PATH) {
	DIRECTORY_PATH = dIRECTORY_PATH;
    }

    /**
     * 
     * @return Initialise
     */
    public Initialise getInitialisation() {
	return initialisation;
    }

    /**
     * 
     * @param initialisation
     * @return void
     */
    public void setInitialisation(Initialise initialisation) {
	this.initialisation = initialisation;
    }

    /**
     * 
     * @return int
     */
    public int getNbFileRandom() {
        return nbFileRandom;
    }

    /**
     * 
     * @param nbFileRandom
     * @return void
     */
    public void setNbFileRandom(int nbFileRandom) {
        this.nbFileRandom = nbFileRandom;
    }

    /**
     * 
     * @return int
     */
    public int generateRandomKey(){
	Random random = new Random();
	nbFileRandom = random.nextInt();
	return nbFileRandom;
    }

}
