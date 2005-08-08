/*
 * $Id: IGSConnectionManager.java,v 1.4 2005/08/08 11:27:25 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.4 $, $Date: 2005/08/08 11:27:25 $
 * @author $Author: arseniy $
 * @module general
 */
public interface IGSConnectionManager {

	IdentifierGeneratorServer getIGSReference() throws CommunicationException;
}
