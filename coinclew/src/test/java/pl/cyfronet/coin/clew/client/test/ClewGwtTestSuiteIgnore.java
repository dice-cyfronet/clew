package pl.cyfronet.coin.clew.client.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * Combines all GWT test cases to run in a single test framework instance
 * to save time.
 * 
 * TODO(DH): Ignoring until Java 1.8 support is in place.
 */
public class ClewGwtTestSuiteIgnore extends GWTTestSuite {
	public static final String CLOUD_FACADE_URL = "https://vph-dev.cyfronet.pl/api/v1";
	
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(CloudFacadeApplianceTypeRestGwtTest.class);
		suite.addTestSuite(CloudFacadeApplianceSetRestGwtTest.class);
		suite.addTestSuite(CloudFacadeUserKeyRestGwtTest.class);
		suite.addTestSuite(CloudFacadeApplianceInstanceRestGwtTest.class);
		
		return suite;
	}
}