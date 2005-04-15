/*
 * $Id: DefaultIGServerReferenceSource.java,v 1.1 2005/04/15 19:16:21 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/15 19:16:21 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class DefaultIGServerReferenceSource implements IGServerReferenceSource {
	private IdentifierGeneratorServer igServer;

	public DefaultIGServerReferenceSource(IdentifierGeneratorServer igServer) {
		this.igServer = igServer;
	}

	public IdentifierGeneratorServer getVerifiedIGServerReference() throws CommunicationException {
		return this.igServer;
	}

}
