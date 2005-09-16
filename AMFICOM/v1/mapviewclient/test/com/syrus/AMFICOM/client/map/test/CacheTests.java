package com.syrus.AMFICOM.client.map.test;
/**
 * $Id: CacheTests.java,v 1.3 2005/09/16 14:53:38 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

public class CacheTests {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(CacheTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.syrus.AMFICOM.client.map"); //$NON-NLS-1$
		//$JUnit-BEGIN$
		suite.addTestSuite(LoadingThreadTest.class);
//		suite.addTestSuite(TopologicalImageCacheTest.class);
		
		TestSetup wrapper = new TestSetup(suite) {
			protected void setUp() {
				try {
					METS.setUp();
				} catch(Exception e) {
					e.printStackTrace();
					fail(e.getMessage());
				}
			}
			protected void tearDown() {
				METS.tearDown();
			}
		};
		//$JUnit-END$
		return wrapper;
	}

}
