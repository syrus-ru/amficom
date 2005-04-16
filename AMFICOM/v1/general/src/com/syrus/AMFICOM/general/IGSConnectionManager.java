/*
 * $Id: IGSConnectionManager.java,v 1.1 2005/04/16 21:05:55 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/16 21:05:55 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public interface IGSConnectionManager {

	IdentifierGeneratorServer getVerifiedIGSReference() throws CommunicationException;
}
