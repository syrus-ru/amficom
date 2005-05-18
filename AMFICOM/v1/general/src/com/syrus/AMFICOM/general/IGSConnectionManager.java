/*
 * $Id: IGSConnectionManager.java,v 1.3 2005/05/18 11:07:39 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.IdentifierGeneratorServer;

/**
 * @version $Revision: 1.3 $, $Date: 2005/05/18 11:07:39 $
 * @author $Author: bass $
 * @module general_v1
 */
public interface IGSConnectionManager {

	IdentifierGeneratorServer getIGSReference() throws CommunicationException;
}
