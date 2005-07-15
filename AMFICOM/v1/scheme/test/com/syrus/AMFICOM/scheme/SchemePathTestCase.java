/*-
 * $Id: SchemePathTestCase.java,v 1.2 2005/07/15 09:49:24 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

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
 * @version $Revision: 1.2 $, $Date: 2005/07/15 09:49:24 $
 * @module scheme_v1
 */
public class SchemePathTestCase extends TestCase {
	public static void main(String[] args) {
		TestRunner.run(SchemePathTestCase.class);
	}

	public void testSchemePath() throws ApplicationException {
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
		final SchemePort startSchemePort = SchemePort.createInstance(userId, "starting schem port", DirectionType._IN, schemeDevice);
		final SchemePort endSchemePort = SchemePort.createInstance(userId, "ending schem port", DirectionType._OUT, schemeDevice);

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
}
