/*-
 * $Id: SchemePathTestCase.java,v 1.1 2005/07/15 08:47:17 bass Exp $
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
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IGSConnectionManager;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.Kind;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/07/15 08:47:17 $
 * @module scheme_v1
 */
public class SchemePathTestCase extends TestCase {
	public static void main(String[] args) {
		TestRunner.run(SchemePathTestCase.class);
	}

	public void testSchemePath() throws CreateObjectException {
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

		PathElement.createInstance(domainId, schemePath, schemeLink);
		PathElement.createInstance(userId, schemePath, schemeCableThread);		

		for (final PathElement pathElement : schemePath.getPathElements()) {
			System.out.println("" + pathElement.getSequentialNumber() + '\t' + pathElement.getParentSchemePath().getName() + '\t' + pathElement.getAbstractSchemeElement().getName());
		}
	}
}
