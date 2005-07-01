package com.syrus.AMFICOM.client.map.test;
/**
 * $Id: CacheTests.java,v 1.1 2005/07/01 11:33:01 peskovsky Exp $
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
				"Test for com.syrus.AMFICOM.client.map");
		//$JUnit-BEGIN$
		suite.addTestSuite(LoadingThreadTest.class);
//		suite.addTestSuite(TopologicalImageCacheTest.class);
		
		TestSetup wrapper = new TestSetup(suite) {
			protected void setUp() {
				METS.setUp();
			}
			protected void tearDown() {
				METS.tearDown();
			}
		};
		//$JUnit-END$
		return wrapper;
	}

}
