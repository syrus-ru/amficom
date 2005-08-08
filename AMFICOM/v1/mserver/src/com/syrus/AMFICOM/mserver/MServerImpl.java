/*-
 * $Id: MServerImpl.java,v 1.2 2005/08/08 11:43:53 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import com.syrus.AMFICOM.general.IdentifierGeneratorServerCore;
import com.syrus.AMFICOM.mserver.corba.MServerOperations;

/**
 * @version $Revision: 1.2 $, $Date: 2005/08/08 11:43:53 $
 * @author $Author: arseniy $
 * @module mserver
 */
final class MServerImpl extends IdentifierGeneratorServerCore implements MServerOperations {

	private static final long serialVersionUID = 395371850379497709L;

	MServerImpl() {
		super(MServerSessionEnvironment.getInstance().getConnectionManager(),
				MServerSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb());
	}

}
