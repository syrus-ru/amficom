/*
 * $Id: IdentifierGeneratorServerConnectionManager.java,v 1.1.2.1 2006/06/27 15:48:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.systemserver.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/27 15:48:10 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public interface IdentifierGeneratorServerConnectionManager {

	IdentifierGeneratorServer getIdentifierGeneratorServerReference() throws CommunicationException;
}
