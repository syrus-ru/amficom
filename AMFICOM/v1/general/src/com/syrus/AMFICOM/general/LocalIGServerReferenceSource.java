/*
 * $Id: LocalIGServerReferenceSource.java,v 1.1 2005/04/15 22:07:52 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/15 22:07:52 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class LocalIGServerReferenceSource implements IGServerReferenceSource {
	private LocalIdentifierGeneratorServer localIdentifierGeneratorServer;

	public LocalIGServerReferenceSource(LocalIdentifierGeneratorServer localIdentifierGeneratorServer) {
		this.localIdentifierGeneratorServer = localIdentifierGeneratorServer;
	}

	public IdentifierGeneratorServer getVerifiedIGServerReference() throws CommunicationException {
		return this.localIdentifierGeneratorServer;
	}

}
