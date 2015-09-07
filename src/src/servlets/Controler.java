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
import java.util.UUID;

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
import src.beans.Step1_MappingDwc;
import src.beans.Step2_ReconciliationService;
import src.beans.Step3_CheckCoordinates;
import src.beans.Step4_CheckGeoIssue;
import src.beans.Step5_IncludeSynonym;
import src.beans.Step6_CheckTDWG;
import src.beans.Step7_CheckISo2Coordinates;
import src.beans.Step8_CheckCoordinatesRaster;
import src.beans.Step9_EstablishmentMeans;
import src.model.CSVFile;
import src.model.LaunchWorkflow;
import src.model.MappingDwC;
import src.model.MappingReconcilePreparation;
import src.model.ReconciliationService;

/**
 * src.servlets
 * 
 * LaunchWorkflow
 */

@WebServlet(name = "Controler")
public class Controler extends HttpServlet {

	private String DIRECTORY_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/WebContent/"; 
	private String RESSOURCES_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/src/resources/";

	private Initialise initialisation;
	private String nbSessionRandom;
	private Finalisation finalisation;

	private Step1_MappingDwc step1;
	private Step2_ReconciliationService step2;
	private Step3_CheckCoordinates step3;
	private Step4_CheckGeoIssue step4;
	private Step5_IncludeSynonym step5;
	private Step6_CheckTDWG step6;
	private Step7_CheckISo2Coordinates step7;
	private Step8_CheckCoordinatesRaster step8;
	private Step9_EstablishmentMeans step9;

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
		initialisation.setDIRECTORY_PATH(DIRECTORY_PATH);
		initialisation.setRESSOURCES_PATH(RESSOURCES_PATH);

		this.setNbSessionRandom(this.generateRandomKey());
		this.initialisation.setNbSessionRandom(this.getNbSessionRandom());

		List<FileItem> listFileItems = this.initialiseRequest(request);	

		this.initialiseParameters(listFileItems, response, request);
		request.setAttribute("initialise", initialisation);	

		LaunchWorkflow newLaunch = new LaunchWorkflow(this.initialisation);

		newLaunch.initialiseLaunchWorkflow();

		finalisation = newLaunch.getFinalisation();
		request.setAttribute("finalisation", finalisation);

		step1 = newLaunch.getStep1();
		request.setAttribute("step1", step1);
		step2 = newLaunch.getStep2();
		request.setAttribute("step2", step2);
		step3 = newLaunch.getStep3();
		request.setAttribute("step3", step3);
		step4 = newLaunch.getStep4();
		request.setAttribute("step4", step4);
		step5 = newLaunch.getStep5();
		request.setAttribute("step5", step5);
		step6 = newLaunch.getStep6();
		request.setAttribute("step6", step6);
		step7 = newLaunch.getStep7();
		request.setAttribute("step7", step7);
		step8 = newLaunch.getStep8();
		request.setAttribute("step8", step8);
		step9 = newLaunch.getStep9();
		request.setAttribute("step9", step9);

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
	public void initialiseParameters(List<FileItem> items, HttpServletResponse response, HttpServletRequest request) throws IOException{

		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");

		Iterator<FileItem> iterator = (Iterator<FileItem>)items.iterator();
		ArrayList<MappingDwC> listMappingFiles = new ArrayList<>();
		ArrayList<ReconciliationService> listReconcileFiles = new ArrayList<>();
		ArrayList<MappingReconcilePreparation> listMappingReconcileDWC = new ArrayList<>();

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
			String mapping = "mappingActive_";
			String reconcileTable = "reconcileTable_";
			String reconcileActive = "reconcileActive_";
			String tableReconcile = "tableReconcile_";

			String fieldName = item.getFieldName();
			//System.out.println("fieldName : " + fieldName);
			if(fieldName.equals(input)){
				DiskFileItem itemFile = (DiskFileItem) item;
				String fileExtensionName = itemFile.getName();
				fileExtensionName = FilenameUtils.getExtension(fileExtensionName);

				File file = new File(DIRECTORY_PATH + "temp/data/noMappedDWC_" + this.getNbSessionRandom() + "_" + nbFilesInput + "." + fileExtensionName);
				try {
					itemFile.write(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CSVFile csvFile = new CSVFile(file);
				MappingDwC newMappingDWC = new MappingDwC(csvFile, Boolean.toString(false));

				listMappingFiles.add(newMappingDWC);

				newMappingDWC.initialiseMapping(this.getNbSessionRandom());
				HashMap<String, String> connectionTags = new HashMap<>();
				ArrayList<String> tagsNoMapped = newMappingDWC.getTagsListNoMapped();
				for(int i = 0 ; i < tagsNoMapped.size() ; i++){
					connectionTags.put(tagsNoMapped.get(i) + "_" + i, "");
				}
				//System.out.println("connectionTagsControler : " + connectionTags);
				newMappingDWC.setConnectionTags(connectionTags);
				newMappingDWC.getNoMappedFile().setCsvName(file.getName());
				//initialisation.getInputFilesList().add(csvFile.getCsvFile());
				//newMappingDWC.setFilename(itemFile.getName());
				ReconciliationService reconciliationService = new ReconciliationService();
				listReconcileFiles.add(reconciliationService);

				MappingReconcilePreparation mappingReconcileDWC = new MappingReconcilePreparation(newMappingDWC, reconciliationService, nbFilesInput);
				mappingReconcileDWC.setOriginalName(itemFile.getName());
				mappingReconcileDWC.setOriginalExtension(fileExtensionName);
				listMappingReconcileDWC.add(mappingReconcileDWC);

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
				for(int i = 0 ; i < listMappingReconcileDWC.size() ; i++ ){
					HashMap<String, String> connectionTags = listMappingReconcileDWC.get(i).getMappingDWC().getConnectionTags();
					for(Entry<String, String> entry : connectionTags.entrySet()) {
						String [] tableKey = entry.getKey().split("_");
						String idKey = tableKey[tableKey.length-1];
						if(idDropdown.equals(idKey)){
							connectionTags.put(entry.getKey(), valueDropdown);
						}    
					}
					//System.out.println("connectionTags : " + connectionTags);
				}

			}
			else if(fieldName.contains(mapping)){

				int idMapping = Integer.parseInt(fieldName.split("_")[1]);
				
					for(int i = 0 ; i < listMappingReconcileDWC.size() ; i++ ){
						int idFile = listMappingReconcileDWC.get(i).getIdFile();
						if(idFile == (idMapping)){ 
							MappingDwC mappingDWC = listMappingReconcileDWC.get(i).getMappingDWC();
							if(item.getString().equals("true")){
								mappingDWC.setMappingInvolved(Boolean.toString(true));
							}
							else{
								mappingDWC.setMappingInvolved(Boolean.toString(false));
							}
						}
					}				

				nbMappingInput ++;
			}
			else if(fieldName.contains(reconcileActive)){
				String [] tableauField =  fieldName.split("_");
				int idReconcile = Integer.parseInt(tableauField[tableauField.length-1]);
				
				for(int i = 0 ; i < listMappingReconcileDWC.size() ; i++ ){
					int idFile = listMappingReconcileDWC.get(i).getIdFile();
					if(idFile == (idReconcile)){
						ReconciliationService reconciliationService = listMappingReconcileDWC.get(i).getReconcileDWC();
						if(item.getString().equals("true")){
							reconciliationService.setReconcile(true);
							HashMap<Integer, String> linesConnectedNewName = new HashMap<>();
							reconciliationService.setLineConnectedNewName(linesConnectedNewName);
							reconciliationService.setFilename(listMappingReconcileDWC.get(i).getOriginalName());
							listReconcileFiles.add(reconciliationService);
						}
						else{
							reconciliationService.setReconcile(false);
						}
					}
				}

			}
			else if(fieldName.contains("dropdownReconcile_")){

				String [] tableauField =  fieldName.split("_");

				int idDropdown = Integer.parseInt(tableauField[tableauField.length-1]);
				int idFile = Integer.parseInt(tableauField[tableauField.length-2]);
				System.out.println("dropdownReconcile : " + fieldName + "  " + idFile + "  " + listReconcileFiles.size());
				ReconciliationService reconciliationService = listReconcileFiles.get(idFile);
				if(idDropdown == 0){
					String tag = item.getString();
					reconciliationService.setReconcileTagBased(tag);
				}
			}
			else if(fieldName.contains("radio_")){
				String [] tableauField =  fieldName.split("_");
				String value = item.getString();
				int idFile = Integer.parseInt(tableauField[tableauField.length-2]);
				int idLine = Integer.parseInt(tableauField[tableauField.length-1]);
				ReconciliationService reconciliationService = listReconcileFiles.get(idFile);
				HashMap<Integer, String> linesConnnectedNewName = reconciliationService.getLineConnectedNewName();
				linesConnnectedNewName.put(idLine, value);
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


		this.initialisation.setListMappingReconcileFiles(listMappingReconcileDWC);


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
	public String getNbSessionRandom() {
		return nbSessionRandom;
	}

	/**
	 * 
	 * @param nbSessionRandom
	 * @return void
	 */
	public void setNbSessionRandom(String nbSessionRandom) {
		this.nbSessionRandom = nbSessionRandom;
	}

	/**
	 * 
	 * @return int
	 */
	public String generateRandomKey(){
		String nbUUID = UUID.randomUUID().toString().replace("-", "_");
		//String nbSessionRandom =  UUID.randomUUID();
		//Random random = new Random();
		//nbFileRandom = random.nextInt();
		//System.out.println(nbFileRandom);
		return nbUUID;
	}

}
