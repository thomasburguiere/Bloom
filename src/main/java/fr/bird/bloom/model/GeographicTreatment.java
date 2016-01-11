/**
 * src.model
 * GeographicTreatment
 */
package fr.bird.bloom.model;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import fr.bird.bloom.utils.BloomConfig;
import fr.bird.bloom.utils.BloomUtils;
import org.geotools.geojson.geom.GeometryJSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * src.model
 * 
 * GeographicTreatment.java
 * GeographicTreatment
 */
public class GeographicTreatment {

	private String uuid;
	private int nbWrongGeospatialIssues = 0;
	private int nbWrongCoordinates = 0;
	private int nbWrongIso2 = 0;
	private DarwinCore darwinCore;
	private List<String> wrongGeoList;
	private List<String> wrongCoordinatesList;
	private List<String> wrongPolygonList;
	private File wrongGeoFile;
	private File wrongCoordinatesFile;
	private File wrongPolygonFile;

	/**
	 * 
	 * @param darwinCore
	 */
	public GeographicTreatment(DarwinCore darwinCore){
		this.darwinCore = darwinCore;
	}

	/**
	 * 
	 * @return ArrayList<String>
	 */
	public List<String> geoGraphicTreatment(){
		List<String> infosSummary = new ArrayList<>();

		this.deleteWrongIso2();
		this.createTableClean();

		List<String> wrongCoordinates = this.deleteWrongCoordinates();
		this.setWrongCoordinatesList(wrongCoordinates);
		infosSummary.add("error number : " + Integer.toString(wrongCoordinates.size()));

		List<String> wrongGeoSpatial = this.deleteWrongGeospatial();
		this.setWrongGeoList(wrongGeoSpatial);

		List<String> wrongPolygon = this.checkCoordinatesIso2Code();
		this.setWrongPolygonList(wrongPolygon);

		return infosSummary;
	}

	/**
	 * Check if coordinates (latitude and longitude) are included in the country indicated by the iso2 code
	 * 
	 * @return void
	 */
	public List<String> checkCoordinatesIso2Code(){


		//this.getDarwinCore().associateIdData();

		List<String> listToDelete = new ArrayList<>();

		//Map<String, List<String>> idAssoData = this.getDarwinCore().getIdAssoData();
		final String resourcePath = BloomConfig.getResourcePath();
		List<String> idList = this.getDarwinCore().getID();
		//int iLatitude = this.getDarwinCore().getIndiceFromTag("decimalLatitude_");
		//int iLongitude = this.getDarwinCore().getIndiceFromTag("decimalLongitude_");
		//int iIso2 = this.getDarwinCore().getIndiceFromTag("countryCode_");
		//int iGbifID = this.getDarwinCore().getIndiceFromTag("gbifID_");

		int nbWrongIso2 = 0;
		List<String> listIDtoDelete = new ArrayList<>();
		for(int i = 0 ; i< idList.size() ; i++){
			String id_ = idList.get(i);
			//System.out.println(id_);
		//for (String id_ : idAssoData.keySet()) {
			//System.out.println(id_);
			if(!"id_".equals(id_ )){
				//List<String> listInfos = idAssoData.get(id_);
				boolean errorIso = true;
				boolean errorCoord = false;
				float latitude = -1;
				float longitude = -1;
				String iso2 = "error";
				String gbifId_ = "error";
				String iso3 = "error";

				String valueLatitude = this.getDarwinCore().getValueFromColumn("decimalLatitude_", id_.replaceAll("\"", ""));
				System.err.println("decimalLatitude : " + valueLatitude);
				if(!valueLatitude.equals("error")){
					try {
						latitude = Float.parseFloat(valueLatitude.replaceAll("\"", ""));
					}
					catch(NumberFormatException ex) {
						errorCoord = true;
					}
				}


				String valueLongitude = this.getDarwinCore().getValueFromColumn("decimalLongitude_", id_.replaceAll("\"", ""));
				if(!valueLongitude.equals("error")){
					try {
						longitude = Float.parseFloat(valueLongitude.replaceAll("\"", ""));
					}
					catch(NumberFormatException ex) {
						errorCoord = true;
					}
				}

				iso2 = this.getDarwinCore().getValueFromColumn("countryCode_", id_.replaceAll("\"", "")).replaceAll("\"", "");
				//gbifId_ = this.getDarwinCore().getValueFromColumn("gbifID_", id_.replaceAll("\"", "")).replaceAll("\"", "");


				if(!iso2.equals("error") && !errorCoord){
					iso3 = this.convertIso2ToIso3(iso2);

					/*
					try {
						latitude = Float.parseFloat(listInfos.get(iLatitude).replace("\"", ""));
					}
					catch (NumberFormatException nfe){
						System.err.println(listInfos.get(iLatitude).replace("\"", ""));
					}
					longitude = Float.parseFloat(listInfos.get(iLongitude).replace("\"", ""));
					iso2 = listInfos.get(iIso2);
					*/

					//gbifId_ = listInfos.get(iGbifID);
					if(!iso3.equals("error")){
						File geoJsonFile = new File(resourcePath + "gadm_json/" + iso3.toUpperCase() + "_adm0.json");
						GeometryFactory geometryFactory = new GeometryFactory();
						Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
						System.out.println("--------------------------------------------------------------");
						System.out.println("------------------ Check point in polygon --------------------");
						System.out.println("Lat : " + latitude + "\tLong : " +  longitude);
						System.out.println("id_ : " + id_ + "\tIso3 : " + iso3 + "\tiso2 : " + iso2);

						boolean isContained = this.polygonContainedPoint(point, geoJsonFile);

						System.out.println("The point is contained in the polygone : " + isContained);
						System.out.println("--------------------------------------------------------------\n");


						if(!isContained){
							errorIso = true;
							//nbWrongIso2 ++;
							//listIDtoDelete.add(id_);
						}
						else{
							errorIso = false;
						}
					}
					else{
						errorIso = true;
					}
				}
				else{
					errorIso = true;
				}

				if(errorIso){
					nbWrongIso2 ++;
					listIDtoDelete.add(id_);
				}
			}
		}
		if(listIDtoDelete.size() > 0) {
			String sqlIDCleanToSelect = "SELECT abstract_,acceptedNameUsage_,acceptedNameUsageID_,accessRights_,accrualMethod_,accrualPeriodicity_,accrualPolicy_," +
					"alternative_,associatedMedia_,associatedOccurrences_,associatedOrganisms_,associatedReferences_,associatedSequences_,associatedTaxa_,audience_," +
					"available_,basisOfRecord_,bed_,behavior_,bibliographicCitation_,catalogNumber_,class_,classKey_,collectionCode_,collectionID_,conformsTo_,continent_," +
					"contributor_,coordinateAccuracy_,coordinatePrecision_,coordinateUncertaintyInMeters_,country_,countryCode_,county_,coverage_,created_,creator_," +
					"dataGeneralizations_,datasetID_,datasetKey_,datasetName_,date_,dateAccepted_,dateCopyrighted_,dateIdentified_,dateSubmitted_,day_,decimalLatitude_," +
					"decimalLongitude_,depth_,depthAccuracy_,description_,disposition_,distanceAboveSurface_,distanceAboveSurfaceAccuracy_,dynamicProperties_," +
					"earliestAgeOrLowestStage_,earliestEonOrLowestEonothem_,earliestEpochOrLowestSeries_,earliestEraOrLowestErathem_,earliestPeriodOrLowestSystem_," +
					"educationLevel_,elevation_,elevationAccuracy_,endDayOfYear_,establishmentMeans_,event_,eventDate_,eventID_,eventRemarks_,eventTime_,extent_,family_," +
					"familyKey_,fieldNotes_,fieldNumber_,footprintSpatialFit_,footprintSRS_,footprintWKT_,format_,formation_,gbifID_,genericName_,genus_,genusKey_," +
					"geodeticDatum_,geologicalContext_,geologicalContextID_,georeferencedBy_,georeferencedDate_,georeferenceProtocol_,georeferenceRemarks_," +
					"georeferenceSources_,georeferenceVerificationStatus_,group_,habitat_,hasCoordinate_,hasFormat_,hasGeospatialIssues_,hasPart_,hasVersion_," +
					"higherClassification_,higherGeography_,higherGeographyID_,highestBiostratigraphicZone_,identification_,identificationID_,identificationQualifier_," +
					"identificationReferences_,identificationRemarks_,identificationVerificationStatus_,identifiedBy_,identifier_,idFile_,individualCount_,individualID_," +
					"informationWithheld_,infraspecificEpithet_,institutionCode_,institutionID_,instructionalMethod_,isFormatOf_,island_,islandGroup_,isPartOf_," +
					"isReferencedBy_,isReplacedBy_,isRequiredBy_,issue_,issued_,isVersionOf_,kingdom_,kingdomKey_,language_,lastCrawled_,lastInterpreted_,lastParsed_," +
					"latestAgeOrHighestStage_,latestEonOrHighestEonothem_,latestEpochOrHighestSeries_,latestEraOrHighestErathem_,latestPeriodOrHighestSystem_,license_," +
					"lifeStage_,lithostratigraphicTerms_,livingSpecimen_,locality_,locationAccordingTo_,locationID_,locationRemarks_,lowestBiostratigraphicZone_," +
					"machineObservation_,materialSample_,materialSampleID_,maximumDepthinMeters_,maximumDistanceAboveSurfaceInMeters_,maximumElevationInMeters_," +
					"measurementAccuracy_,measurementDeterminedBy_,measurementDeterminedDate_,measurementID_,measurementMethod_,measurementOrFact_,measurementRemarks_," +
					"measurementType_,measurementUnit_,mediator_,mediaType_,medium_,member_,minimumDepthinMeters_,minimumDistanceAboveSurfaceInMeters_," +
					"minimumElevationInMeters_,modified_,month_,municipality_,nameAccordingTo_,nameAccordingToID_,namePublishedIn_,namePublishedInID_,namePublishedInYear_," +
					"nomenclaturalCode_,nomenclaturalStatus_,occurrence_,occurrenceDetails_,occurrenceID_,occurrenceRemarks_,occurrenceStatus_,order_,orderKey_,organism_," +
					"organismID_,organismName_,organismRemarks_,organismScope_,originalNameUsage_,originalNameUsageID_,otherCatalogNumbers_,ownerInstitutionCode_," +
					"parentNameUsage_,parentNameUsageID_,phylum_,phylumKey_,pointRadiusSpatialFit_,preparations_,preservedSpecimen_,previousIdentifications_,protocol_," +
					"provenance_,publisher_,publishingCountry_,recordedBy_,recordNumber_,references_,relatedResourceID_,relationshipAccordingTo_," +
					"relationshipEstablishedDate_,relationshipRemarks_,relation_,replaces_,reproductiveCondition_,requires_,resourceID_,resourceRelationship_," +
					"resourceRelationshipID_,rights_,rightsHolder_,samplingEffort_,samplingProtocol_,scientificName_,scientificNameAuthorship_,scientificNameID_,sex_," +
					"source_,spatial_,species_,speciesKey_,specificEpithet_,startDayOfYear_,stateProvince_,subgenus_,subgenusKey_,subject_,tableOfContents_,taxon_," +
					"taxonConceptID_,taxonID_,taxonKey_,taxonomicStatus_,taxonRank_,taxonRemarks_,temporal_,title_,type_,typeStatus_,typifiedName_,valid_," +
					"verbatimCoordinates_,verbatimCoordinateSystem_,verbatimDate_,verbatimDepth_,verbatimElevation_,verbatimEventDate_,verbatimLatitude_," +
					"verbatimLocality_,verbatimLongitude_,verbatimSRS_,verbatimTaxonRank_,vernacularName_,waterBody_,year_ FROM Workflow.Clean_" + this.getUuid() +
					" WHERE Clean_" + this.getUuid() + ".id_=";

			String sqlIDCleanToDelete = "DELETE FROM Workflow.Clean_" + this.getUuid() + " WHERE id_=";
			for (int l = 0; l < listIDtoDelete.size(); l++) {
				if (l != listIDtoDelete.size() - 1) {
					sqlIDCleanToDelete += listIDtoDelete.get(l) + " OR id_=";
					sqlIDCleanToSelect += listIDtoDelete.get(l) + " OR Clean_" + this.getUuid() + ".id_=";
				} else {
					sqlIDCleanToDelete += listIDtoDelete.get(l) + ";";
					sqlIDCleanToSelect += listIDtoDelete.get(l) + ";";
				}
			}
			Statement statement = null;
			try {
				statement = ConnectionDatabase.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DatabaseTreatment newConnectionSelectID = new DatabaseTreatment(statement);
			List<String> messagesSelectID = new ArrayList<>();
			//String sqlSelectID =  + id_ + ";";
			messagesSelectID.add("\n--- Select wrong matching between polygon and Iso2 code ---\n");
			messagesSelectID.addAll(newConnectionSelectID.executeSQLcommand("executeQuery", sqlIDCleanToSelect));
			//messagesSelectID.add(sqlIDCleanToSelect);
			for (int j = 0; j < messagesSelectID.size(); j++) {
				System.out.println(messagesSelectID.get(j));
			}

			List<String> selectIDResults = newConnectionSelectID.getResultatSelect();
			for (int k = 0; k < selectIDResults.size(); k++) {
				if (!listToDelete.contains(selectIDResults.get(k))) {
					listToDelete.add(selectIDResults.get(k));
				}
			}
			Statement statementDelete = null;
			try {
				statementDelete = ConnectionDatabase.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DatabaseTreatment newConnectionDeleteID = new DatabaseTreatment(statementDelete);
			List<String> messagesDeleteID = new ArrayList<>();
			//String sqlDeleteID = "DELETE FROM Workflow.Clean_" + this.getUuid() + " WHERE id_=" + id_ + ";";
			messagesDeleteID.add("\n--- Delete wrong matching between polygon and Iso2 code ---\n");
			messagesDeleteID.addAll(newConnectionDeleteID.executeSQLcommand("executeUpdate", sqlIDCleanToDelete));
			List<String> deleteIDResults = newConnectionDeleteID.getResultatSelect();
			messagesDeleteID.add("nb lignes affectées :" + listToDelete.size());
			for (int i = 0; i < messagesDeleteID.size(); i++) {
				System.out.println(messagesDeleteID.get(i));
			}

		}


		this.setNbWrongIso2(nbWrongIso2);

		return listToDelete;
	}

	/**
	 * Convert the iso2 code (2 letters) to iso3 code (3 letters)
	 * 
	 * @param String iso2
	 * @return String iso3
	 */
	public String convertIso2ToIso3(String iso2){
		String iso3 = "";

		Statement statement = null;
		try {
			statement = ConnectionDatabase.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseTreatment newConnection = new DatabaseTreatment(statement);

		List<String> messages = new ArrayList<>();
		messages.add("\n--- Convert iso2 code to iso3 code ---");
		String sqlConvertIso2Iso3 = "SELECT iso3_ FROM Workflow.IsoCode WHERE iso2_ = \"" + iso2.replaceAll("\"", "") + "\";";
		messages.addAll(newConnection.executeSQLcommand("executeQuery", sqlConvertIso2Iso3));

		List<String> resultatConvert = newConnection.getResultatSelect();

		if(resultatConvert.size() != 2){
			//System.err.println(iso2 + "\t" + resultatConvert + "\t" + listInfos);
			System.err.println("error to convert iso2 to iso3.\n Iso2 : " + iso2);
			iso3 = "error";
		}
		else{
			iso3 = resultatConvert.get(1).replaceAll("\"", "");
		}
		return iso3;
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
			System.out.println(geoJsonFile.getAbsolutePath());
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		GeometryJSON geometryJSON = new GeometryJSON();
		if("Polygon".equals(polygonType)){
			try {
				Polygon polygon = geometryJSON.readPolygon(jsonInput);
				isContained = polygon.contains(geoPoint);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if ("MultiPolygon".equals(polygonType)){
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

	//Vérifier que le code iso2 existe et qu'il est bien inscrit dans la table IsoCode !!!
	/**
	 * Create temporary table "temp" with only correct iso2 code in DarwinCoreInput table.
	 * Iso2 code (countryCode_) is correct if it's contained in IsoCode table (iso2_).
	 * 
	 * @return void
	 */
	public void deleteWrongIso2() {
		Statement statement = null;
		try {
			statement = ConnectionDatabase.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseTreatment newConnectionTemp = new DatabaseTreatment(statement);
		List<String> messages = new ArrayList<>();
		String choiceStatement = "executeUpdate";
		messages.add("\n--- Create temporary table with correct ISO2 ---");
		String sqlCreateTemp = "CREATE TABLE Workflow.temp_" + this.getUuid() + " AS SELECT DarwinCoreInput.* FROM Workflow.DarwinCoreInput,Workflow.IsoCode WHERE countryCode_=IsoCode.iso2_ AND UUID_=\"" + this.getUuid() + "\";";
		System.out.println(sqlCreateTemp);
		messages.addAll(newConnectionTemp.executeSQLcommand(choiceStatement, sqlCreateTemp));

		for(int i = 0 ; i < messages.size() ; i++){
			System.out.println(messages.get(i));
		}
	}

	/**
	 * From temp table, create a Clean table with correct geospatial coordinates :
	 * -90 >= latitude > 0
	 *  0 < latitude <= 90
	 *  
	 *  -180 >= longitude > 0
	 *   0 < longitude <= 180
	 *   locationID_
	 *   tag "hasGeospatialIssues" = false
	 *   
	 *  @return void
	 */
	public void createTableClean(){
		Statement statement = null;
		try {
			statement = ConnectionDatabase.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseTreatment newConnectionClean = new DatabaseTreatment(statement);
		List<String> messages = new ArrayList<>();
		String choiceStatement = "executeUpdate";
		messages.add("\n--- Create Table Clean from temporary table ---");
		String sqlCreateClean = "CREATE TABLE Workflow.Clean_" + this.getUuid() + " AS SELECT * FROM Workflow.temp_" + this.getUuid() + " WHERE " +
				"(decimalLatitude_!=0 AND decimalLatitude_<90 AND decimalLatitude_>-90 AND decimalLongitude_!=0 " +
				"AND decimalLongitude_>-180 AND decimalLongitude_<180) AND ((hasGeospatialIssues_!=\"true\") OR (hasGeospatialIssues_ IS NULL));";
		messages.addAll(newConnectionClean.executeSQLcommand(choiceStatement, sqlCreateClean));

		for(int i = 0 ; i < messages.size() ; i++){
			System.out.println(messages.get(i));
		}
	}

	/**
	 * Select wrong coordinates and write in a file:
	 * latitude = 0 ; <-90 ; >90
	 * longitude = 0 ; <-180 ; >180
	 * 
	 * @return File wrong coordinates
	 */
	public List<String> deleteWrongCoordinates(){
		Statement statement = null;
		try {
			statement = ConnectionDatabase.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseTreatment newConnection = new DatabaseTreatment(statement);

		List<String> messages = new ArrayList<>();
		messages.add("\n--- Select wrong coordinates ---");

		String sqlRetrieveWrongCoord = "SELECT abstract_,acceptedNameUsage_,acceptedNameUsageID_,accessRights_,accrualMethod_,accrualPeriodicity_,accrualPolicy_,alternative_,associatedMedia_,associatedOccurrences_,associatedOrganisms_,associatedReferences_,associatedSequences_,associatedTaxa_,audience_,available_,basisOfRecord_,bed_,behavior_,bibliographicCitation_,catalogNumber_,class_,classKey_,collectionCode_,collectionID_,conformsTo_,continent_,contributor_,coordinateAccuracy_,coordinatePrecision_,coordinateUncertaintyInMeters_,country_,countryCode_,county_,coverage_,created_,creator_,dataGeneralizations_,datasetID_,datasetKey_,datasetName_,date_,dateAccepted_,dateCopyrighted_,dateIdentified_,dateSubmitted_,day_,decimalLatitude_,decimalLongitude_,depth_,depthAccuracy_,description_,disposition_,distanceAboveSurface_,distanceAboveSurfaceAccuracy_,dynamicProperties_,earliestAgeOrLowestStage_,earliestEonOrLowestEonothem_,earliestEpochOrLowestSeries_,earliestEraOrLowestErathem_,earliestPeriodOrLowestSystem_,educationLevel_,elevation_,elevationAccuracy_,endDayOfYear_,establishmentMeans_,event_,eventDate_,eventID_,eventRemarks_,eventTime_,extent_,family_,familyKey_,fieldNotes_,fieldNumber_,footprintSpatialFit_,footprintSRS_,footprintWKT_,format_,formation_,gbifID_,genericName_,genus_,genusKey_,geodeticDatum_,geologicalContext_,geologicalContextID_,georeferencedBy_,georeferencedDate_,georeferenceProtocol_,georeferenceRemarks_,georeferenceSources_,georeferenceVerificationStatus_,group_,habitat_,hasCoordinate_,hasFormat_,hasGeospatialIssues_,hasPart_,hasVersion_,higherClassification_,higherGeography_,higherGeographyID_,highestBiostratigraphicZone_,identification_,identificationID_,identificationQualifier_,identificationReferences_,identificationRemarks_,identificationVerificationStatus_,identifiedBy_,identifier_,idFile_,individualCount_,individualID_,informationWithheld_,infraspecificEpithet_,institutionCode_,institutionID_,instructionalMethod_,isFormatOf_,island_,islandGroup_,isPartOf_,isReferencedBy_,isReplacedBy_,isRequiredBy_,issue_,issued_,isVersionOf_,kingdom_,kingdomKey_,language_,lastCrawled_,lastInterpreted_,lastParsed_,latestAgeOrHighestStage_,latestEonOrHighestEonothem_,latestEpochOrHighestSeries_,latestEraOrHighestErathem_,latestPeriodOrHighestSystem_,license_,lifeStage_,lithostratigraphicTerms_,livingSpecimen_,locality_,locationAccordingTo_,locationID_,locationRemarks_,lowestBiostratigraphicZone_,machineObservation_,materialSample_,materialSampleID_,maximumDepthinMeters_,maximumDistanceAboveSurfaceInMeters_,maximumElevationInMeters_,measurementAccuracy_,measurementDeterminedBy_,measurementDeterminedDate_,measurementID_,measurementMethod_,measurementOrFact_,measurementRemarks_,measurementType_,measurementUnit_,mediator_,mediaType_,medium_,member_,minimumDepthinMeters_,minimumDistanceAboveSurfaceInMeters_,minimumElevationInMeters_,modified_,month_,municipality_,nameAccordingTo_,nameAccordingToID_,namePublishedIn_,namePublishedInID_,namePublishedInYear_,nomenclaturalCode_,nomenclaturalStatus_,occurrence_,occurrenceDetails_,occurrenceID_,occurrenceRemarks_,occurrenceStatus_,order_,orderKey_,organism_,organismID_,organismName_,organismRemarks_,organismScope_,originalNameUsage_,originalNameUsageID_,otherCatalogNumbers_,ownerInstitutionCode_,parentNameUsage_,parentNameUsageID_,phylum_,phylumKey_,pointRadiusSpatialFit_,preparations_,preservedSpecimen_,previousIdentifications_,protocol_,provenance_,publisher_,publishingCountry_,recordedBy_,recordNumber_,references_,relatedResourceID_,relationshipAccordingTo_,relationshipEstablishedDate_,relationshipRemarks_,relation_,replaces_,reproductiveCondition_,requires_,resourceID_,resourceRelationship_,resourceRelationshipID_,rights_,rightsHolder_,samplingEffort_,samplingProtocol_,scientificName_,scientificNameAuthorship_,scientificNameID_,sex_,source_,spatial_,species_,speciesKey_,specificEpithet_,startDayOfYear_,stateProvince_,subgenus_,subgenusKey_,subject_,tableOfContents_,taxon_,taxonConceptID_,taxonID_,taxonKey_,taxonomicStatus_,taxonRank_,taxonRemarks_,temporal_,title_,type_,typeStatus_,typifiedName_,valid_,verbatimCoordinates_,verbatimCoordinateSystem_,verbatimDate_,verbatimDepth_,verbatimElevation_,verbatimEventDate_,verbatimLatitude_,verbatimLocality_,verbatimLongitude_,verbatimSRS_,verbatimTaxonRank_,vernacularName_,waterBody_,year_ FROM Workflow.DarwinCoreInput WHERE (UUID_=\"" + this.getUuid() + "\") AND (decimalLatitude_=0 OR decimalLatitude_>90 OR decimalLatitude_<-90 OR decimalLongitude_=0 OR decimalLongitude_>180 OR decimalLongitude_<-180);";
		messages.addAll(newConnection.executeSQLcommand("executeQuery", sqlRetrieveWrongCoord));
		List<String> resultatSelect = newConnection.getResultatSelect();
		messages.add("nb lignes affectées :" + Integer.toString(resultatSelect.size() - 1));


		if (!new File(BloomConfig.getDirectoryPath() + "temp/" + this.getUuid() + "/wrong/").exists()) {
			BloomUtils.createDirectory(BloomConfig.getDirectoryPath() + "temp/" + this.getUuid() + "/wrong/");
		}




		for(int j = 0 ; j < messages.size() ; j++){
			if(messages.get(j).contains("nb lignes affectées")){
				this.setNbWrongCoordinates(Integer.parseInt(messages.get(j).split(":")[1]));
			}
			System.out.println(messages.get(j));
		}

		this.setNbWrongCoordinates(resultatSelect.size()-1);

		return resultatSelect;
	}


	/**
	 * Select wrong geospatial and write in a file :
	 * tag "hasGeospatialIssues_" = true
	 * 
	 * @return File wrong geospatial
	 */
	public List<String> deleteWrongGeospatial(){
		Statement statement = null;
		try {
			statement = ConnectionDatabase.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DatabaseTreatment newConnection = new DatabaseTreatment(statement);

		List<String> messages = new ArrayList<>();
		messages.add("\n--- Select wrong geospatialIssues ---");

		String sqlRetrieveWrongGeo = "SELECT abstract_,acceptedNameUsage_,acceptedNameUsageID_,accessRights_,accrualMethod_,accrualPeriodicity_,accrualPolicy_,alternative_,associatedMedia_,associatedOccurrences_,associatedOrganisms_,associatedReferences_,associatedSequences_,associatedTaxa_,audience_,available_,basisOfRecord_,bed_,behavior_,bibliographicCitation_,catalogNumber_,class_,classKey_,collectionCode_,collectionID_,conformsTo_,continent_,contributor_,coordinateAccuracy_,coordinatePrecision_,coordinateUncertaintyInMeters_,country_,countryCode_,county_,coverage_,created_,creator_,dataGeneralizations_,datasetID_,datasetKey_,datasetName_,date_,dateAccepted_,dateCopyrighted_,dateIdentified_,dateSubmitted_,day_,decimalLatitude_,decimalLongitude_,depth_,depthAccuracy_,description_,disposition_,distanceAboveSurface_,distanceAboveSurfaceAccuracy_,dynamicProperties_,earliestAgeOrLowestStage_,earliestEonOrLowestEonothem_,earliestEpochOrLowestSeries_,earliestEraOrLowestErathem_,earliestPeriodOrLowestSystem_,educationLevel_,elevation_,elevationAccuracy_,endDayOfYear_,establishmentMeans_,event_,eventDate_,eventID_,eventRemarks_,eventTime_,extent_,family_,familyKey_,fieldNotes_,fieldNumber_,footprintSpatialFit_,footprintSRS_,footprintWKT_,format_,formation_,gbifID_,genericName_,genus_,genusKey_,geodeticDatum_,geologicalContext_,geologicalContextID_,georeferencedBy_,georeferencedDate_,georeferenceProtocol_,georeferenceRemarks_,georeferenceSources_,georeferenceVerificationStatus_,group_,habitat_,hasCoordinate_,hasFormat_,hasGeospatialIssues_,hasPart_,hasVersion_,higherClassification_,higherGeography_,higherGeographyID_,highestBiostratigraphicZone_,identification_,identificationID_,identificationQualifier_,identificationReferences_,identificationRemarks_,identificationVerificationStatus_,identifiedBy_,identifier_,idFile_,individualCount_,individualID_,informationWithheld_,infraspecificEpithet_,institutionCode_,institutionID_,instructionalMethod_,isFormatOf_,island_,islandGroup_,isPartOf_,isReferencedBy_,isReplacedBy_,isRequiredBy_,issue_,issued_,isVersionOf_,kingdom_,kingdomKey_,language_,lastCrawled_,lastInterpreted_,lastParsed_,latestAgeOrHighestStage_,latestEonOrHighestEonothem_,latestEpochOrHighestSeries_,latestEraOrHighestErathem_,latestPeriodOrHighestSystem_,license_,lifeStage_,lithostratigraphicTerms_,livingSpecimen_,locality_,locationAccordingTo_,locationID_,locationRemarks_,lowestBiostratigraphicZone_,machineObservation_,materialSample_,materialSampleID_,maximumDepthinMeters_,maximumDistanceAboveSurfaceInMeters_,maximumElevationInMeters_,measurementAccuracy_,measurementDeterminedBy_,measurementDeterminedDate_,measurementID_,measurementMethod_,measurementOrFact_,measurementRemarks_,measurementType_,measurementUnit_,mediator_,mediaType_,medium_,member_,minimumDepthinMeters_,minimumDistanceAboveSurfaceInMeters_,minimumElevationInMeters_,modified_,month_,municipality_,nameAccordingTo_,nameAccordingToID_,namePublishedIn_,namePublishedInID_,namePublishedInYear_,nomenclaturalCode_,nomenclaturalStatus_,occurrence_,occurrenceDetails_,occurrenceID_,occurrenceRemarks_,occurrenceStatus_,order_,orderKey_,organism_,organismID_,organismName_,organismRemarks_,organismScope_,originalNameUsage_,originalNameUsageID_,otherCatalogNumbers_,ownerInstitutionCode_,parentNameUsage_,parentNameUsageID_,phylum_,phylumKey_,pointRadiusSpatialFit_,preparations_,preservedSpecimen_,previousIdentifications_,protocol_,provenance_,publisher_,publishingCountry_,recordedBy_,recordNumber_,references_,relatedResourceID_,relationshipAccordingTo_,relationshipEstablishedDate_,relationshipRemarks_,relation_,replaces_,reproductiveCondition_,requires_,resourceID_,resourceRelationship_,resourceRelationshipID_,rights_,rightsHolder_,samplingEffort_,samplingProtocol_,scientificName_,scientificNameAuthorship_,scientificNameID_,sex_,source_,spatial_,species_,speciesKey_,specificEpithet_,startDayOfYear_,stateProvince_,subgenus_,subgenusKey_,subject_,tableOfContents_,taxon_,taxonConceptID_,taxonID_,taxonKey_,taxonomicStatus_,taxonRank_,taxonRemarks_,temporal_,title_,type_,typeStatus_,typifiedName_,valid_,verbatimCoordinates_,verbatimCoordinateSystem_,verbatimDate_,verbatimDepth_,verbatimElevation_,verbatimEventDate_,verbatimLatitude_,verbatimLocality_,verbatimLongitude_,verbatimSRS_,verbatimTaxonRank_,vernacularName_,waterBody_,year_ FROM Workflow.DarwinCoreInput WHERE (UUID_=\"" + this.getUuid() + "\") AND hasGeospatialIssues_='true' AND !(decimalLatitude_=0 OR decimalLatitude_>90 OR decimalLatitude_<-90 OR decimalLongitude_=0 OR decimalLongitude_>180 OR decimalLongitude_<-180);";
		messages.addAll(newConnection.executeSQLcommand("executeQuery", sqlRetrieveWrongGeo));

		List<String> resultatSelect = newConnection.getResultatSelect();

		messages.add("nb lignes affectées : " + Integer.toString(resultatSelect.size() - 1));

		this.setNbWrongGeospatialIssues(resultatSelect.size() - 1 );

		if(!new File(BloomConfig.getDirectoryPath() + "temp/" + this.getUuid() + "/wrong/").exists()) {
			BloomUtils.createDirectory(BloomConfig.getDirectoryPath() + "temp/" + this.getUuid() + "/wrong/");
		}



		for(int j = 0 ; j < messages.size() ; j++){
			System.out.println(messages.get(j));
		}

		this.setNbWrongGeospatialIssues(resultatSelect.size()-1);

		return resultatSelect;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getNbWrongGeospatialIssues() {
		return nbWrongGeospatialIssues;
	}

	public void setNbWrongGeospatialIssues(int nbWrongGeospatialIssues) {
		this.nbWrongGeospatialIssues = nbWrongGeospatialIssues;
	}

	public int getNbWrongCoordinates() {
		return nbWrongCoordinates;
	}

	public void setNbWrongCoordinates(int nbWrongCoordinates) {
		this.nbWrongCoordinates = nbWrongCoordinates;
	}

	public int getNbWrongIso2() {
		return nbWrongIso2;
	}

	public void setNbWrongIso2(int nbWrongIso2) {
		this.nbWrongIso2 = nbWrongIso2;
	}

	public DarwinCore getDarwinCore() {
		return darwinCore;
	}

	public void setDarwinCore(DarwinCore darwinCore) {
		this.darwinCore = darwinCore;
	}

	public List<String> getWrongGeoList() {
		return wrongGeoList;
	}

	public void setWrongGeoList(List<String> wrongGeoList) {
		this.wrongGeoList = wrongGeoList;
	}

	public List<String> getWrongCoordinatesList() {
		return wrongCoordinatesList;
	}

	public void setWrongCoordinatesList(List<String> wrongCoordinatesList) {
		this.wrongCoordinatesList = wrongCoordinatesList;
	}

	public List<String> getWrongPolygonList() {
		return wrongPolygonList;
	}

	public void setWrongPolygonList(List<String> wrongPolygonList) {
		this.wrongPolygonList = wrongPolygonList;
	}

	public File getWrongGeoFile() {
		return wrongGeoFile;
	}

	public void setWrongGeoFile(File wrongGeoFile) {
		this.wrongGeoFile = wrongGeoFile;
	}

	public File getWrongCoordinatesFile() {
		return wrongCoordinatesFile;
	}

	public void setWrongCoordinatesFile(File wrongCoordinatesFile) {
		this.wrongCoordinatesFile = wrongCoordinatesFile;
	}

	public File getWrongPolygonFile() {
		return wrongPolygonFile;
	}

	public void setWrongPolygonFile(File wrongPolygonFile) {
		this.wrongPolygonFile = wrongPolygonFile;
	}


}
