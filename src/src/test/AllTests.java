/**
 * src.test
 * AllTests
 */
package src.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * src.test
 * 
 * AllTests.java
 * AllTests
 */
@RunWith(Suite.class)
@SuiteClasses({ TestConnectionDatabase.class, TestTreatment.class, 
    		/*TestPrepareGeographicOption.class,
    		TestGeographicOption.class, TestOptionTDWG.class, 
    		TestRasterOption.class, TestEstablishmentMeansOptions.class*/
    		})

public class AllTests {

}
