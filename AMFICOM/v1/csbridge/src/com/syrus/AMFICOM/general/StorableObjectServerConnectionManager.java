/*-
 * $Id: StorableObjectServerConnectionManager.java,v 1.1.2.1 2006/06/27 15:47:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.systemserver.corba.StorableObjectServer;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/06/27 15:47:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module csbridge
 */
public interface StorableObjectServerConnectionManager {

	StorableObjectServer getStorableObjectServerReference() throws CommunicationException;

	CORBAServer getCORBAServer();
}
