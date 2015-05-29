/**
 * src.test
 * TesttreatmentData
 */
package src.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import src.model.CSVFile;
import src.model.MappingDwC;

/**
 * src.test
 * 
 * TesttreatmentData.java
 * TesttreatmentData
 */


public class TestTreatmentData {
    //http://java.sun.com/jsp/jstl/core
    //<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


    private String DIRECTORY_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/src/ressources/test/";
    private String RESSOURCES_PATH = "/home/mhachet/workspace/WebWorkflowCleanData/src/ressources/";
    
    /**
     * Test method for {@link src.model.TreatmentData#mappingDwC(src.model.MappingDwC)}.
     */
    @Test
    public void testMappingDwC() {
	MappingDwC mappingDWC = new MappingDwC(new CSVFile(new File(RESSOURCES_PATH + "test/data/test_ipt.csv")), true);
	if(mappingDWC.getNoMappedFile().getSeparator() == ""){
	    fail("No separator found");
	}
	
	if(mappingDWC.getNoMappedFile() == null){
	    fail("No file found");
	}
		
    }

    /**
     * Test method for {@link src.model.TreatmentData#initialiseFile(java.io.File, int)}.
     */
    @Test
    public void testInitialiseFile() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#createTemporaryFile(java.util.List, int)}.
     */
    @Test
    public void testCreateTemporaryFile() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#createSQLInsert(java.io.File, java.util.List)}.
     */
    @Test
    public void testCreateSQLInsert() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#createTableDarwinCoreInput(java.lang.String)}.
     */
    @Test
    public void testCreateTableDarwinCoreInput() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#deleteWrongIso2()}.
     */
    @Test
    public void testDeleteWrongIso2() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#createTableClean()}.
     */
    @Test
    public void testCreateTableClean() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#deleteWrongCoordinates()}.
     */
    @Test
    public void testDeleteWrongCoordinates() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#deleteWrongGeospatial()}.
     */
    @Test
    public void testDeleteWrongGeospatial() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#includeSynonyms(java.io.File)}.
     */
    @Test
    public void testIncludeSynonyms() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#getIndiceFromTag(java.lang.String)}.
     */
    @Test
    public void testGetIndiceFromTag() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#getPolygonTreatment()}.
     */
    @Test
    public void testGetPolygonTreatment() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#createFileCsv(java.util.ArrayList, java.lang.String)}.
     */
    @Test
    public void testCreateFileCsv() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#convertIso2ToIso3(java.lang.String)}.
     */
    @Test
    public void testConvertIso2ToIso3() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#checkIsoTdwgCode()}.
     */
    @Test
    public void testCheckIsoTdwgCode() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#checkWorldClimCell(java.util.ArrayList)}.
     */
    @Test
    public void testCheckWorldClimCell() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#deleteDirectory(java.io.File)}.
     */
    @Test
    public void testDeleteDirectory() {
	fail("Not yet implemented");
    }

    /**
     * Test method for {@link src.model.TreatmentData#establishmentMeansOption(java.util.ArrayList)}.
     */
    @Test
    public void testEstablishmentMeansOption() {
	fail("Not yet implemented");
    }

}
