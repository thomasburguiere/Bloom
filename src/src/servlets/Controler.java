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

    private ArrayList<File> listFiles;
    private ArrayList<File> rasterFiles;
    
    private String filePath;
    private File file ;
    private int maxMemSize = 4 * 1024;
    private int maxFileSize = 50 * 1024;
    
    private File synonymFile;	
    private boolean synonyms;
    private boolean tdwg4Code;
    private boolean worldCell;

    private TreatmentData dataTreatment;
    
    public Controler(){
	
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
	dataTreatment = new TreatmentData();
	try {
	    this.initialise(request, response);
	} catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public void control() throws IOException{
	if(this.listFiles.size() >= 1){
	    System.out.println("random : " + dataTreatment.generateRandomKey());
	    for(int i = 0 ; i < listFiles.size() ; i++){
		int idFile = i + 1;
        		
		List<String> linesInputFile = dataTreatment.initialiseFile(listFiles.get(i), idFile);
        			
        	File inputFileModified = dataTreatment.createTemporaryFile(linesInputFile, idFile);
        	String sqlInsert = dataTreatment.createSQLInsert(inputFileModified, linesInputFile);
        	dataTreatment.createTableDarwinCoreInput(sqlInsert);
	    }	
	}
	else{
		System.out.println("Erreur, il n'y a pas de fichier");	
	}
	dataTreatment.deleteWrongIso2();
	dataTreatment.createTableClean();
	File wrongCoordinatesFile = dataTreatment.deleteWrongCoordinates();
	File wrongGeospatial = dataTreatment.deleteWrongGeospatial();
	
	
	dataTreatment.getPolygonTreatment();
	
	
    }
    /* These methods are called in vue (Ajax)*/
    public void initialise(HttpServletRequest request, HttpServletResponse response) throws Exception{
	
	Initialise init = new Initialise();
	
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
		
		if(item.getFieldName().equals(input)){
		    System.out.println("if : " + item.getFieldName());
		    String fileExtensionName = item.getName();
		    fileExtensionName = FilenameUtils.getExtension(fileExtensionName);
		    String fileName = item.getStoreLocation().getName();
		    File file = new File(System.getProperty("user.dir") + "/workspace/WebWorkflowCleanData/data/" + fileName + "." + fileExtensionName);
		    item.write(file);
		    inputFilesList.add(file);
		    nbFiles =+ 1;
		}	
	    }
	    init.setInputFile(inputFilesList);
	}
	catch(Exception e){
	    e.printStackTrace();
	}

    }
	
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
    
    public File fileToInputStreamApp(Part uploadedFile) throws IOException{
	Random random = new Random();
	int nbFileRandom = random.nextInt();
	
	InputStream content = uploadedFile.getInputStream();
	
	BufferedReader br = null;
	
	File inputFile = new File(System.getProperty("user.dir") + "/workspace/WebWorkflowCleanData/data/" + uploadedFile.getName());
	Writer fileWriter = new FileWriter(inputFile);
	try {
		br = new BufferedReader(new InputStreamReader(content));


		String line;
		while ((line = br.readLine()) != null) {
			fileWriter.write(line);
		}

	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (content != null) {
			try {
			    content.close();
			    fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	return inputFile;
    }
}
