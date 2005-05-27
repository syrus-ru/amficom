/*-
 * $Id: ServerConnectionManager.java,v 1.4 2005/05/27 16:24:44 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.CommonServer;

/**
 * @version $Revision: 1.4 $, $Date: 2005/05/27 16:24:44 $
 * @author $Author: bass $
 * @module csbridge_v1
 */
public interface ServerConnectionManager {
	CommonServer getServerReference() throws CommunicationException;
}
