/*
 * $Id: IGSConnectionManager.java,v 1.5 2005/09/14 18:51:55 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.5 $, $Date: 2005/09/14 18:51:55 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public interface IGSConnectionManager {

	IdentifierGeneratorServer getIGSReference() throws CommunicationException;
}
