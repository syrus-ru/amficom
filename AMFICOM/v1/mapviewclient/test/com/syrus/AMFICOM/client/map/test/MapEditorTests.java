/**
 * $Id: MapEditorTests.java,v 1.11 2006/02/20 09:28:12 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import com.syrus.util.Log;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

public class MapEditorTests {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(MapEditorTests.class);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.syrus.AMFICOM.client.map.test"); //$NON-NLS-1$
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

		suite.addTestSuite(PlaceSchemeElementCommandTestCase.class);
		suite.addTestSuite(PlaceSchemeCableLinkCommandNoCCITestCase.class);
		suite.addTestSuite(PlaceSchemeCableLinkCommandWithCCITestCase.class);
		suite.addTestSuite(PlaceSchemePathCommandTestCase.class);
		suite.addTestSuite(BindPhysicalNodeToSiteCommandBundleTestCase.class);
		suite.addTestSuite(BindUnboundLinkToPhysicalLinkCommandBundleTestCase.class);
		suite.addTestSuite(BindUnboundNodeToSiteCommandBundleTestCase.class);
		suite.addTestSuite(CreateUnboundLinkCommandBundleTestCase.class);
		suite.addTestSuite(GenerateUnboundLinkCablingCommandBundleTestCase.class);
		suite.addTestSuite(GenerateCablePathCablingCommandBundleTestCase.class);

		TestSetup wrapper = new TestSetup(suite) {
			protected void setUp() {
				try {
					METS.setUp();
				} catch(Exception e) {
					Log.errorMessage(e);
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
