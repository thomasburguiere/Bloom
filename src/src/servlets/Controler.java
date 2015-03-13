/**
 * src.servlets
 * LaunchWorkflow
 * TODO
 */
package src.servlets;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import src.beans.Initialise;
import src.model.TreatmentData;

/**
 * src.servlets
 * 
 * LaunchWorkflow
 */

@WebServlet("/upload")
@MultipartConfig
public class Controler extends HttpServlet {

    
    private boolean inputFilesOK;
    
    private String DIRECTORY_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/"; 
    private Initialise init;
    private boolean synonyms;
    private boolean tdwg4Code;
    private boolean worldCell;

    private TreatmentData dataTreatment;
    
    public Controler(){
	
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
	dataTreatment = new TreatmentData();
	
	try {
	    inputFilesOK = this.initialiseInputFiles(request, response);
	    this.initialiseRastersFiles(request, response);
	    
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	if(inputFilesOK){
	    this.launchWorkflow();
	}
    }
    
    
    public boolean initialiseInputFiles(HttpServletRequest request, HttpServletResponse response) throws Exception{
	
	init = new Initialise();
	
	// on prépare pour l'envoie par la mise en oeuvre en mémoire
	DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
	ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
	try {
	    List<FileItem> items = (List<FileItem>)uploadHandler.parseRequest(request);
	    ArrayList<File> inputFilesList = new ArrayList<>();
	    Iterator<FileItem> iterator = (Iterator<FileItem>)items.iterator();
	    int nbFiles = 0;
	    while (iterator.hasNext()) {
		DiskFileItem item = (DiskFileItem) iterator.next();
		String input = "inp" + nbFiles;

		if(!new File(DIRECTORY_PATH + "/temp/").exists()){
		    new File(DIRECTORY_PATH + "/temp/").mkdirs();
		    if(!new File(DIRECTORY_PATH + "/temp/data/").exists()){
			    new File(DIRECTORY_PATH + "/temp/data/").mkdirs();
			    
			}
		}
		
		if(item.getFieldName().equals(input)){
		    System.out.println("if : " + item.getFieldName());
		    String fileExtensionName = item.getName();
		    fileExtensionName = FilenameUtils.getExtension(fileExtensionName);
		    String fileName = item.getStoreLocation().getName();
		    File file = new File(DIRECTORY_PATH + "/temp/data/" + fileName + "." + fileExtensionName);
		    item.write(file);
		    inputFilesList.add(file);
		    nbFiles =+ 1;
		}	
	    }
	    init.setInputFilesList(inputFilesList);
	}
	catch(Exception e){
	    e.printStackTrace();
	    return false;
	}

	if(init.getInputFilesList().size() != 0){
	    return true;
	}
	else{
	    return false;
	}
    }
	
    public void initialiseRastersFiles(HttpServletRequest request, HttpServletResponse response){
	
    }
    
    public void initialiseSynonymsFiles(HttpServletRequest request, HttpServletResponse response){
	
    }
    
    public void initialiseOptions(HttpServletRequest request, HttpServletResponse response){
	
    }
    
    public void launchWorkflow() throws IOException{
	dataTreatment.deleteTables();
	for(int i = 0 ; i < init.getInputFilesList().size() ; i++){
	    int idFile = i + 1;
    		
	    List<String> linesInputFile = dataTreatment.initialiseFile(init.getInputFilesList().get(i), idFile);
	    File inputFileModified = dataTreatment.createTemporaryFile(linesInputFile, idFile);
	    String sqlInsert = dataTreatment.createSQLInsert(inputFileModified, linesInputFile);
	    dataTreatment.createTableDarwinCoreInput(sqlInsert);
	}	
	
	dataTreatment.deleteWrongIso2();
	dataTreatment.createTableClean();
	File wrongCoordinatesFile = dataTreatment.deleteWrongCoordinates();
	File wrongGeospatial = dataTreatment.deleteWrongGeospatial();
	
	dataTreatment.getPolygonTreatment();
	
    }
   /* 
    public void initialiseRasterFile(ArrayList<File> bilFile){
	this.rasterFiles = bilFile;
    }

   
    public void initialiseSynonymFile(ArrayList<File> synonymFileList){
	if(synonymFileList.size() == 1){
	    this.synonymFile = synonymFileList.get(0);
	}
	if(synonymFileList.size() > 1){
	    System.err.println("Only one synonyms file is required");
	}
	else{
	    System.out.println("Default file doesn't change");
	}
    }
   
    public void initialiseOptions(boolean synonyms, boolean tdwg4Code, boolean worldCell){
	this.synonyms = synonyms;
	this.tdwg4Code = tdwg4Code;
	this.worldCell = worldCell;
    }
    */
}
