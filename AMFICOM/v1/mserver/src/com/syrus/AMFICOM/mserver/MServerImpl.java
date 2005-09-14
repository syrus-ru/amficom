/*-
 * $Id: MServerImpl.java,v 1.3 2005/09/14 18:15:00 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import com.syrus.AMFICOM.general.IdentifierGeneratorServerCore;
import com.syrus.AMFICOM.mserver.corba.MServerOperations;

/**
 * @version $Revision: 1.3 $, $Date: 2005/09/14 18:15:00 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */
final class MServerImpl extends IdentifierGeneratorServerCore implements MServerOperations {

	private static final long serialVersionUID = 395371850379497709L;

	MServerImpl() {
		super(MServerSessionEnvironment.getInstance().getConnectionManager(),
				MServerSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb());
	}

}
