/*-
 * $Id: SchemePathTestCase.java,v 1.16 2005/08/04 18:55:05 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ObjectGroupEntities.CONFIGURATION_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.MAP_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.RESOURCE_GROUP_CODE;
import static com.syrus.AMFICOM.general.ObjectGroupEntities.SCHEME_GROUP_CODE;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;

import junit.awtui.TestRunner;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IGSConnectionManager;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.MapLibrary;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.map.SiteNodeType;
import com.syrus.AMFICOM.map.corba.IdlSiteNodeTypePackage.SiteNodeTypeSort;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;
import com.syrus.util.Logger;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.16 $, $Date: 2005/08/04 18:55:05 $
 * @module scheme
 */
public final class SchemePathTestCase extends TestCase {
	public SchemePathTestCase(final String method) {
		super(method);
	}

	public static void main(String[] args) {
		TestRunner.run(SchemePathTestCase.class);
	}

	public static Test suite() {
		final TestSuite testSuite = new TestSuite();
		testSuite.addTest(new SchemePathTestCase("testAssertionStatus"));
		testSuite.addTest(new SchemePathTestCase("testSchemePathSiblings"));
		testSuite.addTest(new SchemePathTestCase("testSchemePathNoSiblings"));
		testSuite.addTest(new SchemePathTestCase("testShiftLeft"));
		testSuite.addTest(new SchemePathTestCase("testShiftRight"));
		testSuite.addTest(new SchemePathTestCase("testSetSchemePaths"));
		testSuite.addTest(new SchemePathTestCase("testSchemeProtoElementClone"));
		testSuite.addTest(new SchemePathTestCase("testGetCurrentSchemeMonitoringSolution"));
		testSuite.addTest(new SchemePathTestCase("testInvalidClone"));
		testSuite.addTest(new SchemePathTestCase("testSchemeClone"));
		return new TestSetup(testSuite) {
			@Override
			protected void setUp() {
				oneTimeSetUp();
			}

			@Override
			protected void tearDown() {
				oneTimeTearDown();
			}
		};
	}

	public static void oneTimeSetUp() {
		Log.initialize(new Logger() {
			@SuppressWarnings("all")
			public void debugMessage(final String message, final Level debugLevel) {
			}

			public void debugException(final Throwable t, final Level debugLevel) {
				t.printStackTrace();
			}

			@SuppressWarnings("all")
			public void errorMessage(final String message) {
			}

			public void errorException(final Throwable t) {
				t.printStackTrace();
			}
		});
		StorableObjectPool.init(new EmptySchemeObjectLoader(), StorableObjectResizableLRUMap.class);
		StorableObjectPool.addObjectPoolGroup(CONFIGURATION_GROUP_CODE, 20);
		StorableObjectPool.addObjectPoolGroup(RESOURCE_GROUP_CODE, 20);
		StorableObjectPool.addObjectPoolGroup(MAP_GROUP_CODE, 20);
		StorableObjectPool.addObjectPoolGroup(SCHEME_GROUP_CODE, 20);
		final IdentifierGeneratorServer identifierGeneratorServer = new IdentifierGeneratorServer() {
			private long l;
			private static final long serialVersionUID = -9123341768454319489L;

			public IdlIdentifier getGeneratedIdentifier(short entity) throws AMFICOMRemoteException {
				return this.getGeneratedIdentifierRange(entity, 1)[0];
			}

			public synchronized IdlIdentifier[] getGeneratedIdentifierRange(short entity, int size) throws AMFICOMRemoteException {
				final IdlIdentifier ids[] = new IdlIdentifier[size];
				for (int i = 0; i < size; i++) {
					ids[i] = new Identifier(ObjectEntities.codeToString(entity) + '_' + this.l++).getTransferable();
				}
				return ids;
			}
		};
		IdentifierPool.init(new IGSConnectionManager() {
			public IdentifierGeneratorServer getIGSReference() throws CommunicationException {
				return identifierGeneratorServer;
			}
			
		});
	}

	public static void oneTimeTearDown() {
		// empty
	}

	public void testAssertionStatus() {
		try {
			assert false;
			fail();
		} catch (final AssertionError ae) {
			assertTrue(true);
		}
	}

	public void testSchemePathSiblings() throws ApplicationException {
		this.testSchemePath(true);
	}

	public void testSchemePathNoSiblings() throws ApplicationException {
		this.testSchemePath(false);
	}

	public void testSchemePath(final boolean processSubsequentSiblings) throws ApplicationException {
		final Identifier userId = new Identifier("User_0");
		final Identifier domainId = new Identifier("Domain_0");
		final Identifier imageId = new Identifier("ImageResource_0");

		final LinkType linkType = LinkType.createInstance(userId, "codename", "description", "name", LinkTypeSort.LINKTYPESORT_ETHERNET, "manufacturer", "manufactirer code", imageId);
		final CableLinkType cableLinkType = CableLinkType.createInstance(userId, "codename", "description", "name", LinkTypeSort.LINKTYPESORT_GSM, "manufactirer", "manufactirer code", imageId);
		final CableThreadType cableThreadType = CableThreadType.createInstance(userId, "codename", "description", "name", 0, linkType, cableLinkType);

		final String schemeName = "a scheme";
		final Scheme scheme = Scheme.createInstance(userId, schemeName, IdlKind.BAY, domainId);
		final String schemeMonitoringSolutionName = "a scheme monitoring solution";
		final SchemeMonitoringSolution schemeMonitoringSolution = SchemeMonitoringSolution.createInstance(userId, schemeMonitoringSolutionName, scheme);
		final String schemePathName = "a scheme path";
		final SchemePath schemePath = SchemePath.createInstance(userId, schemePathName, schemeMonitoringSolution);
		assertEquals(schemeName, schemePath.getParentSchemeMonitoringSolution().getParentScheme().getName());
		final Set<SchemePath> schemePaths = scheme.getSchemePathsRecursively();
		assertEquals(1, schemePaths.size());
		assertEquals(schemePathName, schemePaths.iterator().next().getName());

		final SchemeLink schemeLink = SchemeLink.createInstance(userId, "a scheme link", scheme);
		final SchemeCableLink schemeCableLink = SchemeCableLink.createInstance(userId, "a scheme cable link", scheme); 
		final SchemeCableThread schemeCableThread = SchemeCableThread.createInstance(userId, "a scheme cable thread", cableThreadType, schemeCableLink);
		final SchemeElement schemeElement = SchemeElement.createInstance(userId, "a scheme element", scheme);
		final SchemeDevice schemeDevice = SchemeDevice.createInstance(userId, "a scheme device", schemeElement);
		final SchemePort startSchemePort = SchemePort.createInstance(userId, "starting scheme port", IdlDirectionType._IN, schemeDevice);
		final SchemePort endSchemePort = SchemePort.createInstance(userId, "ending scheme port", IdlDirectionType._OUT, schemeDevice);

		final AbstractSchemeElement abstractSchemeElements[] = new AbstractSchemeElement[] {schemeElement, schemeCableLink, schemeLink};

		final PathElement pathElement0 = PathElement.createInstance(userId, schemePath, schemePath.getPathMembers().isEmpty() ? null : startSchemePort, endSchemePort);
		final PathElement pathElement1 = PathElement.createInstance(userId, schemePath, schemeCableThread);
		final PathElement pathElement2 = PathElement.createInstance(userId, schemePath, schemeLink);

		@SuppressWarnings("unused")
		final Identifier pathElementId0 = pathElement0.getId();
		final Identifier pathElementId1 = pathElement1.getId();
		final Identifier pathElementId2 = pathElement2.getId();

		SortedSet<PathElement> pathElements = schemePath.getPathMembers();
		assertEquals(3, pathElements.size());
		int i = 0;
		for (final PathElement pathElement : pathElements) {
			assertEquals(i, pathElement.getSequentialNumber());
			assertEquals(schemePathName, pathElement.getParentPathOwner().getName());
			assertEquals(abstractSchemeElements[i].getName(), pathElement.getAbstractSchemeElement().getName());
			assertEquals(i++, pathElement.getKind().value());
		}

		schemePath.removePathMember(pathElement1, processSubsequentSiblings);

		pathElements = schemePath.getPathMembers();
		final int size = pathElements.size();

		assertEquals(0, pathElement0.getSequentialNumber());

		assertEquals(-1, pathElement1.sequentialNumber);
		assertEquals(Identifier.VOID_IDENTIFIER, pathElement1.parentSchemePathId);
		assertNull(StorableObjectPool.getStorableObject(pathElementId1, true));

		if (processSubsequentSiblings) {
			assertEquals(1, size);
			assertEquals(-1, pathElement2.sequentialNumber);
			assertEquals(Identifier.VOID_IDENTIFIER, pathElement2.parentSchemePathId);
			assertNull(StorableObjectPool.getStorableObject(pathElementId2, true));
		} else {
			assertEquals(2, size);
			assertEquals(1, pathElement2.getSequentialNumber());
			assertEquals(schemePath.getId(), pathElement2.getParentSchemePathId());
			assertNotNull(StorableObjectPool.getStorableObject(pathElementId2, true));
		}
	}

	public void testShiftLeft() throws ApplicationException {
		final Identifier userId = new Identifier("User_0");
		final Identifier domainId = new Identifier("Domain_0");

		final String schemeName = "a scheme";
		final Scheme scheme = Scheme.createInstance(userId, schemeName, IdlKind.BAY, domainId);
		final String schemeMonitoringSolutionName = "a scheme monitoring solution";
		final SchemeMonitoringSolution schemeMonitoringSolution = SchemeMonitoringSolution.createInstance(userId, schemeMonitoringSolutionName, scheme);
		final String schemePathName = "a scheme path";
		final SchemePath schemePath = SchemePath.createInstance(userId, schemePathName, schemeMonitoringSolution);
		assertEquals(schemeName, schemePath.getParentSchemeMonitoringSolution().getParentScheme().getName());
		final Set<SchemePath> schemePaths = scheme.getSchemePathsRecursively();
		assertEquals(1, schemePaths.size());
		assertEquals(schemePathName, schemePaths.iterator().next().getName());

		final SchemeLink schemeLink = SchemeLink.createInstance(userId, "a scheme link", scheme);
		final SchemeElement schemeElement = SchemeElement.createInstance(userId, "a scheme element", scheme);
		final SchemeDevice schemeDevice = SchemeDevice.createInstance(userId, "a scheme device", schemeElement);
		final SchemePort startSchemePort = SchemePort.createInstance(userId, "starting scheme port", IdlDirectionType._IN, schemeDevice);
		final SchemePort endSchemePort = SchemePort.createInstance(userId, "ending scheme port", IdlDirectionType._OUT, schemeDevice);

		final PathElement pathElement0 = PathElement.createInstance(userId, schemePath, schemePath.getPathMembers().isEmpty() ? null : startSchemePort, endSchemePort);
		final PathElement pathElement1 = PathElement.createInstance(userId, schemePath, schemeLink);
		final PathElement pathElement2 = PathElement.createInstance(userId, schemePath, schemeLink);
		final PathElement pathElement3 = PathElement.createInstance(userId, schemePath, schemeLink);
		final PathElement pathElement4 = PathElement.createInstance(userId, schemePath, schemeLink);
		final PathElement pathElement5 = PathElement.createInstance(userId, schemePath, schemeLink);
		final PathElement pathElement6 = PathElement.createInstance(userId, schemePath, schemeLink);
		final PathElement pathElement7 = PathElement.createInstance(userId, schemePath, schemeLink);
		final PathElement pathElement8 = PathElement.createInstance(userId, schemePath, schemeLink);
		final PathElement pathElement9 = PathElement.createInstance(userId, schemePath, schemeLink);

		assertEquals(0, pathElement0.getSequentialNumber());
		assertEquals(1, pathElement1.getSequentialNumber());
		assertEquals(2, pathElement2.getSequentialNumber());
		assertEquals(3, pathElement3.getSequentialNumber());
		assertEquals(4, pathElement4.getSequentialNumber());
		assertEquals(5, pathElement5.getSequentialNumber());
		assertEquals(6, pathElement6.getSequentialNumber());
		assertEquals(7, pathElement7.getSequentialNumber());
		assertEquals(8, pathElement8.getSequentialNumber());
		assertEquals(9, pathElement9.getSequentialNumber());

		pathElement5.setParentPathOwner(null, false);

		assertEquals(0, pathElement0.getSequentialNumber());
		assertEquals(1, pathElement1.getSequentialNumber());
		assertEquals(2, pathElement2.getSequentialNumber());
		assertEquals(3, pathElement3.getSequentialNumber());
		assertEquals(4, pathElement4.getSequentialNumber());
		assertEquals(-1, pathElement5.sequentialNumber);
		assertEquals(5, pathElement6.getSequentialNumber());
		assertEquals(6, pathElement7.getSequentialNumber());
		assertEquals(7, pathElement8.getSequentialNumber());
		assertEquals(8, pathElement9.getSequentialNumber());
	}

	public void testShiftRight() throws ApplicationException {
		final Identifier userId = new Identifier("User_0");
		final Identifier domainId = new Identifier("Domain_0");

		final Scheme scheme0 = Scheme.createInstance(userId, "scheme0", IdlKind.BAY, domainId);
		final SchemeMonitoringSolution schemeMonitoringSolution0 = SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution0", scheme0);
		final SchemePath schemePath0 = SchemePath.createInstance(userId, "schemePath0", schemeMonitoringSolution0);
		final SchemeLink schemeLink0 = SchemeLink.createInstance(userId, "schemeLink0", scheme0);
		final SchemeElement schemeElement0 = SchemeElement.createInstance(userId, "schemeElement0", scheme0);
		final SchemeDevice schemeDevice0 = SchemeDevice.createInstance(userId, "schemeDevice0", schemeElement0);
		final SchemePort startSchemePort0 = SchemePort.createInstance(userId, "startSchemePort0", IdlDirectionType._IN, schemeDevice0);
		final SchemePort endSchemePort0 = SchemePort.createInstance(userId, "endSchemePort0", IdlDirectionType._OUT, schemeDevice0);
		final SchemeCableLink schemeCableLink0 = SchemeCableLink.createInstance(userId, "schemeCableLink0", scheme0);
		final SiteNodeType siteNodeType0 = SiteNodeType.createInstance(userId, SiteNodeTypeSort.ATS, "codename", "siteNodeType0", "", Identifier.VOID_IDENTIFIER, false, MapLibrary.createInstance(userId, null)); 
		final SiteNode siteNode0 = SiteNode.createInstance(userId, new DoublePoint(), siteNodeType0);
		final SiteNode siteNode1 = SiteNode.createInstance(userId, new DoublePoint(), siteNodeType0);

		final PathElement pathElement0 = PathElement.createInstance(userId, schemePath0, schemePath0.getPathMembers().isEmpty() ? null : startSchemePort0, endSchemePort0);
		final PathElement pathElement1 = PathElement.createInstance(userId, schemePath0, schemeLink0);
		final PathElement pathElement2 = PathElement.createInstance(userId, schemePath0, schemeLink0);
		final PathElement pathElement3 = PathElement.createInstance(userId, schemePath0, schemeLink0);
		final PathElement pathElement4 = PathElement.createInstance(userId, schemePath0, schemeLink0);
		final PathElement pathElement5 = PathElement.createInstance(userId, schemePath0, schemeLink0);
		final PathElement pathElement6 = PathElement.createInstance(userId, schemePath0, schemeLink0);
		final PathElement pathElement7 = PathElement.createInstance(userId, schemePath0, schemeLink0);
		final PathElement pathElement8 = PathElement.createInstance(userId, schemePath0, schemeLink0);
		final PathElement pathElement9 = PathElement.createInstance(userId, schemePath0, schemeLink0);

		pathElement6.insertSelfAfter(pathElement5);
		pathElement6.insertSelfAfter(pathElement6);
		pathElement6.insertSelfBefore(pathElement6);
		pathElement6.insertSelfBefore(pathElement7);

		pathElement6.insertSelfBefore(pathElement2);
		
		assertEquals(0, pathElement0.getSequentialNumber());
		assertEquals(1, pathElement1.getSequentialNumber());
		assertEquals(2, pathElement6.getSequentialNumber());
		assertEquals(3, pathElement2.getSequentialNumber());
		assertEquals(4, pathElement3.getSequentialNumber());
		assertEquals(5, pathElement4.getSequentialNumber());
		assertEquals(6, pathElement5.getSequentialNumber());
		assertEquals(7, pathElement7.getSequentialNumber());
		assertEquals(8, pathElement8.getSequentialNumber());
		assertEquals(9, pathElement9.getSequentialNumber());

		pathElement2.insertSelfBefore(pathElement7);

		assertEquals(0, pathElement0.getSequentialNumber());
		assertEquals(1, pathElement1.getSequentialNumber());
		assertEquals(2, pathElement6.getSequentialNumber());
		assertEquals(3, pathElement3.getSequentialNumber());
		assertEquals(4, pathElement4.getSequentialNumber());
		assertEquals(5, pathElement5.getSequentialNumber());
		assertEquals(6, pathElement2.getSequentialNumber());
		assertEquals(7, pathElement7.getSequentialNumber());
		assertEquals(8, pathElement8.getSequentialNumber());
		assertEquals(9, pathElement9.getSequentialNumber());

		pathElement6.insertSelfAfter(pathElement2);

		assertEquals(0, pathElement0.getSequentialNumber());
		assertEquals(1, pathElement1.getSequentialNumber());
		assertEquals(2, pathElement3.getSequentialNumber());
		assertEquals(3, pathElement4.getSequentialNumber());
		assertEquals(4, pathElement5.getSequentialNumber());
		assertEquals(5, pathElement2.getSequentialNumber());
		assertEquals(6, pathElement6.getSequentialNumber());
		assertEquals(7, pathElement7.getSequentialNumber());
		assertEquals(8, pathElement8.getSequentialNumber());
		assertEquals(9, pathElement9.getSequentialNumber());

		pathElement2.insertSelfAfter(pathElement1);

		assertEquals(0, pathElement0.getSequentialNumber());
		assertEquals(1, pathElement1.getSequentialNumber());
		assertEquals(2, pathElement2.getSequentialNumber());
		assertEquals(3, pathElement3.getSequentialNumber());
		assertEquals(4, pathElement4.getSequentialNumber());
		assertEquals(5, pathElement5.getSequentialNumber());
		assertEquals(6, pathElement6.getSequentialNumber());
		assertEquals(7, pathElement7.getSequentialNumber());
		assertEquals(8, pathElement8.getSequentialNumber());
		assertEquals(9, pathElement9.getSequentialNumber());

		final CableChannelingItem cableChannelingItem0 = CableChannelingItem.createInstance(userId, siteNode0, siteNode1, schemeCableLink0);
		final CableChannelingItem cableChannelingItem1 = CableChannelingItem.createInstance(userId, siteNode0, siteNode1, schemeCableLink0);
		final CableChannelingItem cableChannelingItem2 = CableChannelingItem.createInstance(userId, siteNode0, siteNode1, schemeCableLink0);
		final CableChannelingItem cableChannelingItem3 = CableChannelingItem.createInstance(userId, siteNode0, siteNode1, schemeCableLink0);
		final CableChannelingItem cableChannelingItem4 = CableChannelingItem.createInstance(userId, siteNode0, siteNode1, schemeCableLink0);
		final CableChannelingItem cableChannelingItem5 = CableChannelingItem.createInstance(userId, siteNode0, siteNode1, schemeCableLink0);
		final CableChannelingItem cableChannelingItem6 = CableChannelingItem.createInstance(userId, siteNode0, siteNode1, schemeCableLink0);
		final CableChannelingItem cableChannelingItem7 = CableChannelingItem.createInstance(userId, siteNode0, siteNode1, schemeCableLink0);
		final CableChannelingItem cableChannelingItem8 = CableChannelingItem.createInstance(userId, siteNode0, siteNode1, schemeCableLink0);
		final CableChannelingItem cableChannelingItem9 = CableChannelingItem.createInstance(userId, siteNode0, siteNode1, schemeCableLink0);

		cableChannelingItem6.insertSelfAfter(cableChannelingItem5);
		cableChannelingItem6.insertSelfAfter(cableChannelingItem6);
		cableChannelingItem6.insertSelfBefore(cableChannelingItem6);
		cableChannelingItem6.insertSelfBefore(cableChannelingItem7);

		cableChannelingItem6.insertSelfBefore(cableChannelingItem2);
		
		assertEquals(0, cableChannelingItem0.getSequentialNumber());
		assertEquals(1, cableChannelingItem1.getSequentialNumber());
		assertEquals(2, cableChannelingItem6.getSequentialNumber());
		assertEquals(3, cableChannelingItem2.getSequentialNumber());
		assertEquals(4, cableChannelingItem3.getSequentialNumber());
		assertEquals(5, cableChannelingItem4.getSequentialNumber());
		assertEquals(6, cableChannelingItem5.getSequentialNumber());
		assertEquals(7, cableChannelingItem7.getSequentialNumber());
		assertEquals(8, cableChannelingItem8.getSequentialNumber());
		assertEquals(9, cableChannelingItem9.getSequentialNumber());

		cableChannelingItem2.insertSelfBefore(cableChannelingItem7);

		assertEquals(0, cableChannelingItem0.getSequentialNumber());
		assertEquals(1, cableChannelingItem1.getSequentialNumber());
		assertEquals(2, cableChannelingItem6.getSequentialNumber());
		assertEquals(3, cableChannelingItem3.getSequentialNumber());
		assertEquals(4, cableChannelingItem4.getSequentialNumber());
		assertEquals(5, cableChannelingItem5.getSequentialNumber());
		assertEquals(6, cableChannelingItem2.getSequentialNumber());
		assertEquals(7, cableChannelingItem7.getSequentialNumber());
		assertEquals(8, cableChannelingItem8.getSequentialNumber());
		assertEquals(9, cableChannelingItem9.getSequentialNumber());

		cableChannelingItem6.insertSelfAfter(cableChannelingItem2);

		assertEquals(0, cableChannelingItem0.getSequentialNumber());
		assertEquals(1, cableChannelingItem1.getSequentialNumber());
		assertEquals(2, cableChannelingItem3.getSequentialNumber());
		assertEquals(3, cableChannelingItem4.getSequentialNumber());
		assertEquals(4, cableChannelingItem5.getSequentialNumber());
		assertEquals(5, cableChannelingItem2.getSequentialNumber());
		assertEquals(6, cableChannelingItem6.getSequentialNumber());
		assertEquals(7, cableChannelingItem7.getSequentialNumber());
		assertEquals(8, cableChannelingItem8.getSequentialNumber());
		assertEquals(9, cableChannelingItem9.getSequentialNumber());

		cableChannelingItem2.insertSelfAfter(cableChannelingItem1);

		assertEquals(0, cableChannelingItem0.getSequentialNumber());
		assertEquals(1, cableChannelingItem1.getSequentialNumber());
		assertEquals(2, cableChannelingItem2.getSequentialNumber());
		assertEquals(3, cableChannelingItem3.getSequentialNumber());
		assertEquals(4, cableChannelingItem4.getSequentialNumber());
		assertEquals(5, cableChannelingItem5.getSequentialNumber());
		assertEquals(6, cableChannelingItem6.getSequentialNumber());
		assertEquals(7, cableChannelingItem7.getSequentialNumber());
		assertEquals(8, cableChannelingItem8.getSequentialNumber());
		assertEquals(9, cableChannelingItem9.getSequentialNumber());
	}

	/**
	 * @see SchemeMonitoringSolution#setSchemePaths(java.util.Set)
	 */
	public void testSetSchemePaths() throws ApplicationException {
		final Identifier userId = new Identifier("User_0");
		final Identifier domainId = new Identifier("Domain_0");

		final Scheme scheme0 = Scheme.createInstance(userId, "scheme0", IdlKind.BAY, domainId);
		final Scheme scheme1 = Scheme.createInstance(userId, "scheme1", IdlKind.BAY, domainId);

		final SchemeMonitoringSolution schemeMonitoringSolution0 = SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution0", scheme0);
		final SchemeMonitoringSolution schemeMonitoringSolution1 = SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution1", scheme1);

		@SuppressWarnings("unused")
		final SchemePath schemePath0 = SchemePath.createInstance(userId, "0", schemeMonitoringSolution0);
		@SuppressWarnings("unused")
		final SchemePath schemePath1 = SchemePath.createInstance(userId, "1", schemeMonitoringSolution0);
		@SuppressWarnings("unused")
		final SchemePath schemePath2 = SchemePath.createInstance(userId, "2", schemeMonitoringSolution0);
		final SchemePath schemePath3 = SchemePath.createInstance(userId, "3", schemeMonitoringSolution0);
		final SchemePath schemePath4 = SchemePath.createInstance(userId, "4", schemeMonitoringSolution0);
		final SchemePath schemePath5 = SchemePath.createInstance(userId, "5", schemeMonitoringSolution0);
		final SchemePath schemePath6 = SchemePath.createInstance(userId, "6", schemeMonitoringSolution0);
		final SchemePath schemePath7 = SchemePath.createInstance(userId, "7", schemeMonitoringSolution1);
		final SchemePath schemePath8 = SchemePath.createInstance(userId, "8", schemeMonitoringSolution1);
		final SchemePath schemePath9 = SchemePath.createInstance(userId, "9", schemeMonitoringSolution1);

		for (final SchemePath schemePath : scheme0.getSchemePathsRecursively()) {
			System.out.println(scheme0.getName() + ": " + schemePath.getName());
		}
		for (final SchemePath schemePath : scheme1.getSchemePathsRecursively()) {
			System.out.println(scheme1.getName() + ": " + schemePath.getName());
		}
		
		final Set<SchemePath> schemePaths = new HashSet<SchemePath>();
		schemePaths.add(schemePath3);
		schemePaths.add(schemePath4);
		schemePaths.add(schemePath5);
		schemePaths.add(schemePath6);
		schemePaths.add(schemePath7);
		schemePaths.add(schemePath8);
		schemePaths.add(schemePath9);
		
		schemeMonitoringSolution0.setSchemePaths(schemePaths);

		for (final SchemePath schemePath : scheme0.getSchemePathsRecursively()) {
			System.out.println(scheme0.getName() + ": " + schemePath.getName());
		}
		for (final SchemePath schemePath : scheme1.getSchemePathsRecursively()) {
			System.out.println(scheme1.getName() + ": " + schemePath.getName());
		}
	}

	/**
	 * @throws CreateObjectException 
	 * @see SchemeProtoElement#clone()
	 */
	public void testSchemeProtoElementClone() throws CreateObjectException, CloneNotSupportedException {
		final Identifier userId = new Identifier("User_0");
		final SchemeProtoGroup schemeProtoGroup = SchemeProtoGroup.createInstance(userId, "a scheme proto group");
		final SchemeProtoElement schemeProtoElement0 = SchemeProtoElement.createInstance(userId, "parent scheme proto element", schemeProtoGroup);
		final SchemeImageResource ugoCell = SchemeImageResource.createInstance(userId);
		final SchemeImageResource schemeCell = SchemeImageResource.createInstance(userId);
		schemeProtoElement0.setUgoCell(ugoCell);
		schemeProtoElement0.setSchemeCell(schemeCell);
		@SuppressWarnings("unused")
		final SchemeProtoElement schemeProtoElement1 = SchemeProtoElement.createInstance(userId, "child scheme proto element", schemeProtoElement0);

		final SchemeProtoElement schemeProtoElement2 = schemeProtoElement0.clone();

		System.out.println(schemeProtoElement0.getId());
		System.out.println("\t\t" + schemeProtoElement0.getName());
		System.out.println("\t\t" + schemeProtoElement0.getDescription());
		System.out.println("\t\t" + schemeProtoElement0.getLabel());
		System.out.println("\t\t" + schemeProtoElement0.getEquipmentTypeId());
		System.out.println("\t\t" + schemeProtoElement0.getSymbolId());
		System.out.println("\t\t" + schemeProtoElement0.getUgoCellId());
		System.out.println("\t\t" + schemeProtoElement0.getSchemeCellId());
		System.out.println("\t\t" + schemeProtoElement0.getParentSchemeProtoGroupId());
		System.out.println("\t\t" + schemeProtoElement0.getParentSchemeProtoElementId());
		for (final SchemeProtoElement schemeProtoElement : schemeProtoElement0.getSchemeProtoElements()) {
			assertTrue(schemeProtoElement.getClonedIdMap().isEmpty());
			System.out.println("\t\t" + schemeProtoElement.getId() + ": " + schemeProtoElement.getName());
		}
		System.out.println(schemeProtoElement2.getId());
		System.out.println("\t\t" + schemeProtoElement2.getName());
		System.out.println("\t\t" + schemeProtoElement2.getDescription());
		System.out.println("\t\t" + schemeProtoElement2.getLabel());
		System.out.println("\t\t" + schemeProtoElement2.getEquipmentTypeId());
		System.out.println("\t\t" + schemeProtoElement2.getSymbolId());		
		System.out.println("\t\t" + schemeProtoElement2.getUgoCellId());
		System.out.println("\t\t" + schemeProtoElement2.getSchemeCellId());
		System.out.println("\t\t" + schemeProtoElement2.getParentSchemeProtoGroupId());
		System.out.println("\t\t" + schemeProtoElement2.getParentSchemeProtoElementId());
		for (final SchemeProtoElement schemeProtoElement : schemeProtoElement2.getSchemeProtoElements()) {
			System.out.println("\t\t" + schemeProtoElement.getId() + ": " + schemeProtoElement.getName());
		}
		assertTrue(schemeProtoElement0.getClonedIdMap().isEmpty());
		assertTrue(schemeProtoElement0.getClonedIdMap().isEmpty());
		final Map<Identifier, Identifier> idMap = schemeProtoElement2.getClonedIdMap();
		for (final Identifier key : idMap.keySet()) {
			System.out.println(key + ":\t" + idMap.get(key));
		}
	}

	/**
	 * @throws CreateObjectException 
	 * @see Scheme#getCurrentSchemeMonitoringSolution()
	 */
	public void testGetCurrentSchemeMonitoringSolution() throws CreateObjectException {
		final Identifier userId = new Identifier("User_0");
		final Identifier domainId = new Identifier("Domain_0");

		final Scheme scheme0 = Scheme.createInstance(userId, "scheme0", IdlKind.BAY, domainId);

		final SchemeOptimizeInfo schemeOptimizeInfo0 = SchemeOptimizeInfo.createInstance(userId, "schemeOptimizeInfo0", scheme0);
		final SchemeOptimizeInfo schemeOptimizeInfo1 = SchemeOptimizeInfo.createInstance(userId, "schemeOptimizeInfo1", scheme0);

		final SchemeMonitoringSolution schemeMonitoringSolution0 =
				SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution0", scheme0);
		final SchemeMonitoringSolution schemeMonitoringSolution1 =
				SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution1", scheme0);
		final SchemeMonitoringSolution schemeMonitoringSolution2 =
				SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution2", scheme0);
		final SchemeMonitoringSolution schemeMonitoringSolution3 =
				SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution3", scheme0);
		final SchemeMonitoringSolution schemeMonitoringSolution4 =
				SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution4", schemeOptimizeInfo0);
		final SchemeMonitoringSolution schemeMonitoringSolution5 =
				SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution5", schemeOptimizeInfo0);
		final SchemeMonitoringSolution schemeMonitoringSolution6 =
				SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution6", schemeOptimizeInfo0);
		final SchemeMonitoringSolution schemeMonitoringSolution7 =
				SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution7", schemeOptimizeInfo1);
		final SchemeMonitoringSolution schemeMonitoringSolution8 =
				SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution8", schemeOptimizeInfo1);
		final SchemeMonitoringSolution schemeMonitoringSolution9 =
				SchemeMonitoringSolution.createInstance(userId, "schemeMonitoringSolution9", schemeOptimizeInfo1);

		schemeMonitoringSolution0.setActive(true);
		assertEquals(scheme0.getCurrentSchemeMonitoringSolution().getId(), schemeMonitoringSolution0.getId());

		schemeMonitoringSolution1.setActive(true);
		assertEquals(scheme0.getCurrentSchemeMonitoringSolution().getId(), schemeMonitoringSolution1.getId());

		schemeMonitoringSolution2.setActive(true);
		assertEquals(scheme0.getCurrentSchemeMonitoringSolution().getId(), schemeMonitoringSolution2.getId());

		schemeMonitoringSolution3.setActive(true);
		assertEquals(scheme0.getCurrentSchemeMonitoringSolution().getId(), schemeMonitoringSolution3.getId());

		schemeMonitoringSolution4.setActive(true);
		assertEquals(scheme0.getCurrentSchemeMonitoringSolution().getId(), schemeMonitoringSolution4.getId());

		schemeMonitoringSolution5.setActive(true);
		assertEquals(scheme0.getCurrentSchemeMonitoringSolution().getId(), schemeMonitoringSolution5.getId());

		schemeMonitoringSolution6.setActive(true);
		assertEquals(scheme0.getCurrentSchemeMonitoringSolution().getId(), schemeMonitoringSolution6.getId());

		schemeMonitoringSolution7.setActive(true);
		assertEquals(scheme0.getCurrentSchemeMonitoringSolution().getId(), schemeMonitoringSolution7.getId());

		schemeMonitoringSolution8.setActive(true);
		assertEquals(scheme0.getCurrentSchemeMonitoringSolution().getId(), schemeMonitoringSolution8.getId());

		schemeMonitoringSolution9.setActive(true);
		assertEquals(scheme0.getCurrentSchemeMonitoringSolution().getId(), schemeMonitoringSolution9.getId());

		int i = 0;
		for (Set<SchemeMonitoringSolution> schemeMonitoringSolutions = scheme0.getSchemeMonitoringSolutionsRecursively();
				!schemeMonitoringSolutions.isEmpty();
				schemeMonitoringSolutions = scheme0.getSchemeMonitoringSolutionsRecursively()) {
			final SchemeMonitoringSolution oldSchemeMonitoringSolution = scheme0.getCurrentSchemeMonitoringSolution();
			oldSchemeMonitoringSolution.setActive(false);
			final SchemeMonitoringSolution newSchemeMonitoringSolution = scheme0.getCurrentSchemeMonitoringSolution();
			System.out.println("Pass " + (i++)
					+ "; old: " + oldSchemeMonitoringSolution.getName()
					+ "; new: " + (newSchemeMonitoringSolution == null ? "<null>" : newSchemeMonitoringSolution.getName()));
		}
	}

	public void testInvalidClone() throws CreateObjectException, CloneNotSupportedException {
		final Identifier userId = new Identifier("User_0");
		final Identifier domainId = new Identifier("Domain_0");
		final Identifier imageId = new Identifier("ImageResource_0");

		final LinkType linkType0 = LinkType.createInstance(userId, "codename", "description", "linkType0", LinkTypeSort.LINKTYPESORT_ETHERNET, "manufacturer", "manufactirer code", imageId);
		final CableLinkType cableLinkType0 = CableLinkType.createInstance(userId, "codename", "description", "cableLinkType0", LinkTypeSort.LINKTYPESORT_GSM, "manufactirer", "manufactirer code", imageId);
		final CableThreadType cableThreadType0 = CableThreadType.createInstance(userId, "codename", "description", "cableThreadType0", 0, linkType0, cableLinkType0);

		final Scheme scheme0 = Scheme.createInstance(userId, "scheme0", IdlKind.BAY, domainId);
		final SchemeElement schemeElement0 = SchemeElement.createInstance(userId, "schemeElement0", scheme0);
		final SchemeDevice schemeDevice0 = SchemeDevice.createInstance(userId, "schemeDevice0", schemeElement0);
		final SchemePort schemePort0 = SchemePort.createInstance(userId, "schemePort0", IdlDirectionType._IN, schemeDevice0);
		final SchemeLink schemeLink0 = SchemeLink.createInstance(userId, "schemeLink0", schemeElement0);
		final SchemeCableLink schemeCableLink0 = SchemeCableLink.createInstance(userId, "schemeCableLink0", scheme0);
		final SchemeCableThread schemeCableThread0 = SchemeCableThread.createInstance(userId, "schemeCableThread0", cableThreadType0, schemeCableLink0);

		scheme0.clone();
		schemeElement0.clone();

		try {
			schemeDevice0.clone();
			fail();
		} catch (final CloneNotSupportedException cnse) {
			System.out.println(cnse.getMessage());
			assertTrue(true);
		}

		try {
			schemePort0.clone();
			fail();
		} catch (final CloneNotSupportedException cnse) {
			System.out.println(cnse.getMessage());
			assertTrue(true);
		}

		try {
			schemeLink0.clone();
			fail();
		} catch (final CloneNotSupportedException cnse) {
			System.out.println(cnse.getMessage());
			assertTrue(true);
		}

		try {
			schemeCableLink0.clone();
			fail();
		} catch (final CloneNotSupportedException cnse) {
			System.out.println(cnse.getMessage());
			assertTrue(true);
		}

		try {
			schemeCableThread0.clone();
			fail();
		} catch (final CloneNotSupportedException cnse) {
			System.out.println(cnse.getMessage());
			assertTrue(true);
		}
	}

	public void testSchemeClone() throws CreateObjectException, CloneNotSupportedException {
		final Identifier userId = new Identifier("User_0");
		final Identifier domainId = new Identifier("Domain_0");

		final Scheme scheme0 = Scheme.createInstance(userId, "scheme0", IdlKind.BAY, domainId);
		final SchemeElement schemeElement0 = SchemeElement.createInstance(userId, "schemeElement0", scheme0);
		final Scheme scheme1 = Scheme.createInstance(userId, "scheme1", IdlKind.BAY, domainId);
		scheme1.setParentSchemeElement(schemeElement0);
		final SchemeElement schemeElement1 = SchemeElement.createInstance(userId, "schemeElement1", scheme1);

		final SchemeDevice schemeDevice0 = SchemeDevice.createInstance(userId, "schemeDevice0", schemeElement1);

		final SchemePort schemePort0 = SchemePort.createInstance(userId, "schemePort0", IdlDirectionType._IN, schemeDevice0);
		final SchemePort schemePort1 = SchemePort.createInstance(userId, "schemePort1", IdlDirectionType._IN, schemeDevice0);
		final SchemePort schemePort2 = SchemePort.createInstance(userId, "schemePort2", IdlDirectionType._IN, schemeDevice0);
		final SchemePort schemePort3 = SchemePort.createInstance(userId, "schemePort3", IdlDirectionType._IN, schemeDevice0);

		final SchemeLink schemeLink0 = SchemeLink.createInstance(userId, "schemeLink0", scheme0);
		schemeLink0.setSourceAbstractSchemePort(schemePort0);
		final SchemeLink schemeLink1 = SchemeLink.createInstance(userId, "schemeLink1", schemeElement0);
		schemeLink1.setSourceAbstractSchemePort(schemePort1);
		final SchemeLink schemeLink2 = SchemeLink.createInstance(userId, "schemeLink2", scheme1);
		schemeLink2.setSourceAbstractSchemePort(schemePort2);
		final SchemeLink schemeLink3 = SchemeLink.createInstance(userId, "schemeLink3", schemeElement1);
		schemeLink3.setSourceAbstractSchemePort(schemePort3);
		
		final Scheme scheme0prime = scheme0.clone();
		final SchemeElement schemeElement0prime = scheme0prime.getSchemeElements().iterator().next();
		final Scheme scheme1prime = schemeElement0prime.getSchemes().iterator().next();
		final SchemeElement schemeElement1prime = scheme1prime.getSchemeElements().iterator().next();

		final SchemeDevice schemeDevice0prime = schemeElement1prime.getSchemeDevices().iterator().next();

		final SchemeLink schemeLink0prime = scheme0prime.getSchemeLinks().iterator().next();

		assertNotSame(schemeLink0prime, schemeLink0);
		assertFalse(schemeLink0prime.getId().equals(schemeLink0.getId()));
		assertEquals(schemeLink0prime.getName(), schemeLink0.getName());
		assertNull(schemeLink0prime.getTargetAbstractSchemePort());
		
		final SchemePort schemePort0prime = schemeLink0prime.getSourceAbstractSchemePort();
		assertNotSame(schemePort0prime, schemePort0);
		assertFalse(schemePort0prime.getId().equals(schemePort0.getId()));
		assertEquals(schemePort0prime.getName(), schemePort0.getName());
		assertEquals(schemeDevice0prime, schemePort0prime.getParentSchemeDevice());
	}
}
