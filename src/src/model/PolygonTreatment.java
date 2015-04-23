/**
 * @author mhachet
 */
package src.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class PolygonTreatment {

    private String DIRECTORY_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/";


    /**
     * 
     * src.model
     * PolygonTreatment
     */
    public PolygonTreatment(){

    }

    /**
     * Check if a geospatial point is in a country (polygon or multipolygon)
     * 
     * @param Point geoPoint (com.vividsolutions.jts.geom.Point;)
     * @param File geoJsonFile (format Json)
     * 
     * @return boolean
     */
    public boolean polygonContainedPoint(Point geoPoint, File geoJsonFile){
	String polygonType = this.getPolygoneType(geoJsonFile);
	FileInputStream jsonInput = null;
	boolean isContained = false;

	try {
	    jsonInput = new FileInputStream(geoJsonFile);
	} catch (FileNotFoundException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	GeometryJSON geometryJSON = new GeometryJSON();
	if(polygonType.equals("Polygon")){
	    try {
		Polygon polygon = geometryJSON.readPolygon(jsonInput);
		isContained = polygon.contains(geoPoint);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	if (polygonType.equals("MultiPolygon")){
	    try {
		MultiPolygon multipolygon = geometryJSON.readMultiPolygon(jsonInput);
		isContained = multipolygon.contains(geoPoint);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	return isContained;
    }

    /**
     * Get the polygon type : multipolygon or simple polygon
     * 
     * @param File geoJsonFile
     * 
     * @return String
     */
    public String getPolygoneType(File geoJsonFile){
	String typePolygon = "";
	JSONParser parser = new JSONParser();
	try {
	    Object obj = parser.parse(new FileReader(geoJsonFile.getAbsoluteFile()));
	    JSONObject jsonObjectFeatures = (JSONObject) obj;
	    JSONArray features = (JSONArray) jsonObjectFeatures.get("features");
	    JSONObject firstFeature = (JSONObject) features.get(0);
	    JSONObject geometry = (JSONObject) firstFeature.get("geometry");
	    typePolygon = (String) geometry.get("type");

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return typePolygon;
    }


    /**
     * Find code tdwg4 from coordinates 
     * 
     * @param geoPoint
     * @param iso2
     * @throws IOException
     * @return String
     */
    public String tdwg4ContainedPoint(Point geoPoint, String iso2) throws IOException{
	GeometryJSON geometryJSON = new GeometryJSON();
	BufferedReader buff = new BufferedReader(new FileReader(DIRECTORY_PATH + "src/ressources/tdwg4.json"));

	try {
	    String line = null;
	    while ((line = buff.readLine()) != null) {
		if(line.contains("\"ISO_Code\": \"" + iso2 + "\"")){
		    Pattern pattern = Pattern.compile("\"Level4_cod\": \"([A-Z]{3}-[A-Z]{2})\",");
		    Matcher matcher = pattern.matcher(line);
		    String tdwg4Code = "";
		    while(matcher.find()) {
			tdwg4Code = matcher.group(1);
		    }
		    MultiPolygon multipolygon = geometryJSON.readMultiPolygon(line.substring(0, line.length()-1));
		    if(multipolygon.contains(geoPoint)){

			return tdwg4Code;
		    }
		}		
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} finally {
	    buff.close();
	}
	return "";

    }

}