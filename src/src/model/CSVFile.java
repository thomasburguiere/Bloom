/**
 * src.model
 * CSVFile
 * TODO
 */
package src.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * src.model
 * 
 * CSVFile.java
 * CSVFile
 */
public class CSVFile {

    public final static List<String> AVAILABLE_SEPARATORS = Collections.unmodifiableList(new ArrayList<String>(Arrays.asList(",", ";", "\t")));
    protected String separator;
    protected String csvName;
    protected File csvFile;
    protected ArrayList<String> lines;
    
    public CSVFile(File file) {
	this.csvFile = file;
	
	try {
	    this.readCsvFile();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	this.findSeparator();
    }
    
    public String getSeparator(){
	return this.separator;
    }
    
    public void setSeparator(String separator) {
        this.separator = separator;
    }
    
    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }

    public File getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(File csvFile) {
        this.csvFile = csvFile;
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    public void setLines(ArrayList<String> lines) {
        this.lines = lines;
    }   
   
    public void findSeparator(){
	int previous = 0;
	List<String> reste = new ArrayList<String>();
	
        boolean isGoodCandidate = false;
	
        for (String sep : AVAILABLE_SEPARATORS) {
	    for(int i = 0 ; i < lines.size() ; i++){
		String line = lines.get(i);
		int compte = this.compterSeperateurs(line, sep);
		if (compte == 0) {
                    // pas de séparateur dans cette ligne
                    isGoodCandidate = false;
                    break;
                }
                if (compte != previous && previous != 0) {
                    // pas le même nombre de séparateurs que la ligne précédente
                    isGoodCandidate = false;
                    break;
                }

                previous = compte;
                isGoodCandidate = true;
            }
	    if (isGoodCandidate) {
                reste.add(sep);
            }
        }

        if (reste.isEmpty()) {
            // Exception ? aucun candidat
            System.out.println("No separator found !");
        }

        else if (reste.size() > 1) {
            // Exception ? trop de candidats
            System.out.println("Too many separators found");
        }	
        else{
            this.setSeparator(reste.get(0));
            this.separator = this.getSeparator();
            System.out.println("Separator is : " + this.getSeparator());
        }
    }
    
    public void readCsvFile() throws IOException{
	lines = new ArrayList<String>();
	FileReader fr = new FileReader(this.csvFile);
	BufferedReader br = new BufferedReader(fr);

	for (String line = br.readLine(); line != null; line = br.readLine()) {
	    lines.add(line);
	}
	
	br.close();
	fr.close();
    }
    
    public int compterSeperateurs(String line, String separator) {
        int number = 0;

        int pos = line.indexOf(separator);
        while (pos != -1) {
            number++;
            line = line.substring(pos + 1);
            pos = line.indexOf(separator);
        }
        return number;
    }
}
