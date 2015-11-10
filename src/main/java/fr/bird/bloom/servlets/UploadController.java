package fr.bird.bloom.servlets;

import fr.bird.bloom.utils.BloomConfig;
import fr.bird.bloom.utils.BloomUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@WebServlet(name = "UploadController")
public class UploadController extends HttpServlet{


	private String getDirectoryPath() {
		if (BloomConfig.getDirectoryPath() == null) {
			BloomConfig.initializeDirectoryPath(getServletContext().getRealPath("/"));
		}
		return BloomConfig.getDirectoryPath();
	}

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		int count = 0;
		String uuid = "";
		String nbInput = "";
		String action = "";

		String firstline = "";		

		List<FileItem> items = null;
		try {
			items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}            
		for (FileItem item : items) {
			DiskFileItem itemFile = (DiskFileItem) item;
			String fileExtensionName = itemFile.getName();
			fileExtensionName = FilenameUtils.getExtension(fileExtensionName);

			if(count == 0){
				uuid = itemFile.getString();
				if(!new File(getDirectoryPath() + "temp/").exists()){
					BloomUtils.createDirectory(getDirectoryPath() + "temp/");
				}
				if(!new File(getDirectoryPath() + "temp/" + uuid + "/").exists()){
					BloomUtils.createDirectory(getDirectoryPath() + "temp/" + uuid + "/");
				}
				if(!new File(getDirectoryPath() + "temp/" + uuid + "/data/").exists()){
					BloomUtils.createDirectory(getDirectoryPath() + "temp/" + uuid + "/data/");
				}
				System.out.println(uuid);
			}
			else if(count == 1 ){
				nbInput = itemFile.getString();
				System.out.println(nbInput);
			}
			else if(count == 2){
				action = itemFile.getString();
				System.out.println(action);
			}
			else if(count == 3){
				System.out.println("format : " + fileExtensionName);
				List<String> compressedFormat = new ArrayList<>();
				compressedFormat.add("zip");
				compressedFormat.add("rar");
				compressedFormat.add("tar.gz");
				
				File file = new File(getDirectoryPath() + "temp/" + uuid + "/data/input_" + nbInput + "_" + uuid + "." + fileExtensionName);
				if("upload".equals(action)){
					try {
						itemFile.write(file);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if("zip".equals(fileExtensionName)){
						String dezipFile = this.unzip(file, getDirectoryPath() + "temp/" + uuid + "/data/");
						System.out.println("dezipfile : " + dezipFile);
					}
					else{
						firstline = this.getFirstLine(file);
						//String fileContained = this.getFileContained(file);
						System.out.println(firstline);
						response.setContentType("application/text");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(firstline);
					}
				}
				else if("cancel".equals(action)){

					file.delete();
					response.getWriter().write("cancelDone");
				}
			}

			count ++;			
		}


	}


	/**
	 * get the file contained
	 * 
	 * @param file
	 * @return String
	 */
	protected String getFileContained(File file){
		String contained = "";

		InputStream inputStreamReference;
		try {
			inputStreamReference = new FileInputStream(file);
			InputStreamReader inputStreamReaderReference = new InputStreamReader(inputStreamReference);
			BufferedReader readerReference = new BufferedReader(inputStreamReaderReference);
			String line = "";
			while ((line = readerReference.readLine()) != null){
				contained += line + "\n";
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return contained;
	}


	/**
	 * get the first line of the file
	 * @param file
	 * @return String
	 */
	protected String getFirstLine(File file){
		String firstLine = "";

		InputStream inputStreamReference;
		try {
			inputStreamReference = new FileInputStream(file);
			InputStreamReader inputStreamReaderReference = new InputStreamReader(inputStreamReference);
			BufferedReader readerReference = new BufferedReader(inputStreamReaderReference);
			String line = "";
			int countLine = 0;
			while ((line = readerReference.readLine()) != null){
				if(countLine == 0){
					firstLine = line;
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 



		return firstLine;
	}


	/**
	 * unzip zip file
	 * @param zipfile
	 * @param folder
	 * @return String filename of unzip file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String unzip(File zipfile, String folder) throws FileNotFoundException, IOException{
		
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipfile.getCanonicalFile())));
		String zipFilename = zipfile.getName().split("/")[zipfile.getName().split("/").length - 1];
		String dezipFilename = "";
		ZipEntry ze = null;
		
		try {
			while((ze = zis.getNextEntry()) != null){
				//dezipFilename = ze.toString();
				dezipFilename = zipFilename.split("\\.")[0] + ".csv";
				File f = new File(folder,dezipFilename );
				if (ze.isDirectory()) {
					f.mkdirs();
					continue;
				}
				

				f.getParentFile().mkdirs();
				OutputStream fos = new BufferedOutputStream(new FileOutputStream(f));

				try {
					try {
						final byte[] buf = new byte[8192];
						int bytesRead;
						while (-1 != (bytesRead = zis.read(buf)))
							fos.write(buf, 0, bytesRead);
					}
					finally {
						fos.close();
					}
				}
				catch (final IOException ioe) {
					f.delete();
					throw ioe;
				}
			}
		}
		finally {
			zis.close();
			zipfile.delete();
		}
		
		return dezipFilename;
	}


}


/*
 ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipfile.getCanonicalFile())));
		String zipFilename = zipfile.getName().split("/")[zipfile.getName().split("/").length - 1];
		String dezipFilename = "";
		ZipEntry ze = null;
		
		try {
			while((ze = zis.getNextEntry()) != null){

				File f = new File(folder, zipFilename);
				System.out.println(f.getAbsolutePath() + "  " + f.getName());
				if (ze.isDirectory()) {
					f.mkdirs();
					continue;
				}
				dezipFilename = zipFilename.split("\\.")[0];
				*/
