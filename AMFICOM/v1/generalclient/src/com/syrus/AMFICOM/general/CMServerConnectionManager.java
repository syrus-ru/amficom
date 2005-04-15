/*
 * $Id: CMServerConnectionManager.java,v 1.1 2005/04/15 22:18:12 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/15 22:18:12 $
 * @author $Author: arseniy $
 * @module generalclient_v1
 */
public final class CMServerConnectionManager extends VerifiedReferenceSource implements IGServerReferenceSource {

	public CMServerConnectionManager(CORBAServer corbaServer, String cmServerServantName) {
		super(corbaServer, cmServerServantName);
	}

	public IdentifierGeneratorServer getVerifiedIGServerReference() throws CommunicationException {
		return (IdentifierGeneratorServer) super.getVerifiedReference();
	}

	public CMServer getVerifiedCMServerReference() throws CommunicationException {
		return (CMServer) super.getVerifiedReference();
	}
}
