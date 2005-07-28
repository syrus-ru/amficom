/*-
 * $Id: MServerImpl.java,v 1.1 2005/07/28 18:20:49 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import com.syrus.AMFICOM.general.IdentifierGeneratorServerCore;
import com.syrus.AMFICOM.mserver.corba.MServerOperations;

/**
 * @version $Revision: 1.1 $, $Date: 2005/07/28 18:20:49 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */
final class MServerImpl extends IdentifierGeneratorServerCore implements MServerOperations {

	private static final long serialVersionUID = 395371850379497709L;

	MServerImpl() {
		super(MServerSessionEnvironment.getInstance().getConnectionManager(),
				MServerSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb());
	}

}
