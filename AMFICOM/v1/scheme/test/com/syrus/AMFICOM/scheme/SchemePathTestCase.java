/*-
 * $Id: SchemePathTestCase.java,v 1.3 2005/07/18 16:58:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.HashSet;
import java.util.Set;

import junit.awtui.TestRunner;
import junit.framework.TestCase;

import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.EmptyConfigurationObjectLoader;
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
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.DirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.Kind;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/07/18 16:58:43 $
 * @module scheme_v1
 */
public final class SchemePathTestCase extends TestCase {
	public static void main(String[] args) {
		TestRunner.run(SchemePathTestCase.class);
	}

	@Override
	protected void setUp() {
		ConfigurationStorableObjectPool.init(new EmptyConfigurationObjectLoader());
		SchemeStorableObjectPool.init(new EmptySchemeObjectLoader());
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

	public void testAssertionStatus() {
		try {
			assert false;
			fail();
		} catch (final AssertionError ae) {
			assertTrue(true);
		}
	}

	public void testSchemePath() throws ApplicationException {
		final Identifier userId = new Identifier("User_0");
		final Identifier domainId = new Identifier("Domain_0");
		final Identifier imageId = new Identifier("ImageResource_0");

		final LinkType linkType = LinkType.createInstance(userId, "codename", "description", "name", LinkTypeSort.LINKTYPESORT_ETHERNET, "manufacturer", "manufactirer code", imageId);
		final CableLinkType cableLinkType = CableLinkType.createInstance(userId, "codename", "description", "name", LinkTypeSort.LINKTYPESORT_GSM, "manufactirer", "manufactirer code", imageId);
		final CableThreadType cableThreadType = CableThreadType.createInstance(userId, "codename", "description", "name", 0, linkType, cableLinkType);

		final Scheme scheme = Scheme.createInstance(userId, "a scheme", Kind.BAY, domainId);
		final SchemePath schemePath = SchemePath.createInstance(userId, "a scheme path", scheme);
		System.out.println(schemePath.getParentScheme().getName());
		for (final SchemePath schemePath2 : scheme.getSchemePaths()) {
			System.out.println(schemePath2.getName());
		}

		final SchemeLink schemeLink = SchemeLink.createInstance(domainId, "a scheme link", scheme);
		final SchemeCableLink schemeCableLink = SchemeCableLink.createInstance(domainId, "a scheme cable link", scheme); 
		final SchemeCableThread schemeCableThread = SchemeCableThread.createInstance(domainId, "a scheme cable thread", cableThreadType, schemeCableLink);
		final SchemeElement schemeElement = SchemeElement.createInstance(userId, "a scheme element", scheme);
		final SchemeDevice schemeDevice = SchemeDevice.createInstance(userId, "a scheme device", schemeElement);
		final SchemePort startSchemePort = SchemePort.createInstance(userId, "starting scheme port", DirectionType._IN, schemeDevice);
		final SchemePort endSchemePort = SchemePort.createInstance(userId, "ending scheme port", DirectionType._OUT, schemeDevice);

		final PathElement pathElement1 = PathElement.createInstance(domainId, schemePath, schemeLink);
		final PathElement pathElement2 = PathElement.createInstance(userId, schemePath, schemeCableThread);
		final PathElement pathElement3 = PathElement.createInstance(userId, schemePath, startSchemePort, endSchemePort);

		final Identifier pathElementId1 = pathElement1.getId();
		final Identifier pathElementId2 = pathElement2.getId();
		final Identifier pathElementId3 = pathElement3.getId();

		for (final PathElement pathElement : schemePath.getPathElements()) {
			System.out.println("Sequential number: " + pathElement.getSequentialNumber() + ";\t parent scheme path: " + pathElement.getParentSchemePath().getName() + ";\t abstract scheme element: " + pathElement.getAbstractSchemeElement().getName() + ";\t kind: " + pathElement.getKind().value());
		}

		schemePath.removePathElement(pathElement2);

		for (final PathElement pathElement : schemePath.getPathElements()) {
			System.out.println("Sequential number: " + pathElement.getSequentialNumber() + ";\t parent scheme path: " + pathElement.getParentSchemePath().getName() + ";\t abstract scheme element: " + pathElement.getAbstractSchemeElement().getName() + ";\t kind: " + pathElement.getKind().value());
		}

		System.out.println(StorableObjectPool.getStorableObject(pathElementId1, true));
		System.out.println(StorableObjectPool.getStorableObject(pathElementId2, true));
		System.out.println(StorableObjectPool.getStorableObject(pathElementId3, true));
	}

	/**
	 * @see Scheme#setSchemePaths(java.util.Set)
	 */
	public void testSetSchemePaths() throws CreateObjectException {
		final Identifier userId = new Identifier("User_0");
		final Identifier domainId = new Identifier("Domain_0");

		final Scheme scheme0 = Scheme.createInstance(userId, "scheme0", Kind.BAY, domainId);
		final Scheme scheme1 = Scheme.createInstance(userId, "scheme1", Kind.BAY, domainId);

		@SuppressWarnings("unused")
		final SchemePath schemePath0 = SchemePath.createInstance(userId, "0", scheme0);
		@SuppressWarnings("unused")
		final SchemePath schemePath1 = SchemePath.createInstance(userId, "1", scheme0);
		@SuppressWarnings("unused")
		final SchemePath schemePath2 = SchemePath.createInstance(userId, "2", scheme0);
		final SchemePath schemePath3 = SchemePath.createInstance(userId, "3", scheme0);
		final SchemePath schemePath4 = SchemePath.createInstance(userId, "4", scheme0);
		final SchemePath schemePath5 = SchemePath.createInstance(userId, "5", scheme0);
		final SchemePath schemePath6 = SchemePath.createInstance(userId, "6", scheme0);
		final SchemePath schemePath7 = SchemePath.createInstance(userId, "7", scheme1);
		final SchemePath schemePath8 = SchemePath.createInstance(userId, "8", scheme1);
		final SchemePath schemePath9 = SchemePath.createInstance(userId, "9", scheme1);

		for (final SchemePath schemePath : scheme0.getSchemePaths()) {
			System.out.println(scheme0.getName() + ": " + schemePath.getName());
		}
		for (final SchemePath schemePath : scheme1.getSchemePaths()) {
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
		
		scheme0.setSchemePaths(schemePaths);

		for (final SchemePath schemePath : scheme0.getSchemePaths()) {
			System.out.println(scheme0.getName() + ": " + schemePath.getName());
		}
		for (final SchemePath schemePath : scheme1.getSchemePaths()) {
			System.out.println(scheme1.getName() + ": " + schemePath.getName());
		}
	}
}
