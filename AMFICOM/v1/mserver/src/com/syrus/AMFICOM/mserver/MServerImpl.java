/*-
 * $Id: MServerImpl.java,v 1.4 2005/11/28 12:35:30 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import com.syrus.AMFICOM.general.IdentifierGeneratorServerCore;
import com.syrus.AMFICOM.mserver.corba.MServerOperations;

/**
 * @version $Revision: 1.4 $, $Date: 2005/11/28 12:35:30 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module mserver
 */
final class MServerImpl extends IdentifierGeneratorServerCore implements MServerOperations {

	private static final long serialVersionUID = 395371850379497709L;

	MServerImpl(final MServerServantManager mServerServantManager) {
		super(mServerServantManager, mServerServantManager.getCORBAServer().getOrb());
	}

}
