/*
 * $Id: IGServerReferenceSource.java,v 1.1 2005/04/15 19:17:18 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/15 19:17:18 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public interface IGServerReferenceSource {

	IdentifierGeneratorServer getVerifiedIGServerReference() throws CommunicationException;
}
