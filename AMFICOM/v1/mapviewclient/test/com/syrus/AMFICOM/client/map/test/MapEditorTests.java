/**
 * $Id: MapEditorTests.java,v 1.1 2005/07/01 07:52:53 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

public class MapEditorTests {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(MapEditorTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.syrus.AMFICOM.client.map.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(LogicalNetLayerTestCase.class);

		suite.addTestSuite(CreateSiteCommandAtomicTestCase.class);
		suite.addTestSuite(CreatePhysicalLinkCommandAtomicTestCase.class);
		suite.addTestSuite(CreatePhysicalNodeCommandAtomicTestCase.class);
		suite.addTestSuite(CreateNodeLinkCommandAtomicTestCase.class);
		suite.addTestSuite(CreateCollectorCommandAtomicTestCase.class);

		suite.addTestSuite(CreateNodeLinkCommandBundleTestCase.class);
		suite.addTestSuite(DeleteNodeCommandBundleTestCase.class);
		suite.addTestSuite(DeleteNodeLinkCommandBundleTestCase.class);
		suite.addTestSuite(DeletePhysicalLinkCommandBundleTestCase.class);
		suite.addTestSuite(InsertSiteCommandBundleTestCase.class);
		suite.addTestSuite(CreatePhysicalNodeCommandBundleTestCase.class);
		
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
